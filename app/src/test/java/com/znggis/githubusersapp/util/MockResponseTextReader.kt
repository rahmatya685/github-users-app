package com.znggis.githubusersapp.util

import java.io.InputStreamReader


class MockResponseTextReader(path: String)   {

    var content: String = ""

    init {
        val reader = InputStreamReader(this.javaClass.classLoader?.getResourceAsStream(path))

        content = reader.readText()
        reader.close()
    }
}