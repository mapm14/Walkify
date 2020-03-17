package manuelperera.walkify.data.entity.photo.response

import com.google.gson.annotations.SerializedName
import manuelperera.walkify.data.entity.base.ResponseObject

class PhotoResponse(
    @SerializedName("url") val url: String
) : ResponseObject<String> {

    override fun toDomain(): String = url

}