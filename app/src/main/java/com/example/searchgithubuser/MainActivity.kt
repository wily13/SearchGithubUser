package com.example.searchgithubuser

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.searchgithubuser.adapter.GithubUserAdapter
import com.example.searchgithubuser.model.GithubUser
import com.example.searchgithubuser.view_model.MainViewModel
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    private lateinit var githubUserAdapter: GithubUserAdapter
    private lateinit var mainViewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (supportActionBar != null) {
            supportActionBar!!.title = resources.getString(R.string.title_activity_main_user)
        }

        githubUserAdapter =
            GithubUserAdapter(this)
        githubUserAdapter.notifyDataSetChanged()

        rv_githubuser.layoutManager = LinearLayoutManager(this)
        rv_githubuser.adapter = githubUserAdapter

        // Menyambungkan kelas MainViewModel dengan MainActivity
        mainViewModel = ViewModelProvider(
            this,
            ViewModelProvider.NewInstanceFactory()
        ).get(MainViewModel::class.java)

        rv_githubuser.setHasFixedSize(true)

        // mendapatkan value dari LiveData yang ada pada kelas ViewModel
        mainViewModel.getGithubUser().observe(this, Observer { listGithubUsers ->
            if (listGithubUsers != null) {
                githubUserAdapter.setData(listGithubUsers)
                showLoading(false)

                img_no_data.visibility = View.GONE
                tv_no_data.visibility = View.GONE
            }
        })

        // click on item list
        githubUserAdapter.setOnItemClickCallBack(object : GithubUserAdapter.OnItemClickCallBack {
            override fun onItemClicked(data: GithubUser) {
                showSelectedUser(data.username.toString())
            }
        })

        img_no_data.visibility = View.VISIBLE
        tv_no_data.visibility = View.VISIBLE

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.option_menu, menu)

        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        val searchView = menu.findItem(R.id.search).actionView as SearchView

        searchView.setSearchableInfo(searchManager.getSearchableInfo(componentName))
        searchView.queryHint = resources.getString(R.string.search_hint)
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {

            override fun onQueryTextSubmit(query: String): Boolean {
                // clear data before
                githubUserAdapter.clearData()
                // set data from API
                mainViewModel.setGithubUser(query.trim(), progressBar, this@MainActivity)

                //tutup keyboard ketika tombol diklik
                val methodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                methodManager.hideSoftInputFromWindow(searchView.windowToken, 0)

                searchView.clearFocus()

                img_no_data.visibility = View.GONE
                tv_no_data.visibility = View.GONE

                return true
            }

            override fun onQueryTextChange(newText: String): Boolean {
                return false
            }
        })

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId){
            R.id.favorites -> {
                val intent = Intent(this@MainActivity, FavoriteUserActivity::class.java)
                startActivity(intent)
            }
            R.id.setting -> {
                val intent = Intent(this@MainActivity, SettingActivity::class.java)
                startActivity(intent)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun showSelectedUser(username: String) {
        val detailUserIntent =
            Intent(this@MainActivity, DetailUsersActivity::class.java)
        detailUserIntent.putExtra(DetailUsersActivity.EXTRA_USER, username)
        detailUserIntent.putExtra(DetailUsersActivity.EXTRA_ID, 0)
        detailUserIntent.putExtra(DetailUsersActivity.EXTRA_POSITION, 0)
        startActivity(detailUserIntent)
    }

    private fun showLoading(state: Boolean) {
        if (state) {
            progressBar.visibility = View.VISIBLE
        } else {
            progressBar.visibility = View.GONE
        }
    }


}