package manuelperera.walkify.data.repository.photo.data

import io.reactivex.Single
import manuelperera.walkify.data.entity.photo.response.PhotoPaginationData
import manuelperera.walkify.data.entity.photo.response.PhotoSizeWrapperData
import retrofit2.adapter.rxjava2.Result
import retrofit2.http.GET
import retrofit2.http.Query

interface PhotoApi {

    @GET("rest")
    fun getPhotoIdByLocation(
        @Query("lat") latitude: Double,
        @Query("lon") longitude: Double,
        @Query("accuracy") accuracy: Int = 16, // street accuracy
        @Query("content_type") contentType: Int = 1, // just photos
        @Query("geo_context") geoContext: Int = 2, // outdoors
        @Query("radius") radius: Double = 0.09, // radius in Km
        @Query("method") method: String = "flickr.photos.search",
        @Query("per_page") perPage: Int = 1
    ): Single<Result<PhotoPaginationData>>

    @GET("rest")
    fun getPhotoById(
        @Query("photo_id") id: String,
        @Query("method") method: String = "flickr.photos.getSizes"
    ): Single<Result<PhotoSizeWrapperData>>

}