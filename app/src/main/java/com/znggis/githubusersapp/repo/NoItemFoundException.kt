package com.znggis.githubusersapp.repo

import java.lang.Exception

const val NO_ITEM_FOUND = "No Search Results"

class NoItemFoundException : Exception(NO_ITEM_FOUND)