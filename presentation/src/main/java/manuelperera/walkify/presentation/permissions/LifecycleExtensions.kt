package manuelperera.walkify.presentation.permissions

import android.app.Activity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver

interface FragmentLifecycleObserver : LifecycleObserver {
    fun initWith(owner: Fragment?)
}

interface ActivityLifecycleObserver : LifecycleObserver {
    fun initWith(owner: Activity?)
}

fun Lifecycle.addObserver(observer: FragmentLifecycleObserver, fragment: Fragment) {
    addObserver(observer as LifecycleObserver)
    observer.initWith(fragment)
}

fun Lifecycle.addObserver(observer: ActivityLifecycleObserver, activity: Activity) {
    addObserver(observer as LifecycleObserver)
    observer.initWith(activity)
}