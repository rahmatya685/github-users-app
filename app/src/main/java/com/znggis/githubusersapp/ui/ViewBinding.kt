package com.znggis.githubusersapp.ui

import android.widget.ImageButton
import androidx.databinding.BindingAdapter
import com.znggis.githubusersapp.R

@BindingAdapter("app:isFavourite")
fun setFavIcon(imageView: ImageButton, isFavourite: Boolean) {
    if (isFavourite)
        imageView.setImageResource(R.drawable.ic_baseline_favorite_24)
    else
        imageView.setImageResource(R.drawable.ic_baseline_favorite_border_24)
}