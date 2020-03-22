package manuelperera.walkify.app.di.module.data

import dagger.Binds
import dagger.Module
import manuelperera.walkify.data.repository.photo.PhotoRepositoryImpl
import manuelperera.walkify.domain.repository.PhotoRepository

@Module
abstract class RepositoryModule {

    @Binds
    abstract fun photo(photoRepositoryImpl: PhotoRepositoryImpl): PhotoRepository

}