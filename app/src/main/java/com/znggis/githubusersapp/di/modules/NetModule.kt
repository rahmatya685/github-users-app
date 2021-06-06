package com.znggis.githubusersapp.di.modules


import com.znggis.githubusersapp.BuildConfig
import com.znggis.githubusersapp.repo.remote.DefaultRemoteDatasource
import com.znggis.githubusersapp.repo.remote.RemoteDatasource
import com.znggis.githubusersapp.repo.remote.api.UserSearchApi
import com.znggis.githubusersapp.repo.remote.base.RetrofitCreator
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@InstallIn(SingletonComponent::class)
@Module
interface NetModule {

    @get:[Binds Singleton]
    val DefaultRemoteDatasource.defRemoteDatastore: RemoteDatasource


    companion object {
        @Singleton
        @Provides
        fun provideUserSearchApi(): UserSearchApi =
            RetrofitCreator(BuildConfig.END_POINT)
                .build().create(UserSearchApi::class.java)

    }
}