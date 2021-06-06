package com.znggis.githubusersapp.ui

import androidx.fragment.app.Fragment
import com.znggis.githubusersapp.App


fun Fragment.getViewModelsFactory(): ViewModelFactory {
    val repo =  ( requireContext().applicationContext as App).repo
    return ViewModelFactory(repo,this,null)
}
