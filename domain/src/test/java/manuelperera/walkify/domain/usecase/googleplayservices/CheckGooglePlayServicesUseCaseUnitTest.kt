package manuelperera.walkify.domain.usecase.googleplayservices

import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.whenever
import io.reactivex.Completable
import manuelperera.walkify.domain.provider.GooglePlayServicesHandler
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class CheckGooglePlayServicesUseCaseUnitTest {

    @Mock
    private lateinit var googlePlayServicesHandler: GooglePlayServicesHandler

    private lateinit var useCase: CheckGooglePlayServicesUseCase

    @Before
    fun setUp() {
        useCase = CheckGooglePlayServicesUseCase(
            googlePlayServicesHandler = googlePlayServicesHandler
        )
    }

    @Test
    fun `invoke should return Completable that completes`() {
        whenever(googlePlayServicesHandler.checkPlayServices())
            .doReturn(Completable.complete())

        val testObserver = useCase(Unit).test()

        testObserver.awaitTerminalEvent()
        testObserver.assertComplete()
    }

}