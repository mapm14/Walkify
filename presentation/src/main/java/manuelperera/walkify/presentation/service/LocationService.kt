package manuelperera.walkify.presentation.service

import android.app.Notification
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Build
import android.os.IBinder
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import dagger.android.DaggerService
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import manuelperera.walkify.domain.usecase.photo.GetPhotoUpdatesByLocationUseCase
import manuelperera.walkify.domain.usecase.photo.GetPhotosUpdatesByLocationParams
import manuelperera.walkify.presentation.R
import manuelperera.walkify.presentation.broadcastreceiver.GpsLocationSettingReceiver
import manuelperera.walkify.presentation.extensions.Constants.LOCATION_SERVICE_NOTIFICATION_ID
import manuelperera.walkify.presentation.pushnotification.LOCATION_SERVICE_CHANNEL_ID
import manuelperera.walkify.presentation.pushnotification.WalkifyNotificationManager
import manuelperera.walkify.presentation.ui.main.MainActivity
import timber.log.Timber
import javax.inject.Inject

private const val SMALLEST_DISPLACEMENT_IN_METERS = 100F

class LocationService : DaggerService() {

    @Inject
    lateinit var gpsLocationSettingReceiver: GpsLocationSettingReceiver

    @Inject
    lateinit var getPhotoUpdatesByLocationUseCase: GetPhotoUpdatesByLocationUseCase

    @Inject
    lateinit var notificationManagerCompat: NotificationManagerCompat

    @Inject
    lateinit var context: Context

    override fun onBind(intent: Intent?): IBinder? = null

    private val compositeDisposable = CompositeDisposable()

    override fun onCreate() {
        super.onCreate()
        startForeground(LOCATION_SERVICE_NOTIFICATION_ID, createActiveNotification())
        startLocationUpdates()
        gpsLocationSettingReceiver.register(context)
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        super.onStartCommand(intent, flags, startId)
//        showDriverStatusNotification() //TODO
        return Service.START_STICKY
    }

    override fun onDestroy() {
        stopService()
        super.onDestroy()
    }

    private fun stopService() {
        gpsLocationSettingReceiver.unregister(context)
        notificationManagerCompat.cancel(LOCATION_SERVICE_NOTIFICATION_ID)
        stopForeground(true)
        stopSelf()
        compositeDisposable.dispose()
    }

    private fun createActiveNotification(): Notification {
        val contentIntent = PendingIntent.getActivity(
            this,
            0,
            MainActivity.getIntent(this),
            PendingIntent.FLAG_UPDATE_CURRENT
        )
        val priority = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            NotificationCompat.PRIORITY_MAX
        } else {
            NotificationManagerCompat.IMPORTANCE_HIGH
        }
        val channelId =
            WalkifyNotificationManager.getChannelIdByAndroidVersion(LOCATION_SERVICE_CHANNEL_ID)

        val builder = NotificationCompat.Builder(this, channelId)
            .setContentTitle(getString(R.string.recording))
            .setContentText(getString(R.string.enjoy_your_walk))
            .setTicker(getString(R.string.recording))
            .setSmallIcon(R.drawable.ic_walk)
            .setLargeIcon(BitmapFactory.decodeResource(context.resources, R.drawable.ic_walk))
            .setContentIntent(contentIntent)
            .setPriority(priority)
            .setDefaults(Notification.DEFAULT_SOUND)
            .setCategory(NotificationCompat.CATEGORY_SERVICE)
            .setAutoCancel(true)

        return builder.build()
    }

    private fun startLocationUpdates() {
        val params = GetPhotosUpdatesByLocationParams(SMALLEST_DISPLACEMENT_IN_METERS)
        getPhotoUpdatesByLocationUseCase(params)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({}, ::handleFailure) // TODO: Check
            .addTo(compositeDisposable)
    }

    private fun handleFailure(throwable: Throwable) {
        Timber.e(throwable)
//        showDriverStatusNotification() // TODO
    }

}