package com.znggis.githubusersapp.ui.adapter

import com.znggis.githubusersapp.repo.model.GitHubItem

sealed class EventType{
    data class ShowDetail(val gitHubItem: GitHubItem): EventType()
    data class OnFavouriteClicked(val gitHubItem: GitHubItem): EventType()
}
