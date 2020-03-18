package manuelperera.walkify.domain.usecase.photo

import io.reactivex.Single
import manuelperera.walkify.domain.entity.photo.PhotoSizeInfo
import manuelperera.walkify.domain.repository.PhotoRepository
import manuelperera.walkify.domain.usecase.base.SingleUseCase
import javax.inject.Inject

class GetPhotoUrlByIdUseCase @Inject constructor(
    private val photoRepository: PhotoRepository
) : SingleUseCase<String, GetPhotoUrlByIdParams> {

    override fun invoke(params: GetPhotoUrlByIdParams): Single<String> =
        photoRepository.getPhotoById(params.id)
            .map { photoSizeInfoList ->
                photoSizeInfoList.first { it.label == params.selectedLabel }.url
            }

}

class GetPhotoUrlByIdParams(
    val id: String,
    val selectedLabel: PhotoSizeInfo.Label
)