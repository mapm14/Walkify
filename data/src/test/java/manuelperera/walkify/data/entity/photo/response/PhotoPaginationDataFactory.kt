package manuelperera.walkify.data.entity.photo.response

class PhotoPaginationDataFactory {

    companion object {
        fun providesPhotoPaginationData(
            photoInfo: PhotoPaginationData.PhotoInfoResponse = providesPhotoInfoResponse()
        ): PhotoPaginationData {
            return PhotoPaginationData(photoInfo)
        }

        private fun providesPhotoInfoResponse(
            photoList: List<PhotoPaginationData.PhotoInfoResponse.PhotoIdResponse> = providesPhotoIdResponseList()
        ): PhotoPaginationData.PhotoInfoResponse {
            return PhotoPaginationData.PhotoInfoResponse(photoList)
        }

        private fun providesPhotoIdResponseList(
            vararg id: String = arrayOf("1", "2", "3")
        ): List<PhotoPaginationData.PhotoInfoResponse.PhotoIdResponse> {
            return id.map { PhotoPaginationData.PhotoInfoResponse.PhotoIdResponse(it) }
        }
    }

}