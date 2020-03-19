package manuelperera.walkify.data.net

import android.content.Context
import com.chuckerteam.chucker.BuildConfig
import com.chuckerteam.chucker.api.ChuckerCollector
import com.chuckerteam.chucker.api.ChuckerInterceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import okhttp3.logging.HttpLoggingInterceptor.Level.BODY
import okhttp3.logging.HttpLoggingInterceptor.Level.NONE
import java.util.concurrent.TimeUnit
import javax.inject.Inject

private const val TIMEOUT = 30L

class BaseHttpClient @Inject constructor(
    private val chuckerCollector: ChuckerCollector,
    private val context: Context,
    tokenInterceptor: TokenInterceptor
) {

    private fun getChuckerInterceptor() = ChuckerInterceptor(
        context = context,
        collector = chuckerCollector
    )

    @Suppress("ConstantConditionIf")
    private fun getHttpLoggingInterceptor() = HttpLoggingInterceptor().apply {
        level = if (BuildConfig.DEBUG) BODY else NONE
    }

    val okHttpClient: OkHttpClient = OkHttpClient()
        .newBuilder()
        .addInterceptor(tokenInterceptor)
        .addInterceptor(getHttpLoggingInterceptor())
        .addInterceptor(getChuckerInterceptor())
        .connectTimeout(TIMEOUT, TimeUnit.SECONDS)
        .readTimeout(TIMEOUT, TimeUnit.SECONDS)
        .writeTimeout(TIMEOUT, TimeUnit.SECONDS)
        .build()

}