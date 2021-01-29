package com.example.searchgithubuser.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.searchgithubuser.R
import com.example.searchgithubuser.helper.BitmapUtils
import com.example.searchgithubuser.model.GithubUser
import kotlinx.android.synthetic.main.item_row_user.view.*


class GithubUserAdapter(val context: Context?) : RecyclerView.Adapter<GithubUserAdapter.GithubUserViewHolder>() {

    private var onItemClickCallBack: OnItemClickCallBack? = null
    private var onDeleteItemClickCallBack: OnDeleteItemClickCallBack? = null

    var listGithubUser = ArrayList<GithubUser>()
        set(listNotes) {
            this.listGithubUser.clear()
            this.listGithubUser.addAll(listNotes)
            notifyDataSetChanged()
        }

    fun setOnItemClickCallBack(onItemClickCallBack: OnItemClickCallBack) {
        this.onItemClickCallBack = onItemClickCallBack
    }

    fun setOnDeleteItemClickCallBack(OnDeleteItemClickCallBack: OnDeleteItemClickCallBack) {
        this.onDeleteItemClickCallBack = OnDeleteItemClickCallBack
    }

    fun setData(items: ArrayList<GithubUser>) {
        listGithubUser.clear()
        listGithubUser.addAll(items)
        notifyDataSetChanged()
    }

    fun clearData() {
        val size: Int = listGithubUser.size
        listGithubUser.clear()
        notifyItemRangeRemoved(0, size)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): GithubUserViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_row_user, parent, false)
        return GithubUserViewHolder(view)
    }

    override fun getItemCount(): Int {
        return listGithubUser.size
    }

    override fun onBindViewHolder(holder: GithubUserViewHolder, position: Int) {
        holder.bind(listGithubUser[position])
    }

    inner class GithubUserViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(githubUser: GithubUser) {
            with(itemView) {
                tv_github_url.text = githubUser.username
                tv_link_github.text = githubUser.html_url

                // set image to search or favorite
                if (githubUser.avatar != null) {
                    Glide.with(itemView.context)
                        .load(githubUser.avatar)
                        .into(itemView.img_avatar_github)

                } else {

                    val imageAvatar = githubUser.image_blob?.let { BitmapUtils.getImage(it) }

                    Glide.with(itemView.context)
                        .asBitmap()
                        .load(imageAvatar)
                        .into(itemView.img_avatar_github)

                    img_delete.visibility = View.VISIBLE

                    img_delete.setOnClickListener {
                        onDeleteItemClickCallBack?.onItemDeleteClicked(githubUser)
                    }
                }

                // item click call back
                itemView.setOnClickListener { onItemClickCallBack?.onItemClicked(githubUser) }
            }
        }
    }

    interface OnItemClickCallBack {
        fun onItemClicked(data: GithubUser)
    }

    interface OnDeleteItemClickCallBack {
        fun onItemDeleteClicked(data: GithubUser)
    }
}