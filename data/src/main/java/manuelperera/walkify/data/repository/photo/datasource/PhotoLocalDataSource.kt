package manuelperera.walkify.data.repository.photo.datasource

import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.subjects.BehaviorSubject
import manuelperera.walkify.domain.entity.photo.Photo
import javax.inject.Inject
import javax.inject.Singleton

@Singleton // TODO: Remove
class PhotoLocalDataSource @Inject constructor() {

    private var photoSizeInfoBehaviorSubject: BehaviorSubject<List<Photo>> = BehaviorSubject.create()

    fun getPhotoSizeInfoUpdates(): Observable<List<Photo>> {
        return photoSizeInfoBehaviorSubject
    }

    fun setPhotoSizeInfoUpdates(photo: Photo) {
        photoSizeInfoBehaviorSubject.value?.let { list ->
            val mutable = list.toMutableList()
            mutable.add(0, photo)
            photoSizeInfoBehaviorSubject.onNext(mutable)
        } ?: kotlin.run {
            photoSizeInfoBehaviorSubject.onNext(listOf(photo))
        }
    }

    fun clearDatabase(): Completable = Completable.fromAction {
        photoSizeInfoBehaviorSubject = BehaviorSubject.create()
    }

}