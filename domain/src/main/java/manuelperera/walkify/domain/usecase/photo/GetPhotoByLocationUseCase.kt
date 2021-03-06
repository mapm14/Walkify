package manuelperera.walkify.domain.usecase.photo

import io.reactivex.Single
import manuelperera.walkify.domain.entity.photo.Photo
import manuelperera.walkify.domain.usecase.base.SingleUseCase
import javax.inject.Inject

/**
 *  Would return [manuelperera.walkify.domain.entity.base.Failure.NotFound] if there is no photos
 *  for that given location
 */
class GetPhotoByLocationUseCase @Inject constructor(
    private val getPhotoIdByLocationUseCase: GetPhotoIdByLocationUseCase,
    private val getPhotoByIdUseCase: GetPhotoByIdUseCase
) : SingleUseCase<Photo, GetPhotoByLocationParams> {

    override fun invoke(params: GetPhotoByLocationParams): Single<Photo> =
        getPhotoIdByLocationUseCase(GetPhotoIdByLocationParams(params.latitude, params.longitude))
            .flatMap { photoId ->
                val getPhotoParams = GetPhotoByIdParams(photoId)
                getPhotoByIdUseCase(getPhotoParams)
            }

}

data class GetPhotoByLocationParams(
    val latitude: Double,
    val longitude: Double
)