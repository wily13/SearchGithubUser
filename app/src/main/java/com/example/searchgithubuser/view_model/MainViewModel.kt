package com.example.searchgithubuser.view_model

import android.app.Activity
import android.util.Log
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.viewpager.widget.ViewPager
import com.bumptech.glide.Glide
import com.example.searchgithubuser.adapter.SectionPagerAdapter
import com.example.searchgithubuser.model.GithubUser
import com.google.android.material.tabs.TabLayout
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.AsyncHttpResponseHandler
import cz.msebera.android.httpclient.Header
import kotlinx.android.synthetic.main.activity_detail_users.*
import org.json.JSONArray
import org.json.JSONObject

class MainViewModel : ViewModel() {

    private val listGithubUsers = MutableLiveData<ArrayList<GithubUser>>()

    companion object {
        private const val TAG = "MainViewModel"
    }

    fun setGithubUser(userName: String, progressBar: ProgressBar, activity: Activity) {
        progressBar.visibility = View.VISIBLE

        val listItems = java.util.ArrayList<GithubUser>()

        val apiKey = "token 9a05aca2b2255562bf578a3c2cf77b618a20f9ff"
        val url = "https://api.github.com/search/users?q=$userName"

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

                    val responseObject = JSONObject(result)
                    val list = responseObject.getJSONArray("items")

                    for (i in 0 until list.length()) {
                        val githubUser = list.getJSONObject(i)

                        val username = githubUser.getString("login")
                        val avatar = githubUser.getString("avatar_url")
                        val htmlUrl = githubUser.getString("html_url")

                        val githubUserItems = GithubUser(
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

                        listItems.add(githubUserItems)
                    }

                    progressBar.visibility = View.GONE

                    //set data ke adapter
                    listGithubUsers.postValue(listItems)

                } catch (e: Exception) {
                    Log.d("Exception", e.message.toString())
                    progressBar.visibility = View.GONE
                    Toast.makeText(activity, "Error - Exception: "+ e.message.toString(), Toast.LENGTH_SHORT).show()
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
                Toast.makeText(activity, "Error - onFailure: "+ error?.message.toString(), Toast.LENGTH_SHORT).show()
            }
        })
    }

    fun setDetailGithubUser(userName: String,  activity: Activity,
                            sectionPagerAdapter: SectionPagerAdapter, viewPager: ViewPager,
                            tabLayout: TabLayout, progressBar: ProgressBar){
        progressBar.visibility = View.VISIBLE
        activity.fab_favorites.hide()

        val apiKey = "token 9a05aca2b2255562bf578a3c2cf77b618a20f9ff"
        val url = "https://api.github.com/users/${userName}"

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

                    val responseObject = JSONObject(result)

                    GithubUser(
                        0,
                        responseObject.getString("login"),
                        responseObject.getString("name"),
                        responseObject.getString("avatar_url"),
                        responseObject.getString("company"),
                        responseObject.getString("location"),
                        responseObject.getInt("public_repos"),
                        responseObject.getInt("followers"),
                        responseObject.getInt("following"),
                        responseObject.getString("html_url"),
                        responseObject.getString("email"),
                        responseObject.getString("blog"),
                        null
                    )

                    responseObject.let {
                        activity.tv_username.text = it.getString("login")
                        activity.tv_name.text = it.getString("name")
                        activity.tv_company.text = it.getString("company")
                        activity.tv_location.text = it.getString("location")
                        activity.tv_repository.text = it.getString("public_repos")
                        activity.tv_follower.text = it.getString("followers")
                        activity.tv_following.text = it.getString("following")
                        activity.tv_email.text = it.getString("email")
                        activity.tv_blog.text = it.getString("blog")
                        activity.tv_url_image.text = it.getString("avatar_url")
                        activity.tv_github_url.text = it.getString("html_url")

                        Glide.with(activity)
                            .load(it.getString("avatar_url"))
                            .into(activity.img_avatar)

                    }

                    progressBar.visibility = View.GONE
                    activity.fab_favorites.show()

                    // set fragment
                    sectionPagerAdapter.userName = responseObject.getString("login")
                    viewPager.adapter = sectionPagerAdapter
                    tabLayout.setupWithViewPager(viewPager)

                } catch (e: Exception) {
                    Log.d("Exception", e.message.toString())
                    progressBar.visibility = View.VISIBLE
                    Toast.makeText(activity, "Error - Exception: "+ e.message.toString(), Toast.LENGTH_SHORT).show()
                }

            }

            override fun onFailure(
                statusCode: Int,
                headers: Array<Header>?,
                responseBody: ByteArray?,
                error: Throwable?
            ) {
                Log.d("onFailure", error?.message.toString())
                progressBar.visibility = View.VISIBLE
                Toast.makeText(activity, "Error - onFailure: "+ error?.message.toString(), Toast.LENGTH_SHORT).show()
            }
        })
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