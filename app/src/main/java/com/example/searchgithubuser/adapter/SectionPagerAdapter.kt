package com.example.searchgithubuser.adapter

import android.content.Context
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.example.searchgithubuser.R
import com.example.searchgithubuser.fragment.FollowerFragment
import com.example.searchgithubuser.fragment.FollowingFragment

class SectionPagerAdapter(private val mContex: Context, fragmentManager: FragmentManager) :
    FragmentPagerAdapter(fragmentManager, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    var userName: String? = null

    @StringRes
    private val tabTitle = intArrayOf(
        R.string.tab_follower,
        R.string.tab_following
    )

    override fun getItem(position: Int): Fragment {
        var fragment: Fragment? = null
        when (position) {
            0 -> fragment = FollowerFragment.newInstance(userName.toString())
            1 -> fragment = FollowingFragment.newInstance(userName.toString())
        }
        return fragment as Fragment
    }

    override fun getCount(): Int {
        return 2
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return mContex.resources.getString(tabTitle[position])
    }
}