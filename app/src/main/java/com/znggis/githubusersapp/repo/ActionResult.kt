package com.znggis.githubusersapp.repo

sealed class ActionResult<out T> {
    data class Loading<T>(val message: String) : ActionResult<T>()
    data class Success<out T>(val data: T) : ActionResult<T>()
    data class Error<T>(val error: Throwable) : ActionResult<T>()
}