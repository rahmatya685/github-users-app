package com.znggis.githubusersapp.di.modules

import com.znggis.githubusersapp.executer.PostExecutionThread
import com.znggis.githubusersapp.executer.PostExecutionThreadImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@InstallIn(SingletonComponent::class)
@Module
interface DispatcherModule {
    @get:[Binds Singleton]
    val PostExecutionThreadImpl.postExecutionThread: PostExecutionThread
}