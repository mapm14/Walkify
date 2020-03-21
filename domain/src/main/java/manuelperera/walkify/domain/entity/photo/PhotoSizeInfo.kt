package manuelperera.walkify.domain.entity.photo

data class Photo(
    val id: String,
    val sizeList: List<PhotoSizeInfo>
) {

    companion object {
        fun empty(): Photo = Photo("", listOf())
    }

    fun getUrlOrFirstResult(label: PhotoSizeInfo.Label) : String {
        // If sizeList is empty is an API error
        return (sizeList.firstOrNull { it.label == label } ?: sizeList.first()).url
    }

    fun isEmpty() : Boolean {
        return id.isEmpty() && sizeList.isEmpty()
    }

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

}