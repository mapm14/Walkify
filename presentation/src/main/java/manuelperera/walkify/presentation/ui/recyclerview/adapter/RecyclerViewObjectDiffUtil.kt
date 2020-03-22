package manuelperera.walkify.presentation.ui.recyclerview.adapter

import androidx.recyclerview.widget.DiffUtil
import manuelperera.walkify.presentation.ui.recyclerview.entity.RecyclerViewObject

class RecyclerViewObjectDiffUtil(
    private val oldList: List<RecyclerViewObject>,
    private val newList: List<RecyclerViewObject>
) : DiffUtil.Callback() {

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int) =
        oldList[oldItemPosition] === newList[newItemPosition]

    override fun getOldListSize() = oldList.size

    override fun getNewListSize() = newList.size

    override fun areContentsTheSame(oldPosition: Int, newPosition: Int): Boolean {
        val id1 = oldList[oldPosition].id
        val id2 = newList[newPosition].id

        return id1 == id2
    }

}