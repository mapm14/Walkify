package manuelperera.walkify.app.di.module.presentation

import androidx.lifecycle.ViewModelProvider
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap
import manuelperera.walkify.app.di.viewmodel.ViewModelFactory
import manuelperera.walkify.app.di.viewmodel.ViewModelKey
import manuelperera.walkify.presentation.ui.base.viewmodel.BaseViewModel
import manuelperera.walkify.presentation.ui.main.MainViewModel

@Module
abstract class ViewModelModule {

    @Binds
    abstract fun factory(factory: ViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(MainViewModel::class)
    abstract fun main(vm: MainViewModel): BaseViewModel

}
