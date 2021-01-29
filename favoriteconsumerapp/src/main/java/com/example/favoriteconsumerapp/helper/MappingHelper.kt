package com.example.favoriteconsumerapp.helper

import android.database.Cursor
import com.example.favoriteconsumerapp.db.DatabaseContract
import com.example.favoriteconsumerapp.model.GithubUser

object MappingHelper {

    fun mapCursorToArrayList(userCursor: Cursor?): ArrayList<GithubUser> {
        val userList = ArrayList<GithubUser>()

        userCursor?.apply {
            while (moveToNext()){
                val id = getInt(getColumnIndexOrThrow(DatabaseContract.UserColumn.ID))
                val username = getString(getColumnIndexOrThrow(DatabaseContract.UserColumn.USERNAME))
                val githubUrl = getString(getColumnIndexOrThrow(DatabaseContract.UserColumn.GITHUB_URL))
                val image = getBlob(getColumnIndexOrThrow(DatabaseContract.UserColumn.AVATAR))

                userList.add(GithubUser(id, username, null, null, null, null, null,null, null, githubUrl, null, null, image))
            }
        }
        return userList
    }


    fun mapCursorToObject(notesCursor: Cursor?): GithubUser {
        var user = GithubUser(0,null,null,null,null,null,null,null,null,null,null,null,null)
        notesCursor?.apply {
            moveToFirst()
            val id = getInt(getColumnIndexOrThrow(DatabaseContract.UserColumn.ID))
            val username = getString(getColumnIndexOrThrow(DatabaseContract.UserColumn.USERNAME))
            val name = getString(getColumnIndexOrThrow(DatabaseContract.UserColumn.NAME))
            val company = getString(getColumnIndexOrThrow(DatabaseContract.UserColumn.COMPANY))
            val location = getString(getColumnIndexOrThrow(DatabaseContract.UserColumn.LOCATION))
            val repository = getInt(getColumnIndexOrThrow(DatabaseContract.UserColumn.REPOSITORY))
            val follower = getInt(getColumnIndexOrThrow(DatabaseContract.UserColumn.FOLLOWER))
            val following = getInt(getColumnIndexOrThrow(DatabaseContract.UserColumn.FOLLOWING))
            val email = getString(getColumnIndexOrThrow(DatabaseContract.UserColumn.EMAIL))
            val blog = getString(getColumnIndexOrThrow(DatabaseContract.UserColumn.BLOG))
            val githubUrl = getString(getColumnIndexOrThrow(DatabaseContract.UserColumn.GITHUB_URL))
            val imageAvatar = getBlob(getColumnIndexOrThrow(DatabaseContract.UserColumn.AVATAR))
            user = GithubUser(0,username,name,null,company,location,repository,follower,following,githubUrl,email,blog,imageAvatar)
        }
        return user
    }

}