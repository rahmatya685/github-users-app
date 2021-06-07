package com.znggis.githubusersapp.di

import com.znggis.githubusersapp.di.modules.DataModule
import com.znggis.githubusersapp.repo.FakeRepo
import com.znggis.githubusersapp.repo.Repository
import dagger.Module
import dagger.Provides
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import javax.inject.Singleton


@Module
@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [DataModule::class]
)
class FakeDataModule {
    @Singleton
    @Provides
    fun provideFakeRepo(): Repository = FakeRepo()
}