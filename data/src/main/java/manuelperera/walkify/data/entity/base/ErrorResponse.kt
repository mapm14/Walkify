package manuelperera.walkify.data.entity.base

import com.google.gson.annotations.SerializedName

class ErrorResponse(
    @SerializedName(value = "code") val code: Int,
    @SerializedName(value = "message") val message: String
)