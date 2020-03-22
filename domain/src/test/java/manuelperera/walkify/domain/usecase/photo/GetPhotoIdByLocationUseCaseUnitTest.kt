package manuelperera.walkify.domain.usecase.photo

import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.whenever
import io.reactivex.Single
import manuelperera.walkify.domain.repository.PhotoRepository
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner

private const val LATITUDE = 0.32958
private const val LONGITUDE = 2.587335
private const val PHOTO_ID = "kadsa932832fsd"

@RunWith(MockitoJUnitRunner::class)
class GetPhotoIdByLocationUseCaseUnitTest {

    @Mock
    private lateinit var photoRepository: PhotoRepository

    private lateinit var useCase: GetPhotoIdByLocationUseCase
    private lateinit var params: GetPhotoIdByLocationParams

    @Before
    fun setUp() {
        useCase = GetPhotoIdByLocationUseCase(
            photoRepository = photoRepository
        )

        params = GetPhotoIdByLocationParams(
            latitude = LATITUDE,
            longitude = LONGITUDE
        )
    }

    @Test
    fun `invoke should return Single with PHOTO_ID`() {
        whenever(photoRepository.getPhotoIdByLocation(params.latitude, params.longitude))
            .doReturn(Single.just(PHOTO_ID))

        val testObserver = useCase(params).test()

        testObserver.assertResult(PHOTO_ID)
    }

}