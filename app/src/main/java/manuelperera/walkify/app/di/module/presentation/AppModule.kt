package manuelperera.walkify.app.di.module.presentation

import android.app.Application
import android.content.Context
import android.content.res.Resources
import android.location.Geocoder
import com.google.android.gms.location.FusedLocationProviderClient
import dagger.Module
import dagger.Provides
import io.reactivex.Observable
import manuelperera.walkify.app.application.WalkifyApplication
import manuelperera.walkify.presentation.extensions.Constants
import java.util.*
import javax.inject.Named
import javax.inject.Singleton

@Module
class AppModule {

    @Provides
    @Singleton
    fun provideApplication(application: WalkifyApplication): Application = application

    @Provides
    @Singleton
    @Named(Constants.IS_APP_IN_FOREGROUND)
    fun provideAppIsInForegroundObservable(application: WalkifyApplication): Observable<Boolean> =
        application.appIsInForegroundBehaviorSubject

    @Provides
    @Singleton
    fun provideContext(application: Application): Context = application

    @Provides
    @Singleton
    fun resources(application: Application): Resources = application.resources

    @Provides
    fun fusedLocationProvider(context: Context): FusedLocationProviderClient =
        FusedLocationProviderClient(context)

}