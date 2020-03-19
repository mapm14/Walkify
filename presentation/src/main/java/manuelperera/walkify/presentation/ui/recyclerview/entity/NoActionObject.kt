package manuelperera.walkify.presentation.ui.recyclerview.entity

private const val NO_ACTION_OBJECT_ID = "NO_ACTION_OBJECT_ID"

data class NoActionObject(
    val viewType: RecyclerViewObject.ItemViewType
) : RecyclerViewObject {

    override var id: String = NO_ACTION_OBJECT_ID
    override var itemViewType = viewType

}