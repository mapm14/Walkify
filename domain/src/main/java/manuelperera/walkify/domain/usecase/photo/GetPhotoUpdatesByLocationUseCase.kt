package manuelperera.walkify.domain.usecase.photo

import io.reactivex.Observable
import io.reactivex.Single
import manuelperera.walkify.domain.entity.base.Failure
import manuelperera.walkify.domain.entity.photo.Photo
import manuelperera.walkify.domain.usecase.base.ObservableUseCase
import manuelperera.walkify.domain.usecase.location.GetLocationRefreshesParams
import manuelperera.walkify.domain.usecase.location.GetLocationRefreshesUseCase
import javax.inject.Inject

class GetPhotoUpdatesByLocationUseCase @Inject constructor(
    private val getLocationRefreshesUseCase: GetLocationRefreshesUseCase,
    private val getPhotoByLocationUseCase: GetPhotoByLocationUseCase
) : ObservableUseCase<Photo, GetPhotosUpdatesByLocationParams> {

    override fun invoke(params: GetPhotosUpdatesByLocationParams): Observable<Photo> =
        getLocationRefreshesUseCase(GetLocationRefreshesParams(params.smallestDisplacementInMeters))
            .flatMapSingle { gpsLocation ->
                val getPhotoByLocationParams = GetPhotoByLocationParams(
                    latitude = gpsLocation.latitude,
                    longitude = gpsLocation.longitude
                )
                getPhotoByLocationUseCase(getPhotoByLocationParams)
                    .onErrorResumeNext { throwable ->
                        if (throwable is Failure.NotFound || throwable is Failure.NoInternet || throwable is Failure.Timeout) {
                            Single.just(Photo.empty())
                        } else {
                            Single.error(throwable)
                        }
                    }
            }

}

inline class GetPhotosUpdatesByLocationParams(val smallestDisplacementInMeters: Float)