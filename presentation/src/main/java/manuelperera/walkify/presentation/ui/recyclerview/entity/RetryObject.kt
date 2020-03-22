package manuelperera.walkify.presentation.ui.recyclerview.entity

private const val RETRY_OBJECT_ID = "RETRY_OBJECT_ID"

data class RetryObject(
    val text: String,
    val onRetryClick: () -> Unit,
    val viewType: RecyclerViewObject.ItemViewType
) : RecyclerViewObject {

    override var id: String = RETRY_OBJECT_ID
    override var itemViewType = viewType

}