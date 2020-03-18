package manuelperera.walkify.presentation.ui.base.activity

import android.content.Intent
import android.os.Bundle
import androidx.annotation.LayoutRes
import androidx.lifecycle.ViewModelProvider
import dagger.Lazy
import dagger.android.support.DaggerAppCompatActivity
import manuelperera.walkify.presentation.extensions.Constants
import manuelperera.walkify.presentation.permissions.PermissionManager
import manuelperera.walkify.presentation.permissions.addObserver
import javax.inject.Inject

abstract class BaseActivity : DaggerAppCompatActivity() {

    @Inject
    lateinit var viewModelFactory: Lazy<ViewModelProvider.Factory>

    @Inject
    lateinit var permissionManager: Lazy<PermissionManager>

    @setparam:LayoutRes
    abstract var activityLayout: Int

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(activityLayout)
    }

    //region Permissions
    protected open fun requestPermissions() {}

    protected fun requestAndRun(
        permissions: List<String>,
        action: () -> Unit,
        failAction: (List<String>, Boolean) -> Unit = { _, _ -> },
        isMandatory: Boolean = false
    ) {
        lifecycle.addObserver(permissionManager.get(), this)
        permissionManager.get().request(permissions, action, failAction, isMandatory)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        permissionManager.get().onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == Constants.SETTINGS_REQUEST_CODE) {
            requestPermissions()
        } else {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }
    //endregion

}