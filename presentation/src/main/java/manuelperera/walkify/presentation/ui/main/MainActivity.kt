package manuelperera.walkify.presentation.ui.main

import android.Manifest.permission.ACCESS_FINE_LOCATION
import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import androidx.appcompat.app.AlertDialog
import manuelperera.walkify.domain.entity.photo.PhotoSizeInfo
import manuelperera.walkify.presentation.R
import manuelperera.walkify.presentation.broadcastreceiver.GpsLocationSettingReceiver
import manuelperera.walkify.presentation.databinding.ActivityMainBinding
import manuelperera.walkify.presentation.extensions.Constants.GPS_REQUEST_CODE
import manuelperera.walkify.presentation.extensions.isGpsOn
import manuelperera.walkify.presentation.extensions.observe
import manuelperera.walkify.presentation.extensions.viewModel
import manuelperera.walkify.presentation.ui.base.activity.BaseActivity
import javax.inject.Inject

private const val SMALLEST_DISPLACEMENT_IN_METERS = 100F
private val SELECTED_LABEL = PhotoSizeInfo.Label.MEDIUM

class MainActivity : BaseActivity() {

    override var activityLayout: Int = R.layout.activity_main

    private lateinit var mainViewModel: MainViewModel
    private val photoAdapter: PhotoAdapter by lazy { PhotoAdapter() }

    @Inject
    lateinit var gpsLocationSettingReceiver: GpsLocationSettingReceiver

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupUI(binding)
        setupViewModel()
        startWalk()
    }

    override fun onDestroy() {
        gpsLocationSettingReceiver.unregister(this)
        super.onDestroy()
    }

    private fun setupUI(binding: ActivityMainBinding) {
        binding.recyclerView.adapter = photoAdapter
    }

    private fun startWalk() {
        if (isGpsOn()) {
            gpsLocationSettingReceiver.register(this)
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
        gpsLocationSettingReceiver.unregister(this)
    }

    private fun setupViewModel() {
        mainViewModel = viewModel(viewModelFactory.get()) {
            observe(ldLoading) {
                photoAdapter.addLoadingPlaceholder(4)
            }

            observe(ldUrlList, photoAdapter::addPhotos)

            observe(ldFailure) { failure ->
                val message = failure.getMessage(getResources())
                photoAdapter.addError(message, failure.retryAction)
            }
        }
    }

    override fun requestPermissions() {
        requestAndRun(
            permissions = listOf(ACCESS_FINE_LOCATION),
            action = { mainViewModel.getPhotoByLocation(SMALLEST_DISPLACEMENT_IN_METERS, SELECTED_LABEL) },
            isMandatory = true
        )
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == GPS_REQUEST_CODE && isGpsOn()) {
            startWalk()
        } else {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }

}