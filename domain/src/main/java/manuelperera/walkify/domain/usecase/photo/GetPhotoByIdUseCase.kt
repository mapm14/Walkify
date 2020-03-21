package manuelperera.walkify.domain.usecase.photo

import io.reactivex.Single
import manuelperera.walkify.domain.entity.photo.Photo
import manuelperera.walkify.domain.repository.PhotoRepository
import manuelperera.walkify.domain.usecase.base.SingleUseCase
import javax.inject.Inject

class GetPhotoByIdUseCase @Inject constructor(
    private val photoRepository: PhotoRepository
) : SingleUseCase<Photo, GetPhotoByIdParams> {

    override fun invoke(params: GetPhotoByIdParams): Single<Photo> =
        photoRepository.getPhotoById(params.id)

}

inline class GetPhotoByIdParams(val id: String)