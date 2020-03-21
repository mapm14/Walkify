package manuelperera.walkify.data.datasource.remote

import android.content.res.Resources
import androidx.annotation.VisibleForTesting
import com.chuckerteam.chucker.api.ChuckerCollector
import com.google.gson.Gson
import dagger.Lazy
import io.reactivex.Single
import manuelperera.walkify.data.BuildConfig
import manuelperera.walkify.data.R
import manuelperera.walkify.data.entity.base.ApiCodes
import manuelperera.walkify.data.entity.base.DataObject
import manuelperera.walkify.data.entity.base.ErrorResponse
import manuelperera.walkify.domain.entity.base.Failure
import manuelperera.walkify.domain.entity.base.Success
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.adapter.rxjava2.Result
import java.net.ConnectException
import java.net.UnknownHostException
import java.util.concurrent.TimeUnit
import javax.inject.Inject

open class BaseRemoteDataSource {

    @VisibleForTesting(otherwise = VisibleForTesting.PROTECTED)
    @Inject
    lateinit var resources: Lazy<Resources>

    @VisibleForTesting(otherwise = VisibleForTesting.PROTECTED)
    @Inject
    lateinit var gson: Lazy<Gson>

    @VisibleForTesting(otherwise = VisibleForTesting.PROTECTED)
    @Inject
    lateinit var chuckerCollector: Lazy<ChuckerCollector>

    private val timeout = 30L
    private val retry = 1

    @VisibleForTesting(otherwise = VisibleForTesting.PROTECTED)
    fun modifySingleSuccess(
        single: Single<Result<ResponseBody>>,
        timeoutTime: Long = timeout,
        retryTimes: Int = retry
    ): Single<Success> {
        return applyOperations(single, timeoutTime, retryTimes) { response ->
            getDomainObjectNoResponse<Success>(response.code())
        }
    }

    @VisibleForTesting(otherwise = VisibleForTesting.PROTECTED)
    fun <Data : DataObject<Domain>, Domain : Any> modifySingle(
        single: Single<Result<Data>>,
        timeoutTime: Long = timeout,
        retryTimes: Int = retry
    ): Single<Domain> {
        return applyOperations(single, timeoutTime, retryTimes) { response ->
            val body: Data? = response.body()
            if (body != null) {
                getDomainObject(body)
            } else {
                getDomainObjectNoResponse(response.code())
            }
        }
    }

    @VisibleForTesting(otherwise = VisibleForTesting.PROTECTED)
    fun <Data : DataObject<Domain>, Domain : Any> modifySingleList(
        single: Single<Result<List<Data>>>,
        timeoutTime: Long = timeout,
        retryTimes: Int = retry
    ): Single<List<Domain>> {
        return applyOperations(single, timeoutTime, retryTimes) { response ->
            val body: List<Data>? = response.body()
            if (body != null) {
                getDomainObjectList(body)
            } else {
                getDomainObjectNoResponse(response.code())
            }
        }
    }

    private fun <T : Any, R : Any> applyOperations(
        single: Single<Result<T>>,
        timeoutTime: Long = timeout,
        retryTimes: Int = retry,
        returnResult: (Response<T>) -> R
    ): Single<R> {
        return single
            .onErrorResumeNext {
                Single.error(getFailureUnknownError())
            }
            .retry { count, throwable ->
                count <= retryTimes && (throwable is Failure.Timeout || throwable is Failure.NoInternet)
            }
            .map { data ->
                data.response()?.let { response ->
                    val code = response.code()
                    val errorBody = response.errorBody()

                    when {
                        response.isSuccessful -> returnResult(response)
                        code == ApiCodes.UNAUTHORIZED_CODE -> throw Failure.Unauthorized
                        response.isSuccessful.not() -> throw getFailureErrorWithErrorResponse(code, errorBody)
                        else -> throw getFailureUnknownError()
                    }

                } ?: throw getFailureError(data.error())
            }
            .timeout(timeoutTime, TimeUnit.SECONDS, Single.create { emitter ->
                emitter.tryOnError(getFailureTimeout())
            })
    }

    @Suppress("UNCHECKED_CAST")
    private fun <RO : DataObject<DO>, DO : Any> getDomainObject(body: RO): DO =
        (body as DataObject<Any>).toDomain() as DO

    @Suppress("UNCHECKED_CAST")
    private fun <RO : DataObject<DO>, DO : Any> getDomainObjectList(body: List<RO>): List<DO> =
        (body as List<DataObject<Any>>).map { it.toDomain() } as List<DO>

    @Suppress("UNCHECKED_CAST")
    private fun <DO : Any> getDomainObjectNoResponse(code: Int): DO =
        Success(code) as DO

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    fun getFailureErrorWithErrorResponse(code: Int, errorBody: ResponseBody?): Failure.Error {
        val errorResponse = parseErrorResponse(code, errorBody)
        val message = if (errorResponse.message.isNotEmpty()) {
            errorResponse.message
        } else {
            resources.get().getString(R.string.unknown_error)
        }
        return Failure.Error(code, message)
    }

    private fun getFailureError(throwable: Throwable?): Failure {
        return when (throwable) {
            is ConnectException,
            is UnknownHostException -> Failure.NoInternet(resources.get().getString(R.string.no_internet))
            else -> {
                @Suppress("ConstantConditionIf") val message = if (BuildConfig.BUILD_TYPE == "release") {
                    resources.get().getString(R.string.unknown_error)
                } else {
                    throwable?.message ?: resources.get().getString(R.string.unknown_error)
                }
                Failure.Error(message)
            }
        }
    }

    private fun getFailureUnknownError(): Failure.Error =
        Failure.Error(resources.get().getString(R.string.unknown_error))

    private fun getFailureTimeout(): Failure.Timeout =
        Failure.Timeout(resources.get().getString(R.string.timeout_message))

    private fun parseErrorResponse(code: Int, responseBody: ResponseBody?): ErrorResponse {
        return try {
            gson.get().fromJson(responseBody!!.string(), ErrorResponse::class.java)
        } catch (exception: Exception) {
            chuckerCollector.get().onError(exception::class.java.simpleName, exception)
            ErrorResponse(
                code = code,
                message = resources.get().getString(R.string.unknown_error)
            )
        }
    }

}