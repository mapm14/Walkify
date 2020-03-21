package manuelperera.walkify.data.entity.photo.response

import com.google.gson.annotations.SerializedName
import manuelperera.walkify.data.entity.base.ResponseObject
import manuelperera.walkify.domain.entity.base.Failure

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

    override fun toDomain(): String = photoInfo.photoList.firstOrNull()?.id ?: throw Failure.NotFound

}