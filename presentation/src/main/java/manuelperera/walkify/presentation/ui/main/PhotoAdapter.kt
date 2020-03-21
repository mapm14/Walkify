package manuelperera.walkify.presentation.ui.main

import android.view.View
import manuelperera.walkify.domain.entity.photo.Photo
import manuelperera.walkify.presentation.R
import manuelperera.walkify.presentation.databinding.ItemPhotoBinding
import manuelperera.walkify.presentation.extensions.load
import manuelperera.walkify.presentation.ui.recyclerview.adapter.NotPagingAdapter
import manuelperera.walkify.presentation.ui.recyclerview.entity.RecyclerViewObject

private val SELECTED_LABEL = Photo.PhotoSizeInfo.Label.MEDIUM

class PhotoAdapter : NotPagingAdapter<PhotoAdapter.PhotoUI>() {

    override var itemLayout: Int = R.layout.item_photo

    override var onBindItem: (View, PhotoUI) -> Unit = { itemView, photo ->
        val binding = ItemPhotoBinding.bind(itemView)
        if (photo.url.isNotEmpty()) binding.photoView.load(photo.url)
    }

    fun addPhotos(photoList: List<Photo>) {
        addItemsToList(photoList.map { PhotoUI(it) })
    }

    inner class PhotoUI(
        photoId: String,
        val url: String
    ) : RecyclerViewObject {

        override var id: String = photoId
        override var itemViewType: RecyclerViewObject.ItemViewType = RecyclerViewObject.ItemViewType.ITEM

        constructor(photo: Photo) : this(photo.id, (photo.getUrlOrFirstResult(SELECTED_LABEL)))

    }

}