package manuelperera.walkify.domain.usecase.photo

import io.reactivex.Single
import manuelperera.walkify.domain.repository.PhotoRepository
import manuelperera.walkify.domain.usecase.base.SingleUseCase
import javax.inject.Inject

class GetPhotoIdByLocationUseCase @Inject constructor(
    private val photoRepository: PhotoRepository
) : SingleUseCase<String, GetPhotoIdByLocationParams> {

    override fun invoke(params: GetPhotoIdByLocationParams): Single<String> =
        photoRepository.getPhotoIdByLocation(params.latitude, params.longitude)

}

class GetPhotoIdByLocationParams(
    val latitude: Double,
    val longitude: Double
)