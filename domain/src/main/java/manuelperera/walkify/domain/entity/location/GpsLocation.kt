package manuelperera.walkify.domain.entity.location

data class GpsLocation(
    val latitude: Double,
    val longitude: Double,
    val isLastKnownLocation: Boolean,
    val accuracy: Float
)