package manuelperera.walkify.domain.usecase.photo

import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.whenever
import io.reactivex.Single
import manuelperera.walkify.domain.entity.photo.Photo
import manuelperera.walkify.domain.repository.PhotoRepository
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner

private const val PHOTO_ID = "as87fmved12v"

@RunWith(MockitoJUnitRunner::class)
class GetPhotoByIdUseCaseUnitTest {

    @Mock
    private lateinit var photoRepository: PhotoRepository

    private lateinit var useCase: GetPhotoByIdUseCase
    private val params: GetPhotoByIdParams = GetPhotoByIdParams(PHOTO_ID)

    @Before
    fun setUp() {
        useCase = GetPhotoByIdUseCase(
            photoRepository = photoRepository
        )
    }

    @Test
    fun `invoke should return Single with Photo`() {
        val photo = Photo.empty()
        whenever(photoRepository.getPhotoById(PHOTO_ID))
            .doReturn(Single.just(photo))

        val testObserver = useCase(params).test()

        testObserver.assertResult(photo)
    }

}