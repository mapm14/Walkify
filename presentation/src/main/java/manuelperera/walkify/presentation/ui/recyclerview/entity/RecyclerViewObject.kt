package manuelperera.walkify.presentation.ui.recyclerview.entity

interface RecyclerViewObject {

    var id: String
    var itemViewType: ItemViewType

    enum class ItemViewType {
        FULL_ERROR,
        ITEM,
        EMPTY,
        ITEM_PLACEHOLDER
    }

}