package manuelperera.walkify.domain.entity.photo

data class Photo(
    val id: String,
    val sizeList: List<SizeInfo>
) {

    companion object {
        fun empty(): Photo = Photo("", listOf())
    }

    fun getUrlOrFirstResult(label: SizeInfo.Label) : String {
        // If sizeList is empty is an API error
        return (sizeList.firstOrNull { it.label == label } ?: sizeList.first()).url
    }

    fun isEmpty() : Boolean {
        return id.isEmpty() && sizeList.isEmpty()
    }

    data class SizeInfo(
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