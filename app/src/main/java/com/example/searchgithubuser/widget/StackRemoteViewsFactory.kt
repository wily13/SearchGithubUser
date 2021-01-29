package com.example.searchgithubuser.widget

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.os.Binder
import android.widget.RemoteViews
import android.widget.RemoteViewsService
import androidx.core.os.bundleOf
import com.example.searchgithubuser.R
import com.example.searchgithubuser.db.DatabaseContract
import com.example.searchgithubuser.db.DatabaseContract.UserColumn.Companion.CONTENT_URI
import com.example.searchgithubuser.helper.BitmapUtils
import java.util.*

class StackRemoteViewsFactory(private val mContext: Context) : RemoteViewsService.RemoteViewsFactory {

    private val mWidgetItems = ArrayList<Bitmap>()

    override fun onCreate() {

    }

    override fun onDataSetChanged() {
        val identityToken = Binder.clearCallingIdentity()
        // query ke database
        val cursor = mContext.contentResolver.query(CONTENT_URI, null, null, null, null)
        if (cursor != null){

            cursor.apply {
                while (moveToNext()){
                    val image = getBlob(getColumnIndexOrThrow(DatabaseContract.UserColumn.AVATAR))

                    val imageAvatar = BitmapUtils.getImage(image)
                    imageAvatar?.let { mWidgetItems.add(it) }
                }
            }

            cursor.close()
        }

        Binder.restoreCallingIdentity(identityToken)
    }

    override fun onDestroy() {

    }

    override fun getCount(): Int = mWidgetItems.size

    override fun getViewAt(position: Int): RemoteViews {
        val rv = RemoteViews(mContext.packageName,
            R.layout.widget_item
        )
        rv.setImageViewBitmap(R.id.imageView, mWidgetItems[position])

        val extras = bundleOf(
            ImageBannerWidget.EXTRA_ITEM to position
        )
        val fillInIntent = Intent()
        fillInIntent.putExtras(extras)

        rv.setOnClickFillInIntent(R.id.imageView, fillInIntent)
        return rv
    }

    override fun getLoadingView(): RemoteViews? = null

    override fun getViewTypeCount(): Int = 1

    override fun getItemId(p0: Int): Long = 0

    override fun hasStableIds(): Boolean = false

}