package java.com.znggis.githubusersapp.util

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.znggis.githubusersapp.repo.local.Database

object DbFactory {

    fun buildDb() = Room.inMemoryDatabaseBuilder(
        ApplicationProvider.getApplicationContext(),
        Database::class.java
    )
        .allowMainThreadQueries()
        .build()
}