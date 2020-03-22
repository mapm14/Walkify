package manuelperera.walkify.domain.usecase.photo

import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers
import manuelperera.walkify.domain.entity.photo.Photo
import manuelperera.walkify.domain.repository.PhotoRepository
import manuelperera.walkify.domain.usecase.base.ObservableUseCase
import javax.inject.Inject

class GetPhotoUpdatesUseCase @Inject constructor(
    private val photoRepository: PhotoRepository
) : ObservableUseCase<List<Photo>, Unit> {

    override fun invoke(params: Unit): Observable<List<Photo>> =
        photoRepository.getPhotoUpdates()
            .subscribeOn(Schedulers.io())

}