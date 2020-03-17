package manuelperera.walkify.presentation.ui.base.activity

import android.content.Intent
import android.os.Bundle
import androidx.annotation.LayoutRes
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.snackbar.Snackbar
import dagger.Lazy
import dagger.android.support.DaggerAppCompatActivity
import io.reactivex.disposables.CompositeDisposable
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

    protected val compositeDisposable: CompositeDisposable by lazy {
        CompositeDisposable()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(activityLayout)
    }

    override fun onDestroy() {
        compositeDisposable.dispose()
        super.onDestroy()
    }

    fun showSnackbar(
        title: String,
        action: String,
        length: Int = Snackbar.LENGTH_INDEFINITE,
        actionResult: () -> Unit = {}
    ) {
        // TODO: Check extension
//        val container = findViewById<View?>(R.id.parentContainer)
//        container?.snackbar(title, action, length, actionResult = actionResult)
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