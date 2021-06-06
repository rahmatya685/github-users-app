package com.znggis.githubusersapp.ui.image

import android.graphics.Bitmap
import android.widget.ImageView
import coil.load
import coil.size.ViewSizeResolver
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ImageLoaderImpl @Inject constructor() : ImageLoader {
    override fun loadImage(view: ImageView, bitmap: Bitmap) {
        view.load(bitmap) {
            crossfade(true)
            size(ViewSizeResolver(view, false))
            listener(
                onError = { _, throwable ->
                    throwable.printStackTrace()
                }
            )
        }
    }
}