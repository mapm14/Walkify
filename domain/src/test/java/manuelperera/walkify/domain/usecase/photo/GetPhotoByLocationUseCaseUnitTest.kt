package manuelperera.walkify.domain.usecase.photo

import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.whenever
import io.reactivex.Single
import manuelperera.walkify.domain.entity.photo.Photo
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner

private const val LATITUDE = 0.32958
private const val LONGITUDE = 2.587335
private const val PHOTO_ID = "kadsa932832fsd"

@RunWith(MockitoJUnitRunner::class)
class GetPhotoByLocationUseCaseUnitTest {

    @Mock
    private lateinit var getPhotoIdByLocationUseCase: GetPhotoIdByLocationUseCase

    @Mock
    private lateinit var getPhotoByIdUseCase: GetPhotoByIdUseCase

    private lateinit var useCase: GetPhotoByLocationUseCase
    private lateinit var params: GetPhotoByLocationParams

    @Before
    fun setUp() {
        useCase = GetPhotoByLocationUseCase(
            getPhotoIdByLocationUseCase = getPhotoIdByLocationUseCase,
            getPhotoByIdUseCase = getPhotoByIdUseCase
        )

        params = GetPhotoByLocationParams(
            latitude = LATITUDE,
            longitude = LONGITUDE
        )
    }

    @Test
    fun `invoke should return Single with Photo`() {
        val getPhotoIdByLocationParams = GetPhotoIdByLocationParams(params.latitude, params.longitude)
        whenever(getPhotoIdByLocationUseCase(getPhotoIdByLocationParams))
            .doReturn(Single.just(PHOTO_ID))
        val getPhotoByIdParams = GetPhotoByIdParams(PHOTO_ID)
        val photo = Photo.empty()
        whenever(getPhotoByIdUseCase(getPhotoByIdParams))
            .doReturn(Single.just(photo))

        val testObserver = useCase(params).test()

        testObserver.assertResult(photo)
    }

}