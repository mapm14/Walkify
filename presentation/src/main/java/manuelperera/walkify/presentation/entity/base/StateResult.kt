package manuelperera.walkify.presentation.entity.base

import manuelperera.walkify.domain.entity.base.Failure

sealed class StateResult<T> {

    class Loading<T> : StateResult<T>()
    class HasValues<T>(val value: T) : StateResult<T>()
    class Error<T>(val failure: Failure) : StateResult<T>()

}