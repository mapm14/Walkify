package manuelperera.walkify.app.di.module.data

import dagger.Binds
import dagger.Module
import manuelperera.walkify.data.provider.AndroidLocationProviderImpl
import manuelperera.walkify.data.provider.GooglePlayServicesHandlerImpl
import manuelperera.walkify.domain.provider.AndroidLocationProvider
import manuelperera.walkify.domain.provider.GooglePlayServicesHandler
import javax.inject.Singleton

@Module
abstract class ProviderModule {

    @Binds
    @Singleton
    abstract fun location(locationProviderImpl: AndroidLocationProviderImpl): AndroidLocationProvider

    @Binds
    abstract fun googlePlay(googlePlayServicesHandlerImpl: GooglePlayServicesHandlerImpl): GooglePlayServicesHandler

}