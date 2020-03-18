package manuelperera.walkify.presentation.ui.recyclerview.adapter

import android.content.res.Resources
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.facebook.shimmer.ShimmerFrameLayout
import manuelperera.walkify.presentation.R
import manuelperera.walkify.presentation.extensions.inflate
import manuelperera.walkify.presentation.ui.recyclerview.entity.NoActionObject
import manuelperera.walkify.presentation.ui.recyclerview.entity.RecyclerViewObject
import manuelperera.walkify.presentation.ui.recyclerview.entity.RecyclerViewObject.ItemViewType.*
import manuelperera.walkify.presentation.ui.recyclerview.entity.RetryObject
import manuelperera.walkify.presentation.ui.recyclerview.viewholder.ErrorViewHolder
import manuelperera.walkify.presentation.ui.recyclerview.viewholder.GenericViewHolder
import manuelperera.walkify.presentation.ui.recyclerview.viewholder.ItemViewHolder
import manuelperera.walkify.presentation.ui.recyclerview.viewholder.NoActionViewHolder

abstract class NotPagingAdapter<T : RecyclerViewObject> : RecyclerView.Adapter<GenericViewHolder<T>>() {

    protected abstract var onBindItem: (View, T) -> Unit
    protected abstract var itemLayout: Int

    protected open var itemFullErrorLayout = R.layout.item_generic_full_error
    protected open var itemEmptyLayout = R.layout.item_generic_empty
    protected open var itemPlaceholderLayout = R.layout.item_generic_placeholder

    private var list = mutableListOf<RecyclerViewObject>()

    private var recyclerView: RecyclerView? = null
    protected var resources: Resources? = null

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        resources = recyclerView.resources
        this.recyclerView = recyclerView
    }

    fun clear() {
        addItems(listOf())
    }

    protected fun addItemsToList(itemList: List<RecyclerViewObject>) {
        if (itemList.isEmpty()) {
            addItems(listOf(NoActionObject(EMPTY)))
        } else {
            addItems(itemList)
        }
    }

    fun addLoadingPlaceholder(size: Int) {
        val mutableList = mutableListOf<RecyclerViewObject>()
        for (i in 0 until size) {
            mutableList.add(NoActionObject(ITEM_PLACEHOLDER))
        }
        addItems(mutableList)
    }

    fun addError(msg: String, onRetryClick: () -> Unit) {
        val onRetry = {
            clear()
            onRetryClick()
        }
        val retryObject = RetryObject(msg, onRetry, FULL_ERROR)
        addItems(listOf(retryObject))
    }

    private fun addItems(newList: List<RecyclerViewObject>) {
        val diffCallback = RecyclerViewObjectDiffUtil(list, newList)
        val diffResult = DiffUtil.calculateDiff(diffCallback)
        list.clear()
        list.addAll(newList)
        diffResult.dispatchUpdatesTo(this)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GenericViewHolder<T> =
        when (viewType) {
            FULL_ERROR.ordinal -> ErrorViewHolder(parent.inflate(itemFullErrorLayout))
            EMPTY.ordinal -> NoActionViewHolder(parent.inflate(itemEmptyLayout))
            ITEM.ordinal -> ItemViewHolder(parent.inflate(itemLayout), onBindItem)
            ITEM_PLACEHOLDER.ordinal -> NoActionViewHolder(wrapWithShimmer(parent.inflate(itemPlaceholderLayout)))
            else -> ErrorViewHolder(parent.inflate(itemFullErrorLayout))
        }

    // Wrap the view to inflate in a ShimmerFrameLayout for been able to show Shimmer effect
    private fun wrapWithShimmer(itemView: View): View {
        val shimmerFrameLayout = ShimmerFrameLayout(itemView.context).apply {
            val layoutManager = recyclerView?.layoutManager
            val width = if (layoutManager is LinearLayoutManager) {
                if (layoutManager.orientation == LinearLayoutManager.HORIZONTAL) {
                    FrameLayout.LayoutParams.WRAP_CONTENT
                } else {
                    FrameLayout.LayoutParams.MATCH_PARENT
                }
            } else {
                FrameLayout.LayoutParams.MATCH_PARENT
            }

            layoutParams = FrameLayout.LayoutParams(width, FrameLayout.LayoutParams.WRAP_CONTENT)

            addView(itemView)
        }

        shimmerFrameLayout.startShimmer()
        return shimmerFrameLayout
    }

    override fun getItemCount() = list.size

    override fun onBindViewHolder(holder: GenericViewHolder<T>, position: Int) {
        holder.bind(list[position])
    }

    override fun getItemViewType(position: Int) = list[position].itemViewType.ordinal

}