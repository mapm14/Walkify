package manuelperera.walkify.domain.usecase.location

import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.whenever
import io.reactivex.Observable
import io.reactivex.rxkotlin.flatMapIterable
import manuelperera.walkify.domain.entity.location.GpsLocation
import manuelperera.walkify.domain.provider.AndroidLocationProvider
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner
import kotlin.random.Random

@RunWith(MockitoJUnitRunner::class)
class GetLocationRefreshesUseCaseUnitTest {

    @Mock
    private lateinit var androidLocationProvider: AndroidLocationProvider

    private lateinit var useCase: GetLocationRefreshesUseCase
    private val params = GetLocationRefreshesParams(100f)

    @Before
    fun setUp() {
        useCase = GetLocationRefreshesUseCase(
            androidLocationProvider = androidLocationProvider
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
    fun `invoke should return Observable with GpsLocations`() {
        val gpsLocationList = getListOfGpsLocation()
        val observable: Observable<GpsLocation> = Observable.just(gpsLocationList).flatMapIterable()
        whenever(androidLocationProvider.locationUpdatesPeriodically(params.smallestDisplacementInMeters))
            .doReturn(observable)

        val testObserver = useCase(params).test()

        testObserver.awaitTerminalEvent()
        testObserver.assertResult(*gpsLocationList.toTypedArray())
    }

}