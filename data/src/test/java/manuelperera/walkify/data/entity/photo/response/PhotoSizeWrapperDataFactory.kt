package manuelperera.walkify.data.entity.photo.response

class PhotoSizeWrapperDataFactory {

    companion object {
        fun providesPhotoSizeWrapperData(
            photoSizeResponse: PhotoSizeWrapperData.PhotoSizeResponse = providesPhotoSizeResponse()
        ): PhotoSizeWrapperData {
            return PhotoSizeWrapperData(photoSizeResponse)
        }

        private fun providesPhotoSizeResponse(
            photoSizeInfoResponseList: List<PhotoSizeWrapperData.PhotoSizeResponse.PhotoSizeInfoResponse> = listOf(
                providesPhotoSizeInfoResponse(),
                providesPhotoSizeInfoResponse(),
                providesPhotoSizeInfoResponse()
            )
        ): PhotoSizeWrapperData.PhotoSizeResponse {
            return PhotoSizeWrapperData.PhotoSizeResponse(
                photoSizeInfoResponseList
            )
        }

        private fun providesPhotoSizeInfoResponse(
            label: String = "label",
            url: String = "url"
        ): PhotoSizeWrapperData.PhotoSizeResponse.PhotoSizeInfoResponse {
            return PhotoSizeWrapperData.PhotoSizeResponse.PhotoSizeInfoResponse(label, url)
        }
    }

}