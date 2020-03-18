package manuelperera.walkify.domain.usecase.photo

import io.reactivex.Single
import manuelperera.walkify.domain.entity.photo.PhotoSizeInfo
import manuelperera.walkify.domain.usecase.base.SingleUseCase
import javax.inject.Inject

class GetPhotoUrlByLocationUseCase @Inject constructor(
    private val getPhotoIdByLocationUseCase: GetPhotoIdByLocationUseCase,
    private val getPhotoUrlByIdUseCase: GetPhotoUrlByIdUseCase
) : SingleUseCase<String, GetPhotoUrlByLocationParams> {

    override fun invoke(params: GetPhotoUrlByLocationParams): Single<String> =
        getPhotoIdByLocationUseCase(GetPhotoIdByLocationParams(params.latitude, params.longitude))
            .flatMap { photoId ->
                val getPhotoParams = GetPhotoUrlByIdParams(photoId, params.selectedLabel)
                getPhotoUrlByIdUseCase(getPhotoParams)
            }

}

class GetPhotoUrlByLocationParams(
    val latitude: Double,
    val longitude: Double,
    val selectedLabel: PhotoSizeInfo.Label
)