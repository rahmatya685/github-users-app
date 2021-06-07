package java.com.znggis.githubusersapp.util

import com.google.gson.Gson
import com.znggis.githubusersapp.repo.remote.dto.user.UserSearchResp

object LoadItemsDto {
    fun loadFull(): UserSearchResp {
        val content = MockResponseTextReader(SEARCH_RESULT_JSON).content
        return Gson().fromJson(content, UserSearchResp::class.java)
    }
    fun loadEmpty(): UserSearchResp =
        UserSearchResp(
            total_count = 0,
            incomplete_results = false,
            items = emptyList()
        )
}