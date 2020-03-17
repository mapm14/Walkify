package manuelperera.walkify.data.repository.photo.data

import io.reactivex.Single
import manuelperera.walkify.data.entity.photo.request.LocationRequest
import manuelperera.walkify.data.entity.photo.response.PhotoResponse
import retrofit2.adapter.rxjava2.Result
import retrofit2.http.Body
import retrofit2.http.GET

interface PhotoApi {

    @GET("photo")
    fun getPhotoByLocation(
        @Body body: LocationRequest
    ): Single<Result<PhotoResponse>>

}