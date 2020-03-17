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
    chuckerCollector: ChuckerCollector,
    context: Context
) {

    private val chuckerInterceptor = ChuckerInterceptor(
        context = context,
        collector = chuckerCollector
    )

    @Suppress("ConstantConditionIf")
    val okHttpClient: OkHttpClient = OkHttpClient()
        .newBuilder()
        .addInterceptor(
            HttpLoggingInterceptor().apply {
                level = if (BuildConfig.DEBUG) BODY else NONE
            }
        )
        .addInterceptor(chuckerInterceptor)
        .connectTimeout(TIMEOUT, TimeUnit.SECONDS)
        .readTimeout(TIMEOUT, TimeUnit.SECONDS)
        .writeTimeout(TIMEOUT, TimeUnit.SECONDS)
        .build()

}