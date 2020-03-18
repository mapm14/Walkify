package manuelperera.walkify.presentation.ui.main

import android.Manifest.permission.ACCESS_FINE_LOCATION
import android.os.Bundle
import manuelperera.walkify.domain.entity.photo.PhotoSizeInfo
import manuelperera.walkify.presentation.R
import manuelperera.walkify.presentation.databinding.ActivityMainBinding
import manuelperera.walkify.presentation.extensions.observe
import manuelperera.walkify.presentation.extensions.viewModel
import manuelperera.walkify.presentation.ui.base.activity.BaseActivity

private const val SMALLEST_DISPLACEMENT_IN_METERS = 100F
private val SELECTED_LABEL = PhotoSizeInfo.Label.MEDIUM

class MainActivity : BaseActivity() {

    override var activityLayout: Int = R.layout.activity_main

    private lateinit var mainViewModel: MainViewModel
    private val photoAdapter: PhotoAdapter by lazy { PhotoAdapter() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupUI(binding)
        setupViewModel()
        requestPermissions()
    }

    private fun setupUI(binding: ActivityMainBinding) {
        binding.recyclerView.adapter = photoAdapter
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

}