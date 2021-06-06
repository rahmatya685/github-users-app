package com.znggis.githubusersapp.repo.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize


@Parcelize
class Query(val text: String) : Parcelable {

    override fun toString(): String {
        return text
    }
}