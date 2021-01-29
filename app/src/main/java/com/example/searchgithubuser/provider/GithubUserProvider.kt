package com.example.searchgithubuser.provider

import android.content.ContentProvider
import android.content.ContentValues
import android.content.Context
import android.content.UriMatcher
import android.database.Cursor
import android.net.Uri
import com.example.searchgithubuser.db.DatabaseContract.AUTHORITY
import com.example.searchgithubuser.db.DatabaseContract.UserColumn.Companion.CONTENT_URI
import com.example.searchgithubuser.db.DatabaseContract.UserColumn.Companion.TABLE_NAME
import com.example.searchgithubuser.db.GithubUserHelper

class GithubUserProvider : ContentProvider() {

    companion object {
        private const val USER = 1
        private const val USER_ID = 2
        private const val USER_USERNAME = 3
        private val sUriMatcher = UriMatcher(UriMatcher.NO_MATCH)
        private lateinit var githubUserHelper: GithubUserHelper

        init {
            // content://com.example.searchgithubuser/githubuser
            sUriMatcher.addURI(AUTHORITY, TABLE_NAME, USER)

            // content://com.example.searchgithubuser/githubuser/id
            sUriMatcher.addURI(
                AUTHORITY,
                "$TABLE_NAME/#",
                USER_ID
            )

            // content://com.example.searchgithubuser/githubuser/username
            sUriMatcher.addURI(
                AUTHORITY,
                "$TABLE_NAME/*",
                USER_USERNAME
            )
        }
    }

    override fun onCreate(): Boolean {
        githubUserHelper = GithubUserHelper.getInstance(context as Context)
        githubUserHelper.open()
        return true
    }

    override fun query(
        uri: Uri, projection: Array<String>?, selection: String?,
        selectionArgs: Array<String>?, sortOrder: String?
    ): Cursor? {
        val cursor: Cursor?
        when (sUriMatcher.match(uri)) {
            USER -> cursor = githubUserHelper.queryAll()
            USER_ID -> cursor = githubUserHelper.queryById(uri.lastPathSegment.toString())
            USER_USERNAME -> cursor = githubUserHelper.queryByUsername(selection.toString())
            else -> cursor = null
        }
        return cursor
    }

    override fun getType(uri: Uri): String? {
        return null
    }

    override fun insert(uri: Uri, values: ContentValues?): Uri? {
        val added: Long = when (USER) {
            sUriMatcher.match(uri) -> githubUserHelper.insert(values)
            else -> 0
        }

        context?.contentResolver?.notifyChange(CONTENT_URI, null)

        return Uri.parse("$CONTENT_URI/$added")
    }

    override fun update(
        uri: Uri, values: ContentValues?, selection: String?,
        selectionArgs: Array<String>?
    ): Int {
        return when (USER_ID) {
            sUriMatcher.match(uri) -> githubUserHelper.updateById(
                uri.lastPathSegment.toString(),
                values
            )
            else -> 0
        }

    }

    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<String>?): Int {
        val deleted: Int?
        when (sUriMatcher.match(uri)) {
            USER_ID -> deleted = githubUserHelper.deleteById(uri.lastPathSegment.toString())
            USER_USERNAME -> deleted = githubUserHelper.deleteByUsername(selection.toString())
            else -> deleted = 0
        }

        context?.contentResolver?.notifyChange(CONTENT_URI, null)

        return deleted
    }

}
