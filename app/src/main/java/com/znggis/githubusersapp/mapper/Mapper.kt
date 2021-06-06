package com.znggis.githubusersapp.mapper

abstract class Mapper<I, O> {
    abstract fun convert(i: I): O
    fun convert(list: List<I>): List<O> = list.map { convert(it) }

}