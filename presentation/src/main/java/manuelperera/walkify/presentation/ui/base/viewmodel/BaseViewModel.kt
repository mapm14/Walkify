package manuelperera.walkify.presentation.ui.base.viewmodel

import android.content.res.Resources
import androidx.lifecycle.ViewModel
import com.chuckerteam.chucker.api.ChuckerCollector
import dagger.Lazy
import io.reactivex.disposables.CompositeDisposable
import manuelperera.walkify.domain.entity.base.Failure
import manuelperera.walkify.presentation.BuildConfig
import manuelperera.walkify.presentation.R
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

    protected fun getFailure(throwable: Throwable, retryAction: () -> Unit): Failure {
        chuckerCollector.get().onError(throwable::class.java.simpleName, throwable)

        val failure = throwable as? Failure ?: kotlin.run {
            val message = if (BuildConfig.BUILD_TYPE == "release" || throwable.message?.isEmpty() == true) {
                resources.get().getString(R.string.unknown_error)
            } else {
                throwable.message ?: resources.get().getString(R.string.unknown_error)
            }
            Failure.Error(message)
        }
        failure.retryAction = retryAction

        return failure
    }

}