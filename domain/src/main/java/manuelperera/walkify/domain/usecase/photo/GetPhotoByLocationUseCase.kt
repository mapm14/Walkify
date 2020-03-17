package manuelperera.walkify.domain.usecase.photo

import io.reactivex.Observable
import manuelperera.walkify.domain.repository.PhotoRepository
import manuelperera.walkify.domain.usecase.base.ObservableUseCase
import manuelperera.walkify.domain.usecase.location.GetLocationRefreshesParams
import manuelperera.walkify.domain.usecase.location.GetLocationRefreshesUseCase
import javax.inject.Inject

class GetPhotoByLocationUseCase @Inject constructor(
    private val photoRepository: PhotoRepository,
    private val getLocationRefreshesUseCase: GetLocationRefreshesUseCase
) : ObservableUseCase<String, GetPhotoByLocationParams> {

    override fun invoke(params: GetPhotoByLocationParams): Observable<String> =
        getLocationRefreshesUseCase(GetLocationRefreshesParams(params.smallestDisplacementInMeters))
            .flatMapSingle { gpsLocation ->
                photoRepository.getPhotoByLocation(gpsLocation.latitude, gpsLocation.longitude)
            }

}

inline class GetPhotoByLocationParams(val smallestDisplacementInMeters: Float)