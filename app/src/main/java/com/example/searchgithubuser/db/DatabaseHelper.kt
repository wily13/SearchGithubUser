package com.example.searchgithubuser.db

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.searchgithubuser.db.DatabaseContract.UserColumn
import com.example.searchgithubuser.db.DatabaseContract.UserColumn.Companion.TABLE_NAME

internal class DatabaseHelper(context: Context): SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "bdgithubuser"
        private const val DATABASE_VERSION = 1

        private val SQL_CREATE_TABLE_USER = "CREATE TABLE $TABLE_NAME" +
                " (${UserColumn.ID} INTEGER PRIMARY KEY AUTOINCREMENT," +
                " ${UserColumn.USERNAME} TEXT NOT NULL," +
                " ${UserColumn.NAME} TEXT NOT NULL," +
                " ${UserColumn.AVATAR} BLOB NOT NULL," +
                " ${UserColumn.COMPANY} TEXT NULL," +
                " ${UserColumn.LOCATION} TEXT NULL," +
                " ${UserColumn.REPOSITORY} TEXT NULL," +
                " ${UserColumn.FOLLOWER} TEXT NULL," +
                " ${UserColumn.FOLLOWING} TEXT NULL," +
                " ${UserColumn.EMAIL} TEXT NULL," +
                " ${UserColumn.BLOG} TEXT NULL," +
                " ${UserColumn.GITHUB_URL} TEXT NULL," +
                " ${UserColumn.STATUS_FAVORITE} TEXT NOT NULL)"
    }

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(SQL_CREATE_TABLE_USER)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db)
    }
}