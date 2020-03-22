package manuelperera.walkify.domain.usecase.photo

import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.whenever
import io.reactivex.Observable
import manuelperera.walkify.domain.entity.photo.Photo
import manuelperera.walkify.domain.repository.PhotoRepository
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner
import kotlin.random.Random

@RunWith(MockitoJUnitRunner::class)
class GetPhotoUpdatesUseCaseUnitTest {

    @Mock
    private lateinit var photoRepository: PhotoRepository

    private lateinit var useCase: GetPhotoUpdatesUseCase

    @Before
    fun setUp() {
        useCase = GetPhotoUpdatesUseCase(
            photoRepository = photoRepository
        )
    }

    private fun getPhotoList(size: Int = Random.nextInt(2, 5)) : List<Photo> {
        val mutableList = mutableListOf<Photo>()
        for (i in 0..size) {
            mutableList.add(Photo(Random.nextInt().toString(), emptyList()))
        }
        return mutableList
    }

    @Test
    fun `invoke should return Observable with List of Photos`() {
        val photoList = getPhotoList()
        whenever(photoRepository.getPhotoUpdates())
            .doReturn(Observable.just(photoList))

        val testObserver = useCase(Unit).test()

        testObserver.awaitTerminalEvent()
        testObserver.assertResult(photoList)
    }

}