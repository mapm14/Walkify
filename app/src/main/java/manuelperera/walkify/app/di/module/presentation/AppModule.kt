package manuelperera.walkify.app.di.module.presentation

import android.app.Application
import android.content.Context
import android.content.res.Resources
import androidx.core.app.NotificationManagerCompat
import androidx.room.Room
import com.google.android.gms.location.FusedLocationProviderClient
import dagger.Module
import dagger.Provides
import io.reactivex.Observable
import manuelperera.walkify.app.application.WalkifyApplication
import manuelperera.walkify.data.datasource.local.AppDatabase
import manuelperera.walkify.presentation.broadcastreceiver.GpsLocationSettingReceiver
import manuelperera.walkify.presentation.extensions.Constants
import javax.inject.Named
import javax.inject.Singleton

@Module
class AppModule {

    @Provides
    @Singleton
    fun provideApplication(application: WalkifyApplication): Application = application

    @Provides
    @Named(Constants.IS_APP_IN_FOREGROUND)
    fun provideAppIsInForegroundObservable(application: WalkifyApplication): Observable<Boolean> =
        application.appIsInForegroundBehaviorSubject

    @Provides
    fun provideContext(application: Application): Context = application

    @Provides
    fun resources(application: Application): Resources = application.resources

    @Provides
    fun fusedLocationProvider(context: Context): FusedLocationProviderClient =
        FusedLocationProviderClient(context)

    @Provides
    fun gpsLocationSettingReceiver(notificationManagerCompat: NotificationManagerCompat): GpsLocationSettingReceiver =
        GpsLocationSettingReceiver(notificationManagerCompat)

    @Provides
    @Singleton
    fun database(context: Context): AppDatabase = Room.databaseBuilder(
        context,
        AppDatabase::class.java,
        "WALKIFY_DATABASE"
    ).build()

}