package manuelperera.walkify.presentation.pushnotification

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import manuelperera.walkify.presentation.R
import javax.inject.Inject

const val GPS_CHANNEL_ID = "GPS_CHANNEL_ID"
const val LOCATION_SERVICE_CHANNEL_ID = "LOCATION_SERVICE_CHANNEL_ID"

class WalkifyNotificationManager @Inject constructor(
    private val context: Context
) {

    companion object {
        fun getChannelIdByAndroidVersion(channelId: String): String =
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                channelId
            } else {
                ""
            }
    }

    val notificationManager = NotificationManagerCompat.from(context)

    init {
        addNotificationChannel(GPS_CHANNEL_ID, context.getString(R.string.gps_channel_name))
        addNotificationChannel(LOCATION_SERVICE_CHANNEL_ID, context.getString(R.string.location_channel_name))
    }

    private fun addNotificationChannel(id: String, name: String) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val color = ContextCompat.getColor(context, R.color.colorPrimary)
            val channel =
                NotificationChannel(id, name, NotificationManager.IMPORTANCE_HIGH).apply {
                    lightColor = color
                    lockscreenVisibility = Notification.VISIBILITY_PUBLIC
                }
            notificationManager.createNotificationChannel(channel)
        }
    }

}