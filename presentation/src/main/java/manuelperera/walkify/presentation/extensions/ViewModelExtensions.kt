package manuelperera.walkify.presentation.extensions

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.*
import manuelperera.walkify.presentation.ui.base.viewmodel.BaseViewModel

inline fun <reified T : BaseViewModel> FragmentActivity.viewModel(
    factory: ViewModelProvider.Factory,
    body: T.() -> Unit
): T {
    val vm = ViewModelProvider(this, factory).get(T::class.java)
    vm.body()
    return vm
}

inline fun <reified T : BaseViewModel> Fragment.viewModel(
    factory: ViewModelProvider.Factory,
    body: T.() -> Unit = {}
): T {
    val vm = ViewModelProvider(this, factory).get(T::class.java)
    vm.body()
    return vm
}

inline fun <reified T : BaseViewModel> Fragment.viewModelFromActivity(
    factory: ViewModelProvider.Factory,
    body: T.() -> Unit
): T? {
    return activity?.let {
        val vm = ViewModelProvider(it, factory).get(T::class.java)
        vm.body()
        vm
    }
}

fun <T> LifecycleOwner.observe(liveData: LiveData<T>, action: (t: T) -> Unit) {
    liveData.observe(this, Observer { it?.let { t -> action(t) } })
}