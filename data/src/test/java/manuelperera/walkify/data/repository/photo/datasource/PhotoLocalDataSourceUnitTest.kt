package manuelperera.walkify.data.repository.photo.datasource

import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import io.reactivex.Completable
import io.reactivex.Observable
import manuelperera.walkify.data.datasource.local.AppDatabase
import manuelperera.walkify.data.entity.photo.database.PhotoDao
import manuelperera.walkify.data.entity.photo.database.PhotoEntityDbFactory
import manuelperera.walkify.domain.entity.photo.Photo
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class PhotoLocalDataSourceUnitTest {

    @Mock
    private lateinit var database: AppDatabase

    @Mock
    private lateinit var photoDao: PhotoDao

    private lateinit var localDataSource: PhotoLocalDataSource

    @Before
    fun setUp() {
        localDataSource = PhotoLocalDataSource(
            database = database
        )

        whenever(database.photoDao())
            .doReturn(photoDao)
    }

    @Test
    fun `getPhotoListUpdates should return Observable with List of Photo`() {
        val list = listOf(
            PhotoEntityDbFactory.providesPhotoEntityDb(id = "1"),
            PhotoEntityDbFactory.providesPhotoEntityDb(id = "2")
        )
        whenever(photoDao.getAll())
            .doReturn(Observable.just(list))

        val testObserver = localDataSource.getPhotoListUpdates().test()

        testObserver.assertResult(list.map { it.toDomain() })
    }

    @Test
    fun `insert should insert PhotoEntityDb`() {
        val photo = Photo.empty()

        localDataSource.insert(photo)

        verify(photoDao).insertAll(any())
    }

    @Test
    fun `clearDatabase should return Completable that completes`() {
        whenever(photoDao.deleteAll())
            .doReturn(Completable.complete())

        val testObserver = localDataSource.clearDatabase().test()

        testObserver.assertComplete()
    }

}