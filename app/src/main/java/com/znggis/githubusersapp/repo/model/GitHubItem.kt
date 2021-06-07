package com.znggis.githubusersapp.repo.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class GitHubItem(
    var id: Int,
    val item: String,
    val avatarUrl: String,
    val score: Int,
    val isFavourite: Boolean,
) : Parcelable