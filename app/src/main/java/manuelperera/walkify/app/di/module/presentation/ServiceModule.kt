package manuelperera.walkify.app.di.module.presentation

import dagger.Module
import dagger.android.ContributesAndroidInjector
import manuelperera.walkify.presentation.service.LocationService

@Module
abstract class ServiceModule {

    @ContributesAndroidInjector
    abstract fun location(): LocationService

}