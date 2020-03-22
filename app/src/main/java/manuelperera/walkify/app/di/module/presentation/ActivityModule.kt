package manuelperera.walkify.app.di.module.presentation

import dagger.Module
import dagger.android.ContributesAndroidInjector
import manuelperera.walkify.app.di.scope.PerView
import manuelperera.walkify.presentation.ui.main.MainActivity

@Module
abstract class ActivityModule {

    @PerView
    @ContributesAndroidInjector
    abstract fun main(): MainActivity

}