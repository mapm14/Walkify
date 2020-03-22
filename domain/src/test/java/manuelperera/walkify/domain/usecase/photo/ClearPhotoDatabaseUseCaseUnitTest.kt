package manuelperera.walkify.domain.usecase.photo

import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.whenever
import io.reactivex.Completable
import manuelperera.walkify.domain.repository.PhotoRepository
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class ClearPhotoDatabaseUseCaseUnitTest {

    @Mock
    private lateinit var photoRepository: PhotoRepository

    private lateinit var useCase: ClearPhotoDatabaseUseCase

    @Before
    fun setUp() {
        useCase = ClearPhotoDatabaseUseCase(
            photoRepository = photoRepository
        )
    }

    @Test
    fun `invoke should return Completable that completes`() {
        whenever(photoRepository.clearDatabase())
            .doReturn(Completable.complete())

        val testObserver = useCase(Unit).test()

        testObserver.awaitTerminalEvent()
        testObserver.assertComplete()
    }

}