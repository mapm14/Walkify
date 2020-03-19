package manuelperera.walkify.app.di.module.presentation

import androidx.core.app.NotificationManagerCompat
import dagger.Module
import dagger.Provides
import manuelperera.walkify.presentation.pushnotification.WalkifyNotificationManager

@Module
class PushNotificationModule {

    @Provides
    fun provideNotificationManager(walkifyNotificationManager: WalkifyNotificationManager): NotificationManagerCompat =
        walkifyNotificationManager.notificationManager

}