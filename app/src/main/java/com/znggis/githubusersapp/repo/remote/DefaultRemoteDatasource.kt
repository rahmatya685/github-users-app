package com.znggis.githubusersapp.repo.remote

import com.znggis.githubusersapp.repo.remote.api.UserSearchApi
import retrofit2.Retrofit
import javax.inject.Inject

/**
 * Default implementation of [RemoteDatasource]
 * @param retrofit retrofit object for service creation
 */
class DefaultRemoteDatasource
@Inject constructor(
    private val api:UserSearchApi
) : RemoteDatasource {


    override suspend fun search(
        query: String,
        pageSize: Int,
        page: Int

    ) = api.search(query, pageSize, page)

}