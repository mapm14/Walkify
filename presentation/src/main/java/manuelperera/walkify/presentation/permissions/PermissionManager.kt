package manuelperera.walkify.presentation.permissions

import android.annotation.TargetApi
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.content.pm.PackageManager.PERMISSION_GRANTED
import android.net.Uri
import android.os.Build
import android.provider.Settings
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.OnLifecycleEvent
import manuelperera.walkify.presentation.R
import manuelperera.walkify.presentation.extensions.Constants
import manuelperera.walkify.presentation.extensions.safeLet
import java.io.IOException
import java.util.concurrent.atomic.AtomicBoolean
import javax.inject.Inject
import kotlin.math.abs

class PermissionManager @Inject constructor() : FragmentLifecycleObserver,
    ActivityLifecycleObserver {

    private val requestedPermissionHandlers = HashMap<Int, PermissionsHandler>()
    private val pendingResults = HashMap<Int, PermissionResult>()

    private lateinit var permissionRequester: (Array<String>, Int) -> Unit
    private lateinit var hardMandatoryPermissionRequester: () -> Unit
    private lateinit var hardNotMandatoryPermissionRequester: () -> Unit
    private lateinit var permissionChecker: (String) -> Pair<String, StatePermission>
    private lateinit var hardDenyPermissionChecker: (String) -> Pair<String, StatePermission>
    private val isMandatoryPermission = AtomicBoolean()

    @TargetApi(Build.VERSION_CODES.M)
    override fun initWith(owner: Fragment?) {
        permissionRequester = { permissions, code -> owner?.requestPermissions(permissions, code) }

        hardMandatoryPermissionRequester = {
            safeLet(owner, owner?.context) { fragment, context ->
                showMandatoryDialog(context) { intent, requestCode ->
                    fragment.startActivityForResult(intent, requestCode)
                }
            }
        }

        hardNotMandatoryPermissionRequester = {
            safeLet(owner, owner?.context) { fragment, context ->
                showNotMandatoryDialog(context) { intent, requestCode ->
                    fragment.startActivityForResult(intent, requestCode)
                }
            }
        }

        permissionChecker = { permission -> checkPermission(owner?.context, permission) }

        hardDenyPermissionChecker = { permission -> checkHardDenyPermission(owner?.activity, permission) }
    }

    @TargetApi(Build.VERSION_CODES.M)
    override fun initWith(owner: Activity?) {
        permissionRequester = { permissions, code -> owner?.requestPermissions(permissions, code) }

        hardMandatoryPermissionRequester = {
            owner?.let {
                showMandatoryDialog(it) { intent, requestCode ->
                    it.startActivityForResult(intent, requestCode)
                }
            }
        }

        hardNotMandatoryPermissionRequester = {
            owner?.let {
                showNotMandatoryDialog(it) { intent, requestCode ->
                    it.startActivityForResult(intent, requestCode)
                }
            }
        }

        permissionChecker = { permission -> checkPermission(owner, permission) }

        hardDenyPermissionChecker = { permission -> checkHardDenyPermission(owner, permission) }
    }

    private fun showMandatoryDialog(context: Context, startActivity: (Intent, Int) -> Unit) {
        AlertDialog.Builder(context)
            .setTitle(context.getString(R.string.hard_deny_permission))
            .setMessage(context.getString(R.string.hard_deny_permission_message))
            .setPositiveButton(context.getString(R.string.hard_deny_permission_go_to_settings)) { _, _ ->
                val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                val uri = Uri.fromParts("package", context.packageName, null)
                intent.data = uri
                startActivity(intent, Constants.SETTINGS_REQUEST_CODE)
            }
            .setNegativeButton(context.getString(android.R.string.cancel)) { _, _ ->
            }
            .create()
            .show()
    }

    private fun showNotMandatoryDialog(context: Context, startActivity: (Intent, Int) -> Unit) {
        AlertDialog.Builder(context)
            .setTitle(context.getString(R.string.hard_deny_permission_not_mandatory))
            .setMessage(context.getString(R.string.hard_deny_permission_not_mandatory_message))
            .setPositiveButton(context.getString(R.string.hard_deny_permission_go_to_settings)) { _, _ ->
                val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                val uri = Uri.fromParts("package", context.packageName, null)
                intent.data = uri
                startActivity(intent, Constants.SETTINGS_REQUEST_CODE)
            }
            .setNegativeButton(context.getString(android.R.string.cancel)) { _, _ ->
            }
            .create()
            .show()
    }

    @TargetApi(Build.VERSION_CODES.M)
    private fun checkPermission(context: Context?, permission: String): Pair<String, StatePermission> {
        return context?.let {
            permission to when (PERMISSION_GRANTED) {
                it.checkSelfPermission(permission) -> StatePermission.Granted
                else -> StatePermission.SoftDeny
            }
        } ?: permission to StatePermission.SoftDeny
    }

    @TargetApi(Build.VERSION_CODES.M)
    private fun checkHardDenyPermission(activity: Activity?, permission: String): Pair<String, StatePermission> {
        return activity?.let {
            permission to when {
                it.shouldShowRequestPermissionRationale(permission) -> StatePermission.SoftDeny
                else -> StatePermission.HardDeny
            }
        } ?: permission to StatePermission.SoftDeny
    }

    fun isPermissionGranted(context: Context?, permission: String): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            context?.checkSelfPermission(permission) == PERMISSION_GRANTED
        } else {
            true
        }
    }

    fun request(
        permissions: List<String>,
        action: () -> Unit,
        failAction: (List<String>, Boolean) -> Unit,
        isMandatory: Boolean
    ) {
        isMandatoryPermission.set(isMandatory)
        // Used to prevent request permissions multiple times
        // this way will only request the permissions once until the result is emitted
        if (requestedPermissionHandlers.isEmpty()) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                val handler = PermissionsHandler(failAction, action)

                if (permissions.isEmpty()) {
                    throw IOException("Requested permissions must not be empty")
                }

                val notGrantedPermissions = permissions
                    .map(permissionChecker)
                    .filter { it.second == StatePermission.SoftDeny }

                if (notGrantedPermissions.isEmpty()) {
                    action()
                } else {
                    val requestCode = abs(handler.hashCode().toShort().toInt())
                    requestedPermissionHandlers[requestCode] = handler
                    permissionRequester(notGrantedPermissions.map { it.first }.toTypedArray(), requestCode)
                }
            } else {
                action()
            }
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    fun ready() {
        pendingResults.forEach { (_, value) -> value.onPermissionResult() }
        pendingResults.clear()
    }

    fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        if (requestedPermissionHandlers.containsKey(requestCode)) {
            requestedPermissionHandlers.remove(requestCode)?.let { permissionHandler ->
                if (grantResults.any { it == PackageManager.PERMISSION_DENIED }) {
                    val permissionsResults: List<Pair<String, StatePermission>> =
                        permissions.map(hardDenyPermissionChecker)
                    val isHardDenyPermission = permissionsResults.any { it.second == StatePermission.HardDeny }
                    if (isHardDenyPermission && isMandatoryPermission.get()) {
                        hardMandatoryPermissionRequester()
                        return
                    } else if (isHardDenyPermission && isMandatoryPermission.get().not()) {
                        hardNotMandatoryPermissionRequester()
                        return
                    }
                }

                pendingResults[requestCode] = PermissionResult(
                    permissionHandler,
                    permissions,
                    grantResults,
                    isMandatoryPermission.get()
                )
            }
        }
    }

    sealed class StatePermission {
        object Granted : StatePermission()
        object SoftDeny : StatePermission()
        object HardDeny : StatePermission()
    }

    private class PermissionResult internal constructor(
        private val permissionHandler: PermissionsHandler,
        private val permissions: Array<String>,
        private val grantResults: IntArray,
        private val isMandatory: Boolean
    ) {

        fun onPermissionResult() = permissionHandler.onPermissionResult(permissions, grantResults, isMandatory)
    }

}