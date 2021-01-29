package com.example.searchgithubuser.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.searchgithubuser.R
import com.example.searchgithubuser.adapter.GithubUserAdapter
import com.example.searchgithubuser.view_model.MainViewModel
import kotlinx.android.synthetic.main.fragment_follower.*

class FollowerFragment : Fragment() {

    private lateinit var githubUserAdapter: GithubUserAdapter
    private lateinit var mainViewModel: MainViewModel

    companion object {
        private val ARG_USERNAME = "username"

        fun newInstance(username: String): FollowerFragment {
            val fragment =
                FollowerFragment()
            val bundle = Bundle()
            bundle.putString(ARG_USERNAME, username)
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_follower, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        githubUserAdapter = GithubUserAdapter(context)
        githubUserAdapter.notifyDataSetChanged()

        rv_follower_githubuser.layoutManager = LinearLayoutManager(context)
        rv_follower_githubuser.adapter = githubUserAdapter

        rv_follower_githubuser.setHasFixedSize(true)

        // Menyambungkan kelas MainViewModel dengan Fragment
        mainViewModel = ViewModelProvider(
            this,
            ViewModelProvider.NewInstanceFactory()
        ).get(MainViewModel::class.java)

        val username = arguments?.getString(ARG_USERNAME)

        // set data
        mainViewModel.setFollowerUser(username.toString(), progressBar)

        // mendapatkan value dari LiveData yang ada pada kelas ViewModel
        mainViewModel.getGithubUser().observe(viewLifecycleOwner, Observer { listFollowerUser ->
            if (listFollowerUser != null) {
                githubUserAdapter.setData(listFollowerUser)
                showLoading(false)
            }
        })
    }

    private fun showLoading(state: Boolean) {
        if (state) {
            progressBar.visibility = View.VISIBLE
        } else {
            progressBar.visibility = View.GONE
        }
    }

}