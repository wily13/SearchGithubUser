package com.example.favoriteconsumerapp.view_model

import android.util.Log
import android.view.View
import android.widget.ProgressBar
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.favoriteconsumerapp.model.GithubUser
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.AsyncHttpResponseHandler
import cz.msebera.android.httpclient.Header
import org.json.JSONArray

class MainViewModel : ViewModel() {

    private val listGithubUsers = MutableLiveData<ArrayList<GithubUser>>()

    companion object {
        private const val TAG = "MainViewModel"
    }

    fun setFollowerUser(userName: String, progressBar: ProgressBar) {
        progressBar.visibility = View.VISIBLE

        val listItems = java.util.ArrayList<GithubUser>()

        val apiKey = "token 9a05aca2b2255562bf578a3c2cf77b618a20f9ff"
        val url = "https://api.github.com/users/$userName/followers"

        val client = AsyncHttpClient()
        client.addHeader("Authorization", apiKey)
        client.addHeader("User-Agent", "request")
        client.get(url, object : AsyncHttpResponseHandler() {
            override fun onSuccess(
                statusCode: Int,
                headers: Array<Header>,
                responseBody: ByteArray
            ) {
                try {
                    //parsing json
                    val result = String(responseBody)
                    Log.d(TAG, "result: " + result)

                    val list = JSONArray(result)

                    for (i in 0 until list.length()) {
                        val followerUser = list.getJSONObject(i)

                        val username = followerUser.getString("login")
                        val avatar = followerUser.getString("avatar_url")
                        val htmlUrl = followerUser.getString("html_url")

                        val followerUserItems = GithubUser(
                            0,
                            username,
                            null,
                            avatar,
                            null,
                            null,
                            null,
                            null,
                            null,
                            htmlUrl,
                            null,
                            null,
                            null
                        )

                        listItems.add(followerUserItems)
                    }

                    listGithubUsers.postValue(listItems)
                    progressBar.visibility = View.GONE

                } catch (e: Exception) {
                    Log.d("Exception", e.message.toString())
                    progressBar.visibility = View.GONE
                }

            }

            override fun onFailure(
                statusCode: Int,
                headers: Array<Header>?,
                responseBody: ByteArray?,
                error: Throwable?
            ) {
                Log.d("onFailure", error?.message.toString())
                progressBar.visibility = View.GONE
            }
        })
    }

    fun setFollowingUser(userName: String, progressBar: ProgressBar) {
        progressBar.visibility = View.VISIBLE

        val listItems = java.util.ArrayList<GithubUser>()

        val apiKey = "token 9a05aca2b2255562bf578a3c2cf77b618a20f9ff"
        val url = "https://api.github.com/users/$userName/following"

        val client = AsyncHttpClient()
        client.addHeader("Authorization", apiKey)
        client.addHeader("User-Agent", "request")
        client.get(url, object : AsyncHttpResponseHandler() {
            override fun onSuccess(
                statusCode: Int,
                headers: Array<Header>,
                responseBody: ByteArray
            ) {
                try {
                    //parsing json
                    val result = String(responseBody)
                    Log.d(TAG, "result: " + result)

                    val list = JSONArray(result)

                    for (i in 0 until list.length()) {
                        val followingUser = list.getJSONObject(i)

                        val username = followingUser.getString("login")
                        val avatar = followingUser.getString("avatar_url")
                        val htmlUrl = followingUser.getString("html_url")

                        val followingUserItems = GithubUser(
                            0,
                            username,
                            null,
                            avatar,
                            null,
                            null,
                            null,
                            null,
                            null,
                            htmlUrl,
                            null,
                            null,
                            null
                        )

                        listItems.add(followingUserItems)
                    }

                    listGithubUsers.postValue(listItems)
                    progressBar.visibility = View.GONE

                } catch (e: Exception) {
                    Log.d("Exception", e.message.toString())
                    progressBar.visibility = View.GONE
                }

            }

            override fun onFailure(
                statusCode: Int,
                headers: Array<Header>?,
                responseBody: ByteArray?,
                error: Throwable?
            ) {
                Log.d("onFailure", error?.message.toString())
                progressBar.visibility = View.GONE
            }
        })
    }


    fun getGithubUser(): LiveData<ArrayList<GithubUser>> {
        return listGithubUsers
    }

}