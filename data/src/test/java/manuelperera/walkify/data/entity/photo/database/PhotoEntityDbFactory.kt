package manuelperera.walkify.data.entity.photo.database

import java.util.*

class PhotoEntityDbFactory {

    companion object {
        fun providesPhotoEntityDb(
            id: String = "ID",
            sizeInfoList: List<PhotoEntityDb.SizeInfo> = listOf(providesSizeInfo(), providesSizeInfo()),
            addedDate: Date = Date()
        ): PhotoEntityDb {
            return PhotoEntityDb(
                id = id,
                sizeInfoList = sizeInfoList,
                addedDate = addedDate
            )
        }

        private fun providesSizeInfo(
            label: String = "SMALL",
            url: String = "url"
        ): PhotoEntityDb.SizeInfo {
            return PhotoEntityDb.SizeInfo(
                label = label,
                url = url
            )
        }
    }

}