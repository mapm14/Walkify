package manuelperera.walkify.app.di.module.data

import android.content.Context
import com.chuckerteam.chucker.api.ChuckerCollector
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import manuelperera.walkify.data.net.BaseHttpClient
import manuelperera.walkify.data.net.BaseRetrofit
import okhttp3.OkHttpClient
import retrofit2.Retrofit

@Module
class NetModule {

    @Provides
    fun gson(): Gson = GsonBuilder()
        .setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
        .create()

    @Provides
    fun okHttpClient(baseHttpClient: BaseHttpClient): OkHttpClient = baseHttpClient.okHttpClient

    @Provides
    fun retrofit(baseRetrofit: BaseRetrofit): Retrofit = baseRetrofit.retrofit

    @Provides
    fun chuckerCollector(context: Context): ChuckerCollector = ChuckerCollector(context)

}