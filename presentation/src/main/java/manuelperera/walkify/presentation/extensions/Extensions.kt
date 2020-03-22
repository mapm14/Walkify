package manuelperera.walkify.presentation.extensions

import android.app.Activity
import android.app.ActivityManager
import android.content.Context
import android.location.LocationManager
import androidx.appcompat.app.AlertDialog
import com.google.android.gms.common.GoogleApiAvailability
import manuelperera.walkify.domain.entity.base.Failure
import manuelperera.walkify.presentation.R

fun <T1 : Any, T2 : Any, R : Any> safeLet(p1: T1?, p2: T2?, block: (T1, T2) -> R?): R? =
    if (p1 != null && p2 != null) block(p1, p2) else null

fun Context.isGpsOn(): Boolean {
    val locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
    return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) ||
            locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
}

@Suppress("DEPRECATION")
fun <T> Context.isServiceRunning(service: Class<T>): Boolean {
    return (getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager)
        .getRunningServices(Integer.MAX_VALUE)
        .any { it.service.className == service.name }
}

fun Activity.showResolvableGooglePlayServiceError(failure: Failure.ResolvableGooglePlayServicesError) {
    val googleApiAvailability = GoogleApiAvailability.getInstance()
    googleApiAvailability.showErrorDialogFragment(
        this,
        failure.resultCode,
        Constants.GOOGLE_API_AVAILABILITY_REQUEST_CODE
    ) { finishAndRemoveTask() }
}

fun Activity.showNotResolvableGooglePlayServicesError() {
    AlertDialog.Builder(this)
        .setTitle(getString(R.string.google_play_services_required))
        .setMessage(getString(R.string.google_play_services_required_message))
        .setPositiveButton(getString(R.string.okay)) { _, _ ->
            finishAndRemoveTask()
        }
        .setCancelable(false)
        .create()
        .show()
}