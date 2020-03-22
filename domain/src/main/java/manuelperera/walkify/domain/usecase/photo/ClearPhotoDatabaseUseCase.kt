package manuelperera.walkify.domain.usecase.photo

import io.reactivex.Completable
import io.reactivex.schedulers.Schedulers
import manuelperera.walkify.domain.repository.PhotoRepository
import manuelperera.walkify.domain.usecase.base.CompletableUseCase
import javax.inject.Inject

class ClearPhotoDatabaseUseCase @Inject constructor(
    private val photoRepository: PhotoRepository
) : CompletableUseCase<Unit> {

    override fun invoke(params: Unit): Completable = photoRepository
            .clearDatabase()
            .subscribeOn(Schedulers.io())

}