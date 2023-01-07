package com.org.datingapp.features.onboarding.photos

import android.net.Uri
import com.org.datingapp.features.onboarding.photos.PhotosAdapter.Companion.EMPTY_PHOTO_VIEW
import com.org.datingapp.features.onboarding.photos.PhotosAdapter.Companion.LOADED_PHOTO_VIEW

class PhotoListItemViewModel {

    var uri : Uri? = null
    fun getViewType() = if (uri == null) EMPTY_PHOTO_VIEW else LOADED_PHOTO_VIEW

}