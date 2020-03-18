package manuelperera.walkify.presentation.ui.recyclerview.entity

class RetryObject(
    val text: String,
    val onRetryClick: () -> Unit,
    viewType: RecyclerViewObject.ItemViewType
) : RecyclerViewObject {

    override var id: String = this::class.java.simpleName
    override var itemViewType = viewType

}