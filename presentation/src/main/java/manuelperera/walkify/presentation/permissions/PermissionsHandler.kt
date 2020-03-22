package manuelperera.walkify.presentation.permissions

import android.content.pm.PackageManager

class PermissionsHandler(
    private val failAction: (deniedPermissions: List<String>, isMandatory: Boolean) -> Unit,
    private val action: () -> Unit
) {

    fun onPermissionResult(permissions: Array<String>, grantResults: IntArray, isMandatory: Boolean) {
        val permissionsDenied = mutableListOf<String>()

        for (i in grantResults.indices) {
            if (grantResults[i] == PackageManager.PERMISSION_DENIED) {
                permissionsDenied.add(permissions[i])
            }
        }

        if (permissionsDenied.isNotEmpty()) {
            failAction(permissionsDenied, isMandatory)
        } else {
            action()
        }
    }

}