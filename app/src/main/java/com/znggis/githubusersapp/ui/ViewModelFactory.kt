package com.znggis.githubusersapp.ui

import android.os.Bundle
import androidx.lifecycle.AbstractSavedStateViewModelFactory
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.savedstate.SavedStateRegistryOwner
import com.znggis.githubusersapp.executer.PostExecutionThreadImpl
import com.znggis.githubusersapp.repo.Repository
import java.lang.IllegalStateException
import javax.inject.Inject


/**
 * A factory for all ViewModels
 */


//class ViewModelFactory @Inject constructor(
//    private val repo: Repository,
//    owner: SavedStateRegistryOwner,
//    defaultArgs: Bundle?
//) : AbstractSavedStateViewModelFactory(owner, defaultArgs) {
//
//    override fun <T : ViewModel?> create(
//        key: String,
//        modelClass: Class<T>,
//        handle: SavedStateHandle
//    ) =with(modelClass){
//        when{
//            isAssignableFrom(UserItemsViewModel::class.java) -> UserItemsViewModel(handle,repo,PostExecutionThreadImpl())
//            else -> throw IllegalStateException("Unknown ViewModel class for $name")
//        }
//    } as T
//}