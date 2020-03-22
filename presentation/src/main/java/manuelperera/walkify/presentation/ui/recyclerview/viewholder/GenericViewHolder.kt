package manuelperera.walkify.presentation.ui.recyclerview.viewholder

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import manuelperera.walkify.presentation.R
import manuelperera.walkify.presentation.extensions.safeLet
import manuelperera.walkify.presentation.ui.recyclerview.entity.RecyclerViewObject
import manuelperera.walkify.presentation.ui.recyclerview.entity.RetryObject

abstract class GenericViewHolder<in T>(view: View) : RecyclerView.ViewHolder(view) {

    abstract fun bind(item: RecyclerViewObject)

}

class NoActionViewHolder<in T>(val view: View) : GenericViewHolder<T>(view) {

    override fun bind(item: RecyclerViewObject) {}

}

class ErrorViewHolder<in T>(private val view: View) : GenericViewHolder<T>(view) {

    override fun bind(item: RecyclerViewObject) {
        val retryButton = view.findViewById<View?>(R.id.retryBtn)
        val textView = view.findViewById<TextView?>(R.id.retryTxv)
        val retryObject = item as? RetryObject
        safeLet(retryButton, retryObject) { button, retry ->
            button.setOnClickListener { retry.onRetryClick() }
        }

        safeLet(textView, retryObject) { txtView, retry ->
            txtView.text = retry.text
        }
    }

}

class ItemViewHolder<T : RecyclerViewObject>(itemView: View, val onBindItem: (View, T) -> Unit) :
    GenericViewHolder<T>(itemView) {

    @Suppress("UNCHECKED_CAST")
    override fun bind(item: RecyclerViewObject) {
        onBindItem(itemView, item as T)
    }

}