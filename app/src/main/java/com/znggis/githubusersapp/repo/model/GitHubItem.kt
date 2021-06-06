package com.znggis.githubusersapp.repo.model


data class GitHubItem(
    var id: Int,
    val item: String,
    val avatarUrl: String,
    val score: Int,
    val isFavourite: Boolean,
)