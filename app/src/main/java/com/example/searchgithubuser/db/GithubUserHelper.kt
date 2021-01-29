package com.example.searchgithubuser.db

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.SQLException
import android.database.sqlite.SQLiteDatabase
import android.provider.BaseColumns
import com.example.searchgithubuser.db.DatabaseContract.UserColumn.Companion.TABLE_NAME
import com.example.searchgithubuser.db.DatabaseContract.UserColumn.Companion.USERNAME

class GithubUserHelper(context: Context) {

    private var dataBaseHelper: DatabaseHelper = DatabaseHelper(context)
    private lateinit var database: SQLiteDatabase

    companion object{
        private const val DATABASE_TABLE = TABLE_NAME
        private var INSTANCE: GithubUserHelper? = null

        fun getInstance(context: Context): GithubUserHelper =
            INSTANCE ?: synchronized(this){
                INSTANCE ?: GithubUserHelper(context)
            }
    }

    @Throws(SQLException::class)
    fun open() {
        database = dataBaseHelper.writableDatabase
    }

    fun queryAll(): Cursor {
        return database.query(
            DATABASE_TABLE,
            null,
            null,
            null,
            null,
            null,
            "${BaseColumns._ID} DESC"
        )
    }

    fun queryById(id: String): Cursor {
        return database.query(
            DATABASE_TABLE,
            null,
            "${BaseColumns._ID} = ?",
            arrayOf(id),
            null,
            null,
            null,
            null)
    }

    fun queryByUsername(username: String): Cursor {
        return database.query(
            DATABASE_TABLE,
            null,
            "$USERNAME = ?",
            arrayOf(username),
            null,
            null,
            null,
            null)
    }

    fun insert(values: ContentValues?): Long {
        return database.insert(DATABASE_TABLE, null, values)
    }

    fun updateById(id: String, values: ContentValues?): Int =
        database.update(DATABASE_TABLE, values, "${BaseColumns._ID} = ?", arrayOf(id))


    fun deleteById(id: String): Int =
        database.delete(DATABASE_TABLE, "${BaseColumns._ID} = '$id'", null)


    fun deleteByUsername(username: String): Int =
        database.delete(DATABASE_TABLE, "$USERNAME = '$username'", null)

}