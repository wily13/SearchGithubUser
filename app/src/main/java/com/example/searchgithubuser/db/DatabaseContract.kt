package com.example.searchgithubuser.db

import android.net.Uri
import android.provider.BaseColumns

object DatabaseContract {

    // Authority yang digunakan
    const val AUTHORITY = "com.example.searchgithubuser"
    const val SCHEME = "content"

    internal class UserColumn: BaseColumns{
        companion object {
            const val TABLE_NAME = "favorite_user"
            const val ID = "_id"
            const val USERNAME = "username"
            const val NAME = "name"
            const val AVATAR = "avatar"
            const val COMPANY = "company"
            const val LOCATION = "location"
            const val REPOSITORY = "repository"
            const val FOLLOWER = "follower"
            const val FOLLOWING = "following"
            const val EMAIL = "email"
            const val BLOG = "blog"
            const val GITHUB_URL = "html_url"
            const val STATUS_FAVORITE = "status_favorite"

            // Base content yang digunakan untuk akses content provider
            val CONTENT_URI: Uri = Uri.Builder().scheme(SCHEME)
                .authority(AUTHORITY)
                .appendPath(TABLE_NAME)
                .build()
        }
    }
}