package manuelperera.walkify.presentation.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.rxkotlin.addTo
import manuelperera.walkify.domain.entity.photo.Photo
import manuelperera.walkify.domain.usecase.googleplayservices.CheckGooglePlayServicesUseCase
import manuelperera.walkify.domain.usecase.photo.ClearPhotoDatabaseUseCase
import manuelperera.walkify.domain.usecase.photo.GetPhotoUpdatesUseCase
import manuelperera.walkify.presentation.entity.base.StateResult
import manuelperera.walkify.presentation.ui.base.viewmodel.BaseViewModel
import javax.inject.Inject

class MainViewModel @Inject constructor(
    private val getPhotoUpdatesUseCase: GetPhotoUpdatesUseCase,
    private val clearPhotoDatabaseUseCase: ClearPhotoDatabaseUseCase,
    private val checkGooglePlayServicesUseCase: CheckGooglePlayServicesUseCase
) : BaseViewModel() {

    private val _ldPhotoListResult: MutableLiveData<StateResult<List<Photo>>> = MutableLiveData()
    val ldPhotoListResult: LiveData<StateResult<List<Photo>>> = _ldPhotoListResult

    private var updatesDisposable: Disposable? = null

    init {
        checkGooglePlayServices()
    }

    fun checkGooglePlayServices() {
        checkGooglePlayServicesUseCase(Unit)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({}, { throwable ->
                val failure = getFailure(throwable) { checkGooglePlayServices() }
                _ldPhotoListResult.value = StateResult.Error(failure)
            })
            .addTo(compositeDisposable)
    }

    fun getPhotoUpdates() {
        updatesDisposable?.dispose()

        updatesDisposable = getPhotoUpdatesUseCase(Unit)
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe { _ldPhotoListResult.value = StateResult.Loading() }
            .subscribe({ photoList ->
                _ldPhotoListResult.value = StateResult.HasValues(photoList)
            }, { throwable ->
                val failure = getFailure(throwable) { getPhotoUpdates() }
                _ldPhotoListResult.value = StateResult.Error(failure)
            })
            .addTo(compositeDisposable)
    }

    fun clearDatabase() {
        updatesDisposable?.dispose()

        clearPhotoDatabaseUseCase(Unit)
            .subscribe()
    }

}