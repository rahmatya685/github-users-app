package com.znggis.githubusersapp

import android.app.Application
import com.znggis.githubusersapp.repo.Repository

class App : Application() {
    val repo: Repository
        get() = ServiceLocator.provideRepository(this)
}