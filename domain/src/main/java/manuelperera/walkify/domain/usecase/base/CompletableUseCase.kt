package manuelperera.walkify.domain.usecase.base

import io.reactivex.Completable

interface CompletableUseCase<in Params> {

    operator fun invoke(params: Params): Completable

}