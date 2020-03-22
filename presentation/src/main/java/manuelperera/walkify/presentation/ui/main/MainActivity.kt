package manuelperera.walkify.presentation.ui.main

import android.Manifest.permission.ACCESS_FINE_LOCATION
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import manuelperera.walkify.presentation.R
import manuelperera.walkify.presentation.databinding.ActivityMainBinding
import manuelperera.walkify.presentation.entity.base.StateResult
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
        if (isLocationServiceRunning()) checkOrRequestPermissions()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.main_activity_menu, menu)
        return true
    }

    override fun onPrepareOptionsMenu(menu: Menu): Boolean {
        val item: MenuItem = menu.findItem(R.id.walk)
        item.title = if (isLocationServiceRunning()) getString(R.string.stop_walk) else getString(R.string.start_walk)
        return super.onPrepareOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return if (item.itemId == R.id.walk) {
            if (isLocationServiceRunning()) stopWalk() else startWalk()
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
            checkOrRequestPermissions()
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
        invalidateOptionsMenu()
    }

    private fun setupViewModel() {
        mainViewModel = viewModel(viewModelFactory.get()) {
            observe(ldPhotoListResult) { result ->
                when (result) {
                    is StateResult.HasValues -> photoAdapter.addPhotos(result.value)
                    is StateResult.Loading -> photoAdapter.addLoadingPlaceholder(4)
                    is StateResult.Error -> {
                        val message = result.failure.getMessage(getResources())
                        photoAdapter.addError(message, result.failure.retryAction)
                    }
                }
            }
        }
    }

    override fun checkOrRequestPermissions() {
        checkOrRequestAndRun(
            permissions = listOf(ACCESS_FINE_LOCATION),
            action = this::startLocationService,
            failAction = { _, _ ->
                // Check denied permissions and if they are mandatory
                Toast.makeText(this, getString(R.string.please_enable_permissions), Toast.LENGTH_LONG).show()
            },
            isMandatory = true
        )
    }

    private fun startLocationService() {
        if (isLocationServiceRunning().not()) {
            val intent = Intent(this, LocationService::class.java)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                applicationContext.startForegroundService(intent)
            } else {
                applicationContext.startService(intent)
            }
            invalidateOptionsMenu()
        }

        mainViewModel.getPhotoUpdates()
    }

    private fun isLocationServiceRunning(): Boolean = applicationContext.isServiceRunning(LocationService::class.java)

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == GPS_REQUEST_CODE && isGpsOn()) {
            checkOrRequestPermissions()
        } else {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }

}