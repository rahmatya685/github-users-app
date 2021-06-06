package com.znggis.githubusersapp.ui.image

import android.graphics.Bitmap
import android.widget.ImageView

interface ImageLoader {
    fun loadImage(view: ImageView, bitmap: Bitmap)
}