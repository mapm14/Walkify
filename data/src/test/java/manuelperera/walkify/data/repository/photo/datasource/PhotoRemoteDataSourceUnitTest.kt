package manuelperera.walkify.data.repository.photo.datasource

import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.whenever
import manuelperera.walkify.data.entity.photo.response.PhotoPaginationDataFactory
import manuelperera.walkify.data.entity.photo.response.PhotoSizeWrapperDataFactory
import manuelperera.walkify.data.extensions.getSingleResultSuccess
import manuelperera.walkify.data.repository.photo.data.PhotoApi
import manuelperera.walkify.domain.entity.photo.Photo
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner

private const val PHOTO_ID = "jna823enmasd2"
private const val LATITUDE = 0.2313
private const val LONGITUDE = 32.3414

@RunWith(MockitoJUnitRunner::class)
class PhotoRemoteDataSourceUnitTest {

    @Mock
    private lateinit var photoApi: PhotoApi

    private lateinit var remoteDataSource: PhotoRemoteDataSource

    @Before
    fun setUp() {
        remoteDataSource = PhotoRemoteDataSource(
            photoApi = photoApi
        )
    }

    @Test
    fun `getPhotoIdByLocation should return Single with photo id`() {
        val photoPaginationData = PhotoPaginationDataFactory.providesPhotoPaginationData()
        whenever(photoApi.getPhotoIdByLocation(LATITUDE, LONGITUDE))
            .doReturn(getSingleResultSuccess(photoPaginationData))

        val testObserver = remoteDataSource.getPhotoIdByLocation(LATITUDE, LONGITUDE).test()

        testObserver.awaitTerminalEvent()
        testObserver.assertResult(photoPaginationData.toDomain())
    }

    @Test
    fun `getPhotoById should return Single with Photo`() {
        val photoSizeWrapperData = PhotoSizeWrapperDataFactory.providesPhotoSizeWrapperData()
        whenever(photoApi.getPhotoById(PHOTO_ID))
            .doReturn(getSingleResultSuccess(photoSizeWrapperData))

        val testObserver = remoteDataSource.getPhotoById(PHOTO_ID).test()

        val result = Photo(PHOTO_ID, photoSizeWrapperData.toDomain())
        testObserver.awaitTerminalEvent()
        testObserver.assertResult(result)
    }

}