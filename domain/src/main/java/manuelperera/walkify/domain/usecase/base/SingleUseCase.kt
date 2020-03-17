package manuelperera.walkify.domain.usecase.base

import io.reactivex.Single

interface SingleUseCase<Type, in Params> {

    operator fun invoke(params: Params): Single<Type>

}