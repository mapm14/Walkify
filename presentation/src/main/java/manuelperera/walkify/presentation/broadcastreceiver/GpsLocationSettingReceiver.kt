package manuelperera.walkify.presentation.broadcastreceiver

import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.BitmapFactory
import android.location.LocationManager
import android.provider.Settings
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import manuelperera.walkify.presentation.R
import manuelperera.walkify.presentation.extensions.Constants.TURN_ON_GPS_NOTIFICATION_ID
import manuelperera.walkify.presentation.extensions.isGpsOn
import manuelperera.walkify.presentation.pushnotification.GPS_CHANNEL_ID
import manuelperera.walkify.presentation.pushnotification.WalkifyNotificationManager
import timber.log.Timber
import javax.inject.Inject



class GpsLocationSettingReceiver @Inject constructor(
    private val notificationManager: NotificationManagerCompat
) : BroadcastReceiver() {

    fun register(context: Context) {
        try {
            context.registerReceiver(this, IntentFilter(LocationManager.PROVIDERS_CHANGED_ACTION))
        } catch (e: Exception) {
            Timber.e(e, "Register error")
        }
    }

    fun unregister(context: Context) {
        try {
            context.unregisterReceiver(this)
        } catch (e: Exception) {
            Timber.e(e, "Unregister error")
        }
    }

    override fun onReceive(context: Context?, intent: Intent?) {
        context?.let { safeContext ->
            if (safeContext.isGpsOn()) {
                notificationManager.cancel(TURN_ON_GPS_NOTIFICATION_ID)
            } else {
                createNotificationForEnableGPS(context)
            }
        }
    }

    private fun createNotificationForEnableGPS(context: Context) {
        val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        val gpsSettingsPendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)

        val incomingRequestNotification =
            NotificationCompat.Builder(context, WalkifyNotificationManager.getChannelIdByAndroidVersion(GPS_CHANNEL_ID))
                .setSmallIcon(R.drawable.ic_retry)
                .setLargeIcon(BitmapFactory.decodeResource(context.resources, R.drawable.ic_retry))
                .setContentTitle(context.getString(R.string.enable_gps))
                .setContentText(context.getString(R.string.please_enable_gps_to_resume_walk_track))
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setCategory(NotificationCompat.CATEGORY_SERVICE)
                .setContentIntent(gpsSettingsPendingIntent)
                .build()

        notificationManager.notify(TURN_ON_GPS_NOTIFICATION_ID, incomingRequestNotification)
    }

}