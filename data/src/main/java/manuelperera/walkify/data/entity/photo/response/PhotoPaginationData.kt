package manuelperera.walkify.data.entity.photo.response

import com.google.gson.annotations.SerializedName
import manuelperera.walkify.data.entity.base.DataObject
import manuelperera.walkify.domain.entity.base.Failure

class PhotoPaginationData(
    @SerializedName("photos") val photoInfo: PhotoInfoResponse
) : DataObject<String> {

    class PhotoInfoResponse(
        @SerializedName("photo") val photoList: List<PhotoIdResponse>
    ) {

        class PhotoIdResponse(
            @SerializedName("id") val id: String
        )

    }

    override fun toDomain(): String = photoInfo.photoList.firstOrNull()?.id ?: throw Failure.NotFound

}