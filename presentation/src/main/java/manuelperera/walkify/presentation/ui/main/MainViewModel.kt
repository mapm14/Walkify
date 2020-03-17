package manuelperera.walkify.presentation.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.addTo
import manuelperera.walkify.domain.usecase.photo.GetPhotoByLocationParams
import manuelperera.walkify.domain.usecase.photo.GetPhotoByLocationUseCase
import manuelperera.walkify.presentation.ui.base.viewmodel.BaseViewModel
import javax.inject.Inject

class MainViewModel @Inject constructor(
    private val getPhotoByLocationUseCase: GetPhotoByLocationUseCase
) : BaseViewModel() {

    private val _ldUrl: MutableLiveData<String> = MutableLiveData()
    val ldUrl: LiveData<String> = _ldUrl

    fun getPhotoByLocation(smallestDisplacementInMeters: Float) {
        getPhotoByLocationUseCase(GetPhotoByLocationParams(smallestDisplacementInMeters))
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe {

            }
            .subscribe({ url ->
                _ldUrl.value = url
            }, { throwable ->
                recordError(throwable)
            })
            .addTo(compositeDisposable)
    }

}