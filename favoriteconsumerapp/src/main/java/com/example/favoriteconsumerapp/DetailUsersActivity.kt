package com.example.favoriteconsumerapp

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.graphics.drawable.toBitmap
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager.widget.ViewPager
import com.bumptech.glide.Glide
import com.example.favoriteconsumerapp.adapter.SectionPagerAdapter
import com.example.favoriteconsumerapp.db.DatabaseContract
import com.example.favoriteconsumerapp.db.DatabaseContract.UserColumn.Companion.CONTENT_URI
import com.example.favoriteconsumerapp.helper.BitmapUtils
import com.example.favoriteconsumerapp.helper.MappingHelper
import com.example.favoriteconsumerapp.model.GithubUser
import com.example.favoriteconsumerapp.view_model.MainViewModel
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.tabs.TabLayout
import kotlinx.android.synthetic.main.activity_detail_users.*
import kotlinx.android.synthetic.main.tab_fragment.*


class DetailUsersActivity : AppCompatActivity() {

    private lateinit var mainViewModel: MainViewModel
    private var githubUser: GithubUser? = null
    private lateinit var uriWithId: Uri
    private lateinit var uriWithUsername: Uri

    private var statusFavorites = false
    private var usernameStatus: Int = 0

    private var dataId = 0
    private lateinit var dataUser: String

    companion object {
        private const val TAG = "DetailUsersActivity"
        const val URL_GITHUB = "https://github.com/"
        const val REQUEST_UPDATE = 200
        const val EXTRA_ID = "extra_id"
        const val EXTRA_USER = "extra_users"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_users)

        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
        }

        supportActionBar?.title = resources.getString(R.string.title_activity_detail_users)

         // Menyambungkan kelas MainViewModel dengan Fragment
        mainViewModel = ViewModelProvider(
            this,
            ViewModelProvider.NewInstanceFactory()
        ).get(MainViewModel::class.java)

        // set fragment
        val sectionPagerAdapter = SectionPagerAdapter(this, supportFragmentManager)

        // get data from intent
        dataUser = intent.getStringExtra(EXTRA_USER).toString()
        dataId = intent.getIntExtra(EXTRA_ID, 0)

        // Uri yang di dapatkan digunakan untuk ambil data dari provider
        // content://com.example.searchgithubuser/githubuser/id
        uriWithId = Uri.parse(CONTENT_URI.toString() + "/" + dataId)

        // content://com.example.searchgithubuser/githubuser/username
        uriWithUsername = Uri.parse(CONTENT_URI.toString() + "/" + dataUser)

        val cursor = contentResolver.query(uriWithUsername, null, dataUser, null, null)
        if (cursor != null) {
            Log.d(TAG, "cursor result: "+ cursor.count)
            // status 1 data username save in database
            usernameStatus = cursor.count
            cursor.close()
        }

        // cek activity from search or favorite activity
        if (dataId > 0) {
            // set detail favorite user
            setDetailFavoriteUser(
                sectionPagerAdapter,
                view_pager,
                tabs
            )

        }

        // set default status favorites
        if (usernameStatus > 0) {
            statusFavorites = true
            setStatusFavorites(statusFavorites)
        }


        // Button follow
        btn_follow.setOnClickListener {
            val uriString: String = URL_GITHUB + tv_username.text.toString().trim()
            loadIntentImplicit(uriString)
        }

        // fab button favorite
        fab_favorites.setOnClickListener {
            statusFavorites = !statusFavorites
            setStatusFavorites(statusFavorites)

            // call from favorite activity
            if (dataId > 0) {
                showLoading(true)

                if (statusFavorites) {
                    // insert to database
                    saveToFavorites()
                } else {
                    // delete from database
                    deleteFromFavorite(dataId)
                }
            }

        }

    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }

    private fun saveToFavorites() {
        val username = tv_username.text.toString().trim()
        val name = tv_name.text.toString().trim()
        val company = tv_company.text.toString().trim()
        val location = tv_location.text.toString().trim()
        val repository = tv_repository.text.toString().trim()
        val follower = tv_follower.text.toString().trim()
        val following = tv_following.text.toString().trim()
        val email = tv_email.text.toString().trim()
        val blog = tv_blog.text.toString().trim()
        val githubUrl = tv_github_url.text.toString().trim()
        val avatar = BitmapUtils.getBytes(img_avatar.drawable.toBitmap())

        val values = ContentValues()
        values.put(DatabaseContract.UserColumn.USERNAME, username)
        values.put(DatabaseContract.UserColumn.NAME, name)
        values.put(DatabaseContract.UserColumn.AVATAR, avatar)
        values.put(DatabaseContract.UserColumn.COMPANY, company)
        values.put(DatabaseContract.UserColumn.LOCATION, location)
        values.put(DatabaseContract.UserColumn.REPOSITORY, repository)
        values.put(DatabaseContract.UserColumn.FOLLOWER, follower)
        values.put(DatabaseContract.UserColumn.FOLLOWING, following)
        values.put(DatabaseContract.UserColumn.EMAIL, email)
        values.put(DatabaseContract.UserColumn.BLOG, blog)
        values.put(DatabaseContract.UserColumn.GITHUB_URL, githubUrl)
        values.put(DatabaseContract.UserColumn.STATUS_FAVORITE, statusFavorites.toString())

        // Uri yang di dapatkan digunakan untuk simpan data dari provider
        // content://com.example.searchgithubuser/githubuser/id
        contentResolver.insert(CONTENT_URI, values)
        showSnackbarMessage(getString(R.string.msg_save_success))

        showLoading(false)
    }

    private fun deleteFromFavorite(id: Int) {
        if (id > 0) {

            // Uri yang di dapatkan akan digunakan untuk hapus data dari provider
            // content://com.example.searchgithubuser/githubuser/id
            val result = contentResolver.delete(uriWithId, null, null)
            if (result > 0) {
                showSnackbarMessage(getString(R.string.msg_delete_success))
            } else {
                showSnackbarMessage(getString(R.string.msg_delete_failed))
            }

        } else {

            contentResolver.delete(uriWithUsername, dataUser, null)
            showSnackbarMessage(getString(R.string.msg_delete_success))
        }

        showLoading(false)
    }

    private fun setDetailFavoriteUser(
        sectionPagerAdapter: SectionPagerAdapter,
        viewPager: ViewPager, tabLayout: TabLayout
    ) {

        val cursor = contentResolver.query(uriWithId, null, null, null, null)
        if (cursor != null) {
            githubUser = MappingHelper.mapCursorToObject(cursor)
            cursor.close()
        }

        githubUser?.let {
            tv_username.text = it.username.toString()
            tv_name.text = it.name.toString()
            tv_company.text = it.company.toString()
            tv_location.text = it.location
            tv_repository.text = it.repository.toString()
            tv_follower.text = it.follower.toString()
            tv_following.text = it.following.toString()
            tv_email.text = it.email.toString()
            tv_blog.text = it.blog.toString()
            tv_github_url.text = it.html_url.toString()

            val imageAvatar = it.image_blob?.let { BitmapUtils.getImage(it) }

            Glide.with(applicationContext)
                .asBitmap()
                .load(imageAvatar)
                .into(img_avatar)

            // set fragment
            sectionPagerAdapter.userName = it.username
            viewPager.adapter = sectionPagerAdapter
            tabLayout.setupWithViewPager(viewPager)
        }

        showLoading(false)
    }

    // load explisit intent
    private fun loadIntentImplicit(uriString: String) {
        val gmmIntentUri = Uri.parse(uriString)
        val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
        mapIntent.setPackage("com.android.chrome")

        if (mapIntent.resolveActivity(packageManager) != null) {
            startActivity(mapIntent)
        } else {
            startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(URL_GITHUB)))
        }
    }

    // set icon favorite
    @SuppressLint("UseCompatLoadingForDrawables")
    private fun setStatusFavorites(statusFavorites: Boolean) {
        if (statusFavorites) {
            fab_favorites.setImageDrawable(resources.getDrawable(R.drawable.ic_favorite, theme))
        } else {
            fab_favorites.setImageDrawable(
                resources.getDrawable(
                    R.drawable.ic_favorite_blank,
                    theme
                )
            )
        }
    }

    // loading progressbar
    private fun showLoading(state: Boolean) {
        if (state) {
            progressBar.visibility = View.VISIBLE
        } else {
            progressBar.visibility = View.GONE
        }
    }

    // show snack bar message
    private fun showSnackbarMessage(message: String) {
        Snackbar.make(cl_user, message, Snackbar.LENGTH_SHORT).show()
    }


}