package manuelperera.walkify.presentation.ui.main

import android.Manifest.permission.ACCESS_FINE_LOCATION
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AlertDialog
import manuelperera.walkify.presentation.R
import manuelperera.walkify.presentation.databinding.ActivityMainBinding
import manuelperera.walkify.presentation.extensions.Constants.GPS_REQUEST_CODE
import manuelperera.walkify.presentation.extensions.isGpsOn
import manuelperera.walkify.presentation.extensions.isServiceRunning
import manuelperera.walkify.presentation.extensions.observe
import manuelperera.walkify.presentation.extensions.viewModel
import manuelperera.walkify.presentation.service.LocationService
import manuelperera.walkify.presentation.ui.base.activity.BaseActivity

class MainActivity : BaseActivity() {

    companion object {
        fun getIntent(context: Context) = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
    }

    override var activityLayout: Int = R.layout.activity_main

    private lateinit var mainViewModel: MainViewModel
    private val photoAdapter: PhotoAdapter by lazy { PhotoAdapter() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupUI(binding)
        setupViewModel()
        if (applicationContext.isServiceRunning(LocationService::class.java)) requestPermissions()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.main_activity_menu, menu)
        return true
    }

    override fun onPrepareOptionsMenu(menu: Menu): Boolean {
        val item: MenuItem = menu.findItem(R.id.walk)
        if (applicationContext.isServiceRunning(LocationService::class.java)) {
            item.title = getString(R.string.stop_walk)
        } else {
            item.title = getString(R.string.start_walk)
        }
        return super.onPrepareOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return if (item.itemId == R.id.walk) {
            if (applicationContext.isServiceRunning(LocationService::class.java)) {
                stopWalk()
            } else {
                startWalk()
            }
            invalidateOptionsMenu()
            true
        } else {
            super.onOptionsItemSelected(item)
        }
    }

    private fun setupUI(binding: ActivityMainBinding) {
        binding.recyclerView.adapter = photoAdapter
    }

    private fun startWalk() {
        if (isGpsOn()) {
            requestPermissions()
        } else {
            AlertDialog.Builder(this)
                .setTitle(getString(R.string.gps_needed))
                .setMessage(getString(R.string.enable_gps_services_to_record_walk))
                .setNegativeButton(getString(android.R.string.cancel)) { _, _ -> }
                .setPositiveButton(getString(R.string.activate)) { _, _ ->
                    val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                    startActivityForResult(intent, GPS_REQUEST_CODE)
                }
                .create()
                .show()
        }
    }

    private fun stopWalk() {
        applicationContext.stopService(Intent(this, LocationService::class.java))
        mainViewModel.clearDatabase()
    }

    private fun setupViewModel() {
        mainViewModel = viewModel(viewModelFactory.get()) {
            observe(ldLoading) {
                photoAdapter.addLoadingPlaceholder(4)
            }

            observe(ldPhotoList, photoAdapter::addPhotos)

            observe(ldFailure) { failure ->
                val message = failure.getMessage(getResources())
                photoAdapter.addError(message, failure.retryAction)
            }
        }
    }

    override fun requestPermissions() {
        requestAndRun(
            permissions = listOf(ACCESS_FINE_LOCATION),
            action = this::startLocationService,
            isMandatory = true
        )
    }

    private fun startLocationService() {
        if (applicationContext.isServiceRunning(LocationService::class.java).not()) {
            val intent = Intent(this, LocationService::class.java)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                applicationContext.startForegroundService(intent)
            } else {
                applicationContext.startService(intent)
            }
        }

        mainViewModel.getPhotoUpdates()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == GPS_REQUEST_CODE && isGpsOn()) {
            requestPermissions()
        } else {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }

}