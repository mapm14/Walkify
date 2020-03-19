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

private const val GPS_CHANNEL_ID = "GPS_CHANNEL_ID"
private const val GPS_CHANNEL_NAME = "GPS_CHANNEL_NAME"

class WalkifyNotificationManager @Inject constructor(
    private val context: Context
) {

    companion object {
        fun getGpsChannelId(): String = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            GPS_CHANNEL_ID
        } else {
            ""
        }
    }

    val notificationManager = NotificationManagerCompat.from(context)

    init {
        addGpsNotificationChannel()
    }

    private fun addGpsNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val color = ContextCompat.getColor(context, R.color.colorPrimary)
            val channel =
                NotificationChannel(GPS_CHANNEL_ID, GPS_CHANNEL_NAME, NotificationManager.IMPORTANCE_HIGH).apply {
                    lightColor = color
                    lockscreenVisibility = Notification.VISIBILITY_PUBLIC
                }
            notificationManager.createNotificationChannel(channel)
        }
    }

}