package com.znggis.githubusersapp.repo.model

import com.google.common.truth.Truth.assertThat
import junit.framework.TestCase
import org.junit.Test

class QueryTest {

    @Test
    fun buildObj_getString_checkQuery() {
        val queryString = "Salam"
        val q = Query(queryString)

        assertThat(q.toString()).isEqualTo(queryString)
    }
}