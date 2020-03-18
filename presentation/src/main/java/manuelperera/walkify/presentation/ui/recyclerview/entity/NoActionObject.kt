package manuelperera.walkify.presentation.ui.recyclerview.entity

class NoActionObject(
    viewType: RecyclerViewObject.ItemViewType
) : RecyclerViewObject {

    override var id: String = this::class.java.simpleName
    override var itemViewType = viewType

}