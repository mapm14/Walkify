package manuelperera.walkify.data.extensions

import io.reactivex.Single
import retrofit2.Response
import retrofit2.adapter.rxjava2.Result

fun <T> getSingleResultSuccess(value: T): Single<Result<T>> =
    Single.just(Result.response(Response.success(value)))