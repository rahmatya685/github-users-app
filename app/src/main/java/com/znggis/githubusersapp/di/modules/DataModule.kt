package com.znggis.githubusersapp.di.modules

import com.znggis.githubusersapp.repo.DefaultRepository
import com.znggis.githubusersapp.repo.Repository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
interface DataModule {

    @get:[Binds Singleton]
    val DefaultRepository.repo: Repository

}