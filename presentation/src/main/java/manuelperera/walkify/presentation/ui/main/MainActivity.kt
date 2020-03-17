package manuelperera.walkify.presentation.ui.main

import android.Manifest.permission.ACCESS_FINE_LOCATION
import android.os.Bundle
import android.widget.Toast
import manuelperera.walkify.presentation.R
import manuelperera.walkify.presentation.extensions.observe
import manuelperera.walkify.presentation.extensions.viewModel
import manuelperera.walkify.presentation.ui.base.activity.BaseActivity

private const val SMALLEST_DISPLACEMENT_IN_METERS = 100F

class MainActivity : BaseActivity() {

    override var activityLayout: Int = R.layout.activity_main

    lateinit var mainViewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupViewModel()
        requestPermissions()
    }

    private fun setupViewModel() {
        mainViewModel = viewModel(viewModelFactory.get()) {
            observe(ldUrl) { url ->
                Toast.makeText(applicationContext, url, Toast.LENGTH_LONG).show()
            }
        }
    }

    override fun requestPermissions() {
        requestAndRun(
            permissions = listOf(ACCESS_FINE_LOCATION),
            action = { mainViewModel.getPhotoByLocation(SMALLEST_DISPLACEMENT_IN_METERS) },
            isMandatory = true
        )
    }

}