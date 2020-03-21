package manuelperera.walkify.presentation.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.rxkotlin.addTo
import manuelperera.walkify.domain.entity.photo.Photo
import manuelperera.walkify.domain.usecase.photo.ClearPhotoDatabaseUseCase
import manuelperera.walkify.domain.usecase.photo.GetPhotoUpdatesUseCase
import manuelperera.walkify.presentation.ui.base.viewmodel.BaseViewModel
import javax.inject.Inject

class MainViewModel @Inject constructor(
    private val getPhotoUpdatesUseCase: GetPhotoUpdatesUseCase,
    private val clearPhotoDatabaseUseCase: ClearPhotoDatabaseUseCase
) : BaseViewModel() {

    private val _ldPhotoList: MutableLiveData<List<Photo>> = MutableLiveData()
    val ldPhotoList: LiveData<List<Photo>> = _ldPhotoList

    private var updatesDisposable: Disposable? = null

    fun getPhotoUpdates() {
        updatesDisposable?.dispose()

        updatesDisposable = getPhotoUpdatesUseCase(Unit)
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe { loading() }
            .subscribe({ urlList ->
                _ldPhotoList.value = urlList
            }, { throwable ->
                handleFailure(throwable) { getPhotoUpdates() }
            })
            .addTo(compositeDisposable)
    }

    fun clearDatabase() {
        updatesDisposable?.dispose()

        clearPhotoDatabaseUseCase(Unit)
            .subscribe()
    }

}