package manuelperera.walkify.data.net

import com.google.gson.Gson
import io.reactivex.schedulers.Schedulers
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Inject

class BaseRetrofit @Inject constructor(
    okHttpClient: OkHttpClient,
    gson: Gson
) {

    val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl("https://google.com") // TODO: Add Base URL
        .addConverterFactory(GsonConverterFactory.create(gson))
        .addCallAdapterFactory(RxJava2CallAdapterFactory.createWithScheduler(Schedulers.io()))
        .client(okHttpClient)
        .build()

}