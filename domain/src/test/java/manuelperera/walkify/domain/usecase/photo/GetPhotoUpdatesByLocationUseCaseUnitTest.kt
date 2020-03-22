package manuelperera.walkify.domain.usecase.photo

import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.whenever
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.rxkotlin.flatMapIterable
import manuelperera.walkify.domain.entity.base.Failure
import manuelperera.walkify.domain.entity.location.GpsLocation
import manuelperera.walkify.domain.entity.photo.Photo
import manuelperera.walkify.domain.usecase.location.GetLocationRefreshesParams
import manuelperera.walkify.domain.usecase.location.GetLocationRefreshesUseCase
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner
import kotlin.random.Random

private const val SMALLEST_DISPLACEMENT = 100F

@RunWith(MockitoJUnitRunner::class)
class GetPhotoUpdatesByLocationUseCaseUnitTest {

    @Mock
    private lateinit var getLocationRefreshesUseCase: GetLocationRefreshesUseCase

    @Mock
    private lateinit var getPhotoByLocationUseCase: GetPhotoByLocationUseCase

    private lateinit var useCase: GetPhotoUpdatesByLocationUseCase
    private val params: GetPhotosUpdatesByLocationParams = GetPhotosUpdatesByLocationParams(SMALLEST_DISPLACEMENT)

    @Before
    fun setUp() {
        useCase = GetPhotoUpdatesByLocationUseCase(
            getLocationRefreshesUseCase = getLocationRefreshesUseCase,
            getPhotoByLocationUseCase = getPhotoByLocationUseCase
        )
    }

    private fun getListOfGpsLocation(size: Int = Random.nextInt(2, 5)): List<GpsLocation> {
        val mutableList = mutableListOf<GpsLocation>()
        for (i in 0..size) {
            mutableList.add(
                GpsLocation(
                    latitude = Random.nextDouble(),
                    longitude = Random.nextDouble(),
                    isLastKnownLocation = Random.nextBoolean(),
                    accuracy = Random.nextFloat()
                )
            )
        }
        return mutableList
    }

    @Test
    fun `invoke should return Observable with Photos`() {
        val gpsLocationList = getListOfGpsLocation()
        val observable: Observable<GpsLocation> = Observable.just(gpsLocationList).flatMapIterable()
        val getLocationRefreshesParams = GetLocationRefreshesParams(params.smallestDisplacementInMeters)
        whenever(getLocationRefreshesUseCase(getLocationRefreshesParams))
            .doReturn(observable)
        val photoResultList = mutableListOf<Photo>()
        gpsLocationList.forEach { gpsLocation ->
            val getPhotoByLocationParams = GetPhotoByLocationParams(gpsLocation.latitude, gpsLocation.longitude)
            val photo = Photo(Random.nextInt().toString(), emptyList())
            photoResultList.add(photo)
            whenever(getPhotoByLocationUseCase(getPhotoByLocationParams))
                .doReturn(Single.just(photo))
        }

        val testObserver = useCase(params).test()

        testObserver.assertValueCount(gpsLocationList.size)
        testObserver.assertResult(*photoResultList.toTypedArray())
    }

    @Test
    fun `invoke should return Observable with Photos when GetPhotoByLocationUseCase returns Failure-NotFound`() {
        returnObservableWithPhotosWhenFailureOccurs(Failure.NotFound)
    }

    @Test
    fun `invoke should return Observable with Photos when GetPhotoByLocationUseCase returns Failure-NoInternet`() {
        returnObservableWithPhotosWhenFailureOccurs(Failure.NoInternet("No Internet"))
    }

    @Test
    fun `invoke should return Observable with Photos when GetPhotoByLocationUseCase returns Failure-Timeout`() {
        returnObservableWithPhotosWhenFailureOccurs(Failure.Timeout("Slow connection"))
    }

    private fun returnObservableWithPhotosWhenFailureOccurs(failure: Failure) {
        val gpsLocationList = getListOfGpsLocation()
        val observable: Observable<GpsLocation> = Observable.just(gpsLocationList).flatMapIterable()
        val getLocationRefreshesParams = GetLocationRefreshesParams(params.smallestDisplacementInMeters)
        whenever(getLocationRefreshesUseCase(getLocationRefreshesParams))
            .doReturn(observable)
        val photoResultList = mutableListOf<Photo>()
        gpsLocationList.forEach { gpsLocation ->
            val getPhotoByLocationParams = GetPhotoByLocationParams(gpsLocation.latitude, gpsLocation.longitude)
            val isError = Random.nextBoolean()
            val singlePhoto = if (isError) {
                photoResultList.add(Photo.empty())
                Single.error(failure)
            } else {
                val photo = Photo(Random.nextInt().toString(), emptyList())
                photoResultList.add(photo)
                Single.just(photo)
            }
            whenever(getPhotoByLocationUseCase(getPhotoByLocationParams))
                .doReturn(singlePhoto)
        }

        val testObserver = useCase(params).test()

        testObserver.assertValueCount(gpsLocationList.size)
        testObserver.assertResult(*photoResultList.toTypedArray())
    }

    @Test
    fun `invoke should return Observable error when GetPhotoByLocationUseCase returns Failure different than NotFound`() {
        val gpsLocationList = getListOfGpsLocation(2)
        val observable: Observable<GpsLocation> = Observable.just(gpsLocationList).flatMapIterable()
        val getLocationRefreshesParams = GetLocationRefreshesParams(params.smallestDisplacementInMeters)
        whenever(getLocationRefreshesUseCase(getLocationRefreshesParams))
            .doReturn(observable)
        val unauthorized = Failure.Unauthorized
        gpsLocationList.forEach { gpsLocation ->
            val getPhotoByLocationParams = GetPhotoByLocationParams(gpsLocation.latitude, gpsLocation.longitude)
            whenever(getPhotoByLocationUseCase(getPhotoByLocationParams))
                .doReturn(Single.error(unauthorized))
        }

        val testObserver = useCase(params).test()

        testObserver.assertError(unauthorized)
    }

}