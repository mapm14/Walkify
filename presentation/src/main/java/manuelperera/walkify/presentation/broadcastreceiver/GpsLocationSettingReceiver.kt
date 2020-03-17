package manuelperera.walkify.presentation.broadcastreceiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.location.LocationManager
import manuelperera.walkify.presentation.extensions.safeLet
import timber.log.Timber

class GpsLocationSettingReceiver : BroadcastReceiver() {

    private var isRegistered: Boolean = false

    fun register(context: Context) {
        if (!isRegistered) {
            try {
                context.unregisterReceiver(this)
                context.registerReceiver(this, IntentFilter(LocationManager.PROVIDERS_CHANGED_ACTION))
                isRegistered = true
            } catch (e: Exception) {
                Timber.e(e)
            }
        }
    }

    fun unregister(context: Context) {
        if (isRegistered) {
            try {
                context.unregisterReceiver(this)
                isRegistered = false
            } catch (e: Exception) {
                Timber.e(e)
            }
        }
    }

    override fun onReceive(context: Context?, intent: Intent?) {
        safeLet(context, intent) { safeContext, _ ->
            if (isGpsOn(safeContext).not()) {
                // TODO: Launch Activity to request permissions again
//                safeContext.startActivity(GpsRequestActivity.getIntent(context))
            }
        }
    }

    private fun isGpsOn(context: Context): Boolean {
        val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) ||
                locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
    }

}