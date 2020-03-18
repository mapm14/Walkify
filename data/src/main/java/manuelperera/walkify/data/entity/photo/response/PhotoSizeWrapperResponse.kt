package manuelperera.walkify.data.entity.photo.response

import com.google.gson.annotations.SerializedName
import manuelperera.walkify.data.entity.base.ResponseObject
import manuelperera.walkify.data.extensions.toEnum
import manuelperera.walkify.domain.entity.photo.PhotoSizeInfo

class PhotoSizeWrapperResponse(
    @SerializedName("sizes") val photoSizeResponse: PhotoSizeResponse
) : ResponseObject<List<PhotoSizeInfo>> {

    inner class PhotoSizeResponse(
        @SerializedName("size") val photoSizeInfoResponseList: List<PhotoSizeInfoResponse>
    ) {

        inner class PhotoSizeInfoResponse(
            @SerializedName("label") val label: String,
            @SerializedName("source") val url: String
        )

    }

    override fun toDomain(): List<PhotoSizeInfo> =
        photoSizeResponse.photoSizeInfoResponseList.mapNotNull { photoSizeInfoResponse ->
            try {
                val label = photoSizeInfoResponse.label.toEnum<PhotoSizeInfo.Label>()
                PhotoSizeInfo(label, photoSizeInfoResponse.url)
            } catch (e: Exception) {
                null
            }
        }

}