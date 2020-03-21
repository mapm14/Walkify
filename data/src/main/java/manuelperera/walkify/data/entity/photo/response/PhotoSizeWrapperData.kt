package manuelperera.walkify.data.entity.photo.response

import com.google.gson.annotations.SerializedName
import manuelperera.walkify.data.entity.base.DataObject
import manuelperera.walkify.data.extensions.toEnum
import manuelperera.walkify.domain.entity.photo.Photo

class PhotoSizeWrapperData(
    @SerializedName("sizes") val photoSizeResponse: PhotoSizeResponse
) : DataObject<List<Photo.SizeInfo>> {

    inner class PhotoSizeResponse(
        @SerializedName("size") val photoSizeInfoResponseList: List<PhotoSizeInfoResponse>
    ) {

        inner class PhotoSizeInfoResponse(
            @SerializedName("label") val label: String,
            @SerializedName("source") val url: String
        )

    }

    override fun toDomain(): List<Photo.SizeInfo> =
        photoSizeResponse.photoSizeInfoResponseList.mapNotNull { photoSizeInfoResponse ->
            try {
                val label = photoSizeInfoResponse.label.toEnum<Photo.SizeInfo.Label>()
                Photo.SizeInfo(label, photoSizeInfoResponse.url)
            } catch (e: Exception) {
                null
            }
        }

}