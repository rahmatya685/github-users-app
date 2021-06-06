package com.znggis.githubusersapp

import android.content.Context
import androidx.annotation.VisibleForTesting
import androidx.room.Room
import com.znggis.githubusersapp.executer.PostExecutionThreadImpl
import com.znggis.githubusersapp.platform.NetworkHandler
import com.znggis.githubusersapp.repo.DefaultRepository
import com.znggis.githubusersapp.repo.Repository
import com.znggis.githubusersapp.repo.local.DB_NAME
import com.znggis.githubusersapp.repo.local.Database
import com.znggis.githubusersapp.repo.local.DefaultLocalDatasource
import com.znggis.githubusersapp.repo.local.LocalDatasource
import com.znggis.githubusersapp.repo.remote.DefaultRemoteDatasource
import com.znggis.githubusersapp.repo.remote.RemoteDatasource
import com.znggis.githubusersapp.repo.remote.base.RetrofitCreator


/**
 * dependency provide
 */
object ServiceLocator {

    private val lock = Any()
    private var database: Database? = null

    @Volatile
    var repo: Repository? = null
        @VisibleForTesting set

    fun provideRepository(context: Context): Repository {
        synchronized(this) {
            return repo ?: createRepo(context)
        }
    }

    private fun createRepo(context: Context): Repository {
        val repo = DefaultRepository(
            createLocalDataSource(context),
            createRemoteDataSource(),
            NetworkHandler(context)
        )
        this.repo = repo
        return repo
    }


    private fun createRemoteDataSource(): RemoteDatasource {
        val retrofit = RetrofitCreator(BuildConfig.END_POINT).build()
        return DefaultRemoteDatasource(retrofit)
    }

    private fun createLocalDataSource(context: Context): LocalDatasource {
        val db = database ?: createDb(context)
        return DefaultLocalDatasource(db)
    }

    private fun createDb(context: Context): Database {
        val newDb = Room.databaseBuilder(
            context,
            Database::class.java,
            DB_NAME
        ).build()
        database = newDb
        return newDb
    }
}