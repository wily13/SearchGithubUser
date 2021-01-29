package com.example.searchgithubuser.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class GithubUser(
    var id: Int = 0,
    var username: String?,
    var name: String?,
    var avatar: String?,
    var company: String?,
    var location: String?,
    var repository: Int?,
    var follower: Int?,
    var following: Int?,
    var html_url: String?,
    var email: String?,
    var blog: String?,
    var image_blob: ByteArray?
) : Parcelable

