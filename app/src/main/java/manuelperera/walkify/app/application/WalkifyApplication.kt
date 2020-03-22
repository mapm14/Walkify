package manuelperera.walkify.app.application

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import androidx.lifecycle.ProcessLifecycleOwner
import dagger.android.AndroidInjector
import dagger.android.support.DaggerApplication
import io.reactivex.exceptions.UndeliverableException
import io.reactivex.functions.Consumer
import io.reactivex.plugins.RxJavaPlugins
import io.reactivex.subjects.BehaviorSubject
import manuelperera.walkify.app.di.component.DaggerAppComponent
import timber.log.Timber

class WalkifyApplication : DaggerApplication(), LifecycleObserver {

    val appIsInForegroundBehaviorSubject: BehaviorSubject<Boolean> = BehaviorSubject.create()
    private val rxErrorHandler: Consumer<Throwable> by lazy {
        RxJavaErrorHandler()
    }

    override fun applicationInjector(): AndroidInjector<out DaggerApplication> =
        DaggerAppComponent.builder().application(this).build()

    override fun onCreate() {
        super.onCreate()
        ProcessLifecycleOwner.get().lifecycle.addObserver(this)
        Timber.plant(Timber.DebugTree())
        RxJavaPlugins.setErrorHandler(rxErrorHandler)
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    fun onAppForegrounded() {
        appIsInForegroundBehaviorSubject.onNext(true)
        Timber.d("App in foreground")
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    fun onAppBackgrounded() {
        appIsInForegroundBehaviorSubject.onNext(false)
        Timber.d("App in background")
    }

    private class RxJavaErrorHandler : Consumer<Throwable> {

        override fun accept(throwable: Throwable) {
            when (throwable) {
                is UndeliverableException -> accept(throwable.cause!!) //Unwrap the exception to rerun again through the same consumer
                is KotlinNullPointerException, is NullPointerException, is IllegalArgumentException, is IllegalStateException -> {
                    Thread.currentThread().run {
                        //There is likely a bug in the application
                        uncaughtExceptionHandler?.uncaughtException(this, throwable)
                    }
                }
            }
        }

    }

}