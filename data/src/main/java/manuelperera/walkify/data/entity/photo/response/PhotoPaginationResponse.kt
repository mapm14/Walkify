package manuelperera.walkify.data.entity.photo.response

import com.google.gson.annotations.SerializedName
import manuelperera.walkify.data.entity.base.ResponseObject

class PhotoPaginationResponse(
    @SerializedName("photos") val photoInfo: PhotoInfoResponse
) : ResponseObject<String> {

    inner class PhotoInfoResponse(
        @SerializedName("photo") val photoList: List<PhotoIdResponse>
    ) {

        inner class PhotoIdResponse(
            @SerializedName("id") val id: String
        )

    }

    // TODO: Check what to do in case of NoSuchElementException
    override fun toDomain(): String = photoInfo.photoList.firstOrNull()?.id ?: ""

}