package manuelperera.walkify.presentation.ui.base.viewmodel

import android.content.res.Resources
import androidx.lifecycle.ViewModel
import com.chuckerteam.chucker.api.ChuckerCollector
import dagger.Lazy
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject

abstract class BaseViewModel : ViewModel() {

    @Inject
    lateinit var resources: Lazy<Resources>

    @Inject
    lateinit var chuckerCollector: Lazy<ChuckerCollector>

    protected val compositeDisposable = CompositeDisposable()

    override fun onCleared() {
        compositeDisposable.dispose()
        super.onCleared()
    }

    protected fun recordError(throwable: Throwable) {
        chuckerCollector.get().onError(throwable::class.java.simpleName, throwable)
    }

}