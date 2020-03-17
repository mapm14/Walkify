package manuelperera.walkify.app.di.module.data

import dagger.Binds
import dagger.Module
import manuelperera.walkify.data.provider.AndroidLocationProviderImpl
import manuelperera.walkify.domain.repository.AndroidLocationProvider
import javax.inject.Singleton

@Module
abstract class ProviderModule {

    @Binds
    @Singleton
    abstract fun location(locationProviderImpl: AndroidLocationProviderImpl): AndroidLocationProvider

}