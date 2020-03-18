package manuelperera.walkify.presentation.ui.main

import android.view.View
import manuelperera.walkify.presentation.R
import manuelperera.walkify.presentation.databinding.ItemPhotoBinding
import manuelperera.walkify.presentation.extensions.load
import manuelperera.walkify.presentation.ui.recyclerview.adapter.NotPagingAdapter
import manuelperera.walkify.presentation.ui.recyclerview.entity.RecyclerViewObject

class PhotoAdapter : NotPagingAdapter<PhotoAdapter.PhotoUI>() {

    override var itemLayout: Int = R.layout.item_photo

    override var onBindItem: (View, PhotoUI) -> Unit = { itemView, photo ->
        val binding = ItemPhotoBinding.bind(itemView)
        binding.photoView.load(photo.url)
    }

    fun addPhotos(urlList: List<String>) {
        addItemsToList(urlList.map { PhotoUI(it) })
    }

    inner class PhotoUI(val url: String) : RecyclerViewObject {

        override var id: String = url
        override var itemViewType: RecyclerViewObject.ItemViewType = RecyclerViewObject.ItemViewType.ITEM

    }

}