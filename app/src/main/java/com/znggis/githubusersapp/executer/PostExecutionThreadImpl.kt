package com.znggis.githubusersapp.executer


import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

class PostExecutionThreadImpl : PostExecutionThread {
    override val main: CoroutineDispatcher = Dispatchers.Main
    override val io: CoroutineDispatcher = Dispatchers.IO
    override val default: CoroutineDispatcher = Dispatchers.Default
}