package manuelperera.walkify.domain.usecase.photo

import io.reactivex.Single
import manuelperera.walkify.domain.repository.PhotoRepository
import manuelperera.walkify.domain.usecase.base.SingleUseCase
import javax.inject.Inject

/**
 *  Would return [manuelperera.walkify.domain.entity.base.Failure.NotFound] if there is no photos
 *  for that given location
 */
class GetPhotoIdByLocationUseCase @Inject constructor(
    private val photoRepository: PhotoRepository
) : SingleUseCase<String, GetPhotoIdByLocationParams> {

    override fun invoke(params: GetPhotoIdByLocationParams): Single<String> =
        photoRepository.getPhotoIdByLocation(params.latitude, params.longitude)

}

data class GetPhotoIdByLocationParams(
    val latitude: Double,
    val longitude: Double
)