package com.example.searchgithubuser

import android.content.Intent
import android.database.ContentObserver
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.HandlerThread
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.searchgithubuser.adapter.GithubUserAdapter
import com.example.searchgithubuser.db.DatabaseContract.UserColumn.Companion.CONTENT_URI
import com.example.searchgithubuser.helper.MappingHelper
import com.example.searchgithubuser.model.GithubUser
import com.example.searchgithubuser.view_model.MainViewModel
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_favorite_user.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class FavoriteUserActivity : AppCompatActivity() {

    private lateinit var githubUserAdapter: GithubUserAdapter
    private lateinit var mainViewModel: MainViewModel
    private lateinit var uriWithId: Uri

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_favorite_user)

        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
        }

        supportActionBar?.title = getString(R.string.title_activity_favorite)

        // Menyambungkan kelas MainViewModel dengan MainActivity
        mainViewModel = ViewModelProvider(
            this,
            ViewModelProvider.NewInstanceFactory()
        ).get(MainViewModel::class.java)

        rv_githubuser_favorite.layoutManager = LinearLayoutManager(this)
        rv_githubuser_favorite.setHasFixedSize(true)
        githubUserAdapter = GithubUserAdapter(this)
        rv_githubuser_favorite.adapter = githubUserAdapter

        // mendapatkan value dari LiveData yang ada pada kelas ViewModel
        mainViewModel.getGithubUser().observe(this, Observer { listGithubUsers ->
            if (listGithubUsers != null) {
                githubUserAdapter.setData(listGithubUsers)
            }
        })

        // content resolver untuk membuat content observer
        val handlerThread = HandlerThread("DataObserver")
        handlerThread.start()
        val handler = Handler(handlerThread.looper)

        val myObserver = object : ContentObserver(handler) {
            override fun onChange(self: Boolean) {
                loadFavoriteUserSync()
            }
        }

        // Membuat registerContentObserver.
        contentResolver.registerContentObserver(CONTENT_URI, true, myObserver)

        // load data favorite user
        loadFavoriteUserSync()

        // click on item list
        githubUserAdapter.setOnItemClickCallBack(object : GithubUserAdapter.OnItemClickCallBack {
            override fun onItemClicked(data: GithubUser) {
                showSelectedUser(data.username.toString(), data.id)
            }
        })

        // click image delete
        githubUserAdapter.setOnDeleteItemClickCallBack(object :
            GithubUserAdapter.OnDeleteItemClickCallBack {
            override fun onItemDeleteClicked(data: GithubUser) {
                showAlertDialog(data.id)
            }

        })
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.option_menu, menu)

        val favoriteMenu = menu.findItem(R.id.favorites)
        val searchMenu = menu.findItem(R.id.search)

        favoriteMenu.isVisible = false
        searchMenu.isVisible = false

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.setting -> {
                val intent = Intent(this@FavoriteUserActivity, SettingActivity::class.java)
                startActivity(intent)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun loadFavoriteUserSync() {
        GlobalScope.launch(Dispatchers.Main) {
            showLoading(true)
            val deferredUsers = async(Dispatchers.IO) {
                // Uri yang di dapatkan digunakan untuk ambil data dari provider
                // content://com.example.searchgithubuser/githubuser
                val cursor = contentResolver?.query(CONTENT_URI, null, null, null, null)
                MappingHelper.mapCursorToArrayList(cursor)
            }
            val users = deferredUsers.await()
            if (users.size > 0) {
                githubUserAdapter.setData(users)
            } else {
                githubUserAdapter.listGithubUser = ArrayList()
                showSnackbarMessage("Tidak ada data.")

                img_no_data.visibility = View.VISIBLE
                tv_no_data.visibility = View.VISIBLE

            }
            showLoading(false)
        }
    }

    private fun showSelectedUser(username: String, id: Int) {
        val detailUserIntent =
            Intent(this@FavoriteUserActivity, DetailUsersActivity::class.java)
        detailUserIntent.putExtra(DetailUsersActivity.EXTRA_USER, username)
        detailUserIntent.putExtra(DetailUsersActivity.EXTRA_ID, id)
        startActivityForResult(detailUserIntent, DetailUsersActivity.REQUEST_UPDATE)
    }

    private fun showLoading(state: Boolean) {
        if (state) {
            progressBar.visibility = View.VISIBLE
        } else {
            progressBar.visibility = View.INVISIBLE
        }
    }

    private fun showSnackbarMessage(message: String) {
        Snackbar.make(rv_githubuser_favorite, message, Snackbar.LENGTH_SHORT).show()
    }

    private fun showAlertDialog(dataId: Int) {

        val dialogMessage: String = getString(R.string.dialog_message_delete)
        val dialogTitle: String = getString(R.string.dialog_title_delete)

        val alertDialogBuilder = AlertDialog.Builder(this)
        alertDialogBuilder.setTitle(dialogTitle)
        alertDialogBuilder
            .setMessage(dialogMessage)
            .setCancelable(false)
            .setPositiveButton(getString(R.string.dialod_button_delete)) { dialog, id ->

                // Uri yang di dapatkan akan digunakan untuk hapus data dari provider
                // content://com.example.searchgithubuser/githubuser/id
                uriWithId = Uri.parse(CONTENT_URI.toString() + "/" + dataId)
                contentResolver.delete(uriWithId, null, null)
                showSnackbarMessage(getString(R.string.msg_delete_success))
            }
            .setNegativeButton(getString(R.string.dialog_button_cancel)) { dialog, id -> dialog.cancel() }
        val alertDialog = alertDialogBuilder.create()
        alertDialog.show()
    }

}