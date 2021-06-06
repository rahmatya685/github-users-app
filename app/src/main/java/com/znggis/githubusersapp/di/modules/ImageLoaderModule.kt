package com.znggis.githubusersapp.di.modules

import com.znggis.githubusersapp.ui.image.ImageLoader
import com.znggis.githubusersapp.ui.image.ImageLoaderImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
interface ImageLoaderModule {

    @get:[Binds Singleton]
    val ImageLoaderImpl.imageLoader: ImageLoader
}
