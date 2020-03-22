package manuelperera.walkify.app.di.module.data

import dagger.Module
import dagger.Provides
import manuelperera.walkify.data.repository.photo.data.PhotoApi
import retrofit2.Retrofit

@Module
class ApiModule {

    @Provides
    fun photo(retrofit: Retrofit): PhotoApi =
        retrofit.create(PhotoApi::class.java)

}