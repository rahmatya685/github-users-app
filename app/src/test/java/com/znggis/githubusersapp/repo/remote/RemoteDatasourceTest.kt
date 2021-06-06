package com.znggis.githubusersapp.repo.remote

import android.os.Build
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import com.google.common.truth.Truth.assertThat
import com.znggis.githubusersapp.repo.remote.base.RetrofitCreator
import com.znggis.githubusersapp.util.MockResponseTextReader
import com.znggis.githubusersapp.util.SEARCH_RESULT_JSON
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.MockitoAnnotations
import org.robolectric.annotation.Config
import java.net.HttpURLConnection


@RunWith(AndroidJUnit4::class)
@SmallTest
@ExperimentalCoroutinesApi
@Config(sdk = [Build.VERSION_CODES.O_MR1])
class RemoteDatasourceTest {
    lateinit var mockWebServer: MockWebServer
    lateinit var remoteDatasource: RemoteDatasource


    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        mockWebServer = MockWebServer()
        mockWebServer.start()
        val home = mockWebServer.url("")
        val retrofit =
            RetrofitCreator(home.toUrl().toString())
                .build()
        remoteDatasource = DefaultRemoteDatasource(retrofit)
    }

    @Test
    fun `read sample success json file`() {
        val reader = MockResponseTextReader(SEARCH_RESULT_JSON)
        Assert.assertNotNull(reader.content)
    }


    @Test
    fun `Given request data, When Status 200-Ok, Then check if data is in Network Layer`() = runBlocking {
        val response = MockResponse()
            .setResponseCode(HttpURLConnection.HTTP_OK)
            .setBody(MockResponseTextReader(SEARCH_RESULT_JSON).content)

        mockWebServer.enqueue(response)

        val resp = remoteDatasource.search("",1,2)

        assertThat(resp.items.size).isEqualTo(2)
        assertThat(resp.incomplete_results).isEqualTo(false)
        assertThat(resp.total_count).isEqualTo(8240)

        assertThat(resp.items.first().avatar_url).isEqualTo("https://avatars.githubusercontent.com/u/1933118?v=4")
        assertThat(resp.items.first().login).isEqualTo("scisco")

    }


    @After
    fun finish() {
        mockWebServer.shutdown()
    }
}