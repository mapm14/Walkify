package manuelperera.walkify.data.repository.photo.datasource

import io.reactivex.Single
import manuelperera.walkify.data.datasource.remote.BaseRemoteDataSource
import manuelperera.walkify.data.entity.photo.request.LocationRequest
import manuelperera.walkify.data.repository.photo.data.PhotoApi
import javax.inject.Inject
import kotlin.random.Random

class PhotoRemoteDataSource @Inject constructor(
    private val photoApi: PhotoApi
) : BaseRemoteDataSource() {

    fun getPhotoByLocation(latitude: Double, longitude: Double): Single<String> {
        return Single.just("Url --- ${Random.nextInt()}")

//        val request = LocationRequest(latitude, longitude)
//        return modifySingle(photoApi.getPhotoByLocation(request))
    }

}