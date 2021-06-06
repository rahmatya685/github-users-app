package com.znggis.githubusersapp.repo.remote

import com.znggis.githubusersapp.repo.remote.dto.user.UserSearchResp


/**
 * Main gateway to call endpoints
 */
interface RemoteDatasource {
    suspend fun search(
        query: String,
        pageSize: Int,
        page: Int
    ): UserSearchResp

}