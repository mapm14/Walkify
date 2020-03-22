package manuelperera.walkify.data.repository.photo

import com.nhaarman.mockitokotlin2.*
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single
import manuelperera.walkify.data.repository.photo.datasource.PhotoLocalDataSource
import manuelperera.walkify.data.repository.photo.datasource.PhotoRemoteDataSource
import manuelperera.walkify.domain.entity.base.Failure
import manuelperera.walkify.domain.entity.photo.Photo
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner

private const val PHOTO_ID = "7nsf2rnaf3"
private const val LATITUDE = 0.2313
private const val LONGITUDE = 32.3414

@RunWith(MockitoJUnitRunner::class)
class PhotoRepositoryImplUnitTest {

    @Mock
    private lateinit var photoRemoteDataSource: PhotoRemoteDataSource

    @Mock
    private lateinit var photoLocalDataSource: PhotoLocalDataSource

    private lateinit var repository: PhotoRepositoryImpl

    @Before
    fun setUp() {
        repository = PhotoRepositoryImpl(
            photoRemoteDataSource = photoRemoteDataSource,
            photoLocalDataSource = photoLocalDataSource
        )
    }

    @Test
    fun `getPhotoIdByLocation should return Single with PHOTO_ID`() {
        whenever(photoRemoteDataSource.getPhotoIdByLocation(LATITUDE, LONGITUDE))
            .doReturn(Single.just(PHOTO_ID))

        val testObserver = repository.getPhotoIdByLocation(LATITUDE, LONGITUDE).test()

        testObserver.assertResult(PHOTO_ID)
    }

    @Test
    fun `getPhotoById should return Single with Photo and save in Local Data Source`() {
        val photo = Photo.empty()
        whenever(photoRemoteDataSource.getPhotoById(PHOTO_ID))
            .doReturn(Single.just(photo))

        val testObserver = repository.getPhotoById(PHOTO_ID).test()

        testObserver.assertResult(photo)
        verify(photoLocalDataSource).insert(photo)
    }

    @Test
    fun `getPhotoById should return Single with error and shouldn't save in Local Data Source`() {
        val failure = Failure.Error("")
        whenever(photoRemoteDataSource.getPhotoById(PHOTO_ID))
            .doReturn(Single.error(failure))

        val testObserver = repository.getPhotoById(PHOTO_ID).test()

        testObserver.assertError(failure)
        verify(photoLocalDataSource, never()).insert(any())
    }

    @Test
    fun `getPhotoUpdates should return Observable with List of Photo`() {
        val photoList = listOf(Photo.empty(), Photo(PHOTO_ID, emptyList()))
        whenever(photoLocalDataSource.getPhotoListUpdates())
            .doReturn(Observable.just(photoList))

        val testObserver = repository.getPhotoUpdates().test()

        testObserver.assertResult(photoList)
    }

    @Test
    fun `clearDatabase should return Completable that completes`() {
        whenever(photoLocalDataSource.clearDatabase())
            .doReturn(Completable.complete())

        val testObserver = repository.clearDatabase().test()

        testObserver.assertComplete()
    }

}