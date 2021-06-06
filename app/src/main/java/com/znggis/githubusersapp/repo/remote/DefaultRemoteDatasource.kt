package com.znggis.githubusersapp.repo.remote

import com.znggis.githubusersapp.repo.remote.api.UserSearchApi
import retrofit2.Retrofit

/**
 * Default implementation of [RemoteDatasource]
 * @param retrofit retrofit object for service creation
 */
class DefaultRemoteDatasource(retrofit: Retrofit) : RemoteDatasource {

    private val api by lazy { retrofit.create(UserSearchApi::class.java) }


    override suspend fun search(
        query: String,
        pageSize: Int,
        page: Int

    ) = api.search(query, pageSize, page)

}