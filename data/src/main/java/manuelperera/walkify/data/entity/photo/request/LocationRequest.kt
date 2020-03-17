package manuelperera.walkify.data.entity.photo.request

import com.google.gson.annotations.SerializedName

class LocationRequest(
    @SerializedName(value = "latitude") val latitude: Double,
    @SerializedName(value = "longitude") val longitude: Double
)