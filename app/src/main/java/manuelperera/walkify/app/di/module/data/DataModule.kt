package manuelperera.walkify.app.di.module.data

import dagger.Module

@Module(
    includes = [
        NetModule::class,
        RepositoryModule::class,
        ProviderModule::class,
        ApiModule::class
    ]
)
class DataModule