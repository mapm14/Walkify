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
import javax.inject.Singleton

@Module
class NetModule {

    @Provides
    @Singleton
    fun gson(): Gson = GsonBuilder()
        .setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
        .create()

    @Provides
    @Singleton
    fun okHttpClient(baseHttpClient: BaseHttpClient): OkHttpClient = baseHttpClient.okHttpClient

    @Provides
    @Singleton
    fun retrofit(baseRetrofit: BaseRetrofit): Retrofit = baseRetrofit.retrofit

    @Provides
    @Singleton
    fun chuckerCollector(context: Context): ChuckerCollector = ChuckerCollector(context = context)

}