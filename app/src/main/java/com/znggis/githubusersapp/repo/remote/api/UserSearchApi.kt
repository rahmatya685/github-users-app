package com.znggis.githubusersapp.repo.remote.api

import com.znggis.githubusersapp.repo.remote.dto.user.UserSearchResp
import retrofit2.http.GET
import retrofit2.http.Query

interface UserSearchApi {

    companion object {
        const val PATH_NAME = "search/users"
        const val QUERY_PARAM = "q"
        const val PAGE_SIZE_PARAM = "per_page"
        const val PAGE_PARAM = "page"
    }

    @GET(PATH_NAME)
    suspend fun search(
        @Query(QUERY_PARAM) q: String,
        @Query(PAGE_SIZE_PARAM) pageSize: Int,
        @Query(PAGE_PARAM) page: Int,
    ): UserSearchResp
}