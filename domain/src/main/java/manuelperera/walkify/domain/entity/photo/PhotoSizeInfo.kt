package manuelperera.walkify.domain.entity.photo

data class PhotoSizeInfo(
    val label: Label,
    val url: String
) {

    enum class Label {
        SMALL,
        MEDIUM,
        LARGE
    }

}