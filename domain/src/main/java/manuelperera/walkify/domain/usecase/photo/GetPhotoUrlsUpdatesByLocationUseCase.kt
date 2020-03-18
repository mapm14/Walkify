package manuelperera.walkify.domain.usecase.photo

import io.reactivex.Observable
import manuelperera.walkify.domain.entity.photo.PhotoSizeInfo
import manuelperera.walkify.domain.usecase.base.ObservableUseCase
import manuelperera.walkify.domain.usecase.location.GetLocationRefreshesParams
import manuelperera.walkify.domain.usecase.location.GetLocationRefreshesUseCase
import javax.inject.Inject

class GetPhotoUrlsUpdatesByLocationUseCase @Inject constructor(
    private val getLocationRefreshesUseCase: GetLocationRefreshesUseCase,
    private val getPhotoUrlByLocationUseCase: GetPhotoUrlByLocationUseCase
) : ObservableUseCase<String, GetPhotosUrlsUpdatesByLocationParams> {

    override fun invoke(params: GetPhotosUrlsUpdatesByLocationParams): Observable<String> =
        getLocationRefreshesUseCase(GetLocationRefreshesParams(params.smallestDisplacementInMeters))
            .flatMapSingle { gpsLocation ->
                val getPhotoParams = GetPhotoUrlByLocationParams(
                    latitude = gpsLocation.latitude,
                    longitude = gpsLocation.longitude,
                    selectedLabel = params.selectedLabel
                )
                getPhotoUrlByLocationUseCase(getPhotoParams)
            }

}

class GetPhotosUrlsUpdatesByLocationParams(
    val smallestDisplacementInMeters: Float,
    val selectedLabel: PhotoSizeInfo.Label
)