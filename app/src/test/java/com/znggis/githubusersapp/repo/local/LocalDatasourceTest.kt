package com.znggis.githubusersapp.repo.local

import android.os.Build
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.MediumTest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.resetMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.runner.RunWith
import org.robolectric.annotation.Config


@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
@MediumTest
@Config(sdk = [Build.VERSION_CODES.O_MR1])
class LocalDatasourceTest {

    private lateinit var database: Database
    private lateinit var dataSource: LocalDatasource

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()


    @ExperimentalCoroutinesApi
    @Before
    fun setup() {
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            Database::class.java
        )
            .allowMainThreadQueries()
            .build()

        dataSource = DefaultLocalDatasource(
            database.itemDao()
        )
    }

    @After
    fun cleanUp() {
        database.close()
        Dispatchers.resetMain()
    }

}