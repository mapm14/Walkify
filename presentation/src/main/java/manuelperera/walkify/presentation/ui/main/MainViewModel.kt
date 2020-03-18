package manuelperera.walkify.presentation.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.addTo
import manuelperera.walkify.domain.entity.photo.PhotoSizeInfo
import manuelperera.walkify.domain.usecase.photo.GetPhotoUrlsUpdatesByLocationUseCase
import manuelperera.walkify.domain.usecase.photo.GetPhotosUrlsUpdatesByLocationParams
import manuelperera.walkify.presentation.ui.base.viewmodel.BaseViewModel
import javax.inject.Inject

class MainViewModel @Inject constructor(
    private val getPhotoUrlsUpdatesByLocationUseCase: GetPhotoUrlsUpdatesByLocationUseCase
) : BaseViewModel() {

    private val _ldUrlList: MutableLiveData<MutableList<String>> = MutableLiveData()
    val ldUrlList: LiveData<MutableList<String>> = _ldUrlList

    fun getPhotoByLocation(smallestDisplacementInMeters: Float, selectedLabel: PhotoSizeInfo.Label) {
        val params = GetPhotosUrlsUpdatesByLocationParams(smallestDisplacementInMeters, selectedLabel)

        getPhotoUrlsUpdatesByLocationUseCase(params)
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe { loading() }
            .subscribe(this::addUrlToList)
            { throwable ->
                handleFailure(throwable) { getPhotoByLocation(smallestDisplacementInMeters, selectedLabel) }
            }
            .addTo(compositeDisposable)
    }

    private fun addUrlToList(photoUrl: String) {
        _ldUrlList.value?.let { mutable ->
            mutable.add(0, photoUrl)
            _ldUrlList.value = mutable
        } ?: kotlin.run {
            _ldUrlList.value = mutableListOf(photoUrl)
        }
    }

}