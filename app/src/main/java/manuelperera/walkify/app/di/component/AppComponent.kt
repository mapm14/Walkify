package manuelperera.walkify.app.di.component

import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjectionModule
import dagger.android.AndroidInjector
import manuelperera.walkify.app.application.WalkifyApplication
import manuelperera.walkify.app.di.module.data.DataModule
import manuelperera.walkify.app.di.module.presentation.ActivityModule
import manuelperera.walkify.app.di.module.presentation.AppModule
import manuelperera.walkify.app.di.module.presentation.FragmentModule
import manuelperera.walkify.app.di.module.presentation.ViewModelModule
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        AndroidInjectionModule::class,
        AppModule::class,
        ViewModelModule::class,
        ActivityModule::class,
        FragmentModule::class,
        DataModule::class
//        ServiceModule::class // TODO
    ]
)
interface AppComponent : AndroidInjector<WalkifyApplication> {

    @Component.Builder
    interface Builder {

        @BindsInstance
        fun application(app: WalkifyApplication): Builder

        fun build(): AppComponent
    }

}