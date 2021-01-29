package com.example.searchgithubuser.widget

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.content.Intent
import android.widget.RemoteViews
import androidx.core.net.toUri
import com.example.searchgithubuser.FavoriteUserActivity
import com.example.searchgithubuser.R

/**
 * Implementation of App Widget functionality.
 */
class ImageBannerWidget : AppWidgetProvider() {

    companion object {

        private const val TOAST_ACTION = "com.example.TOAST_ACTION"
        const val EXTRA_ITEM = "com.example.EXTRA_ITEM"

        // Update widget berdasarkan id widget-nya di home screen
        private fun updateAppWidget(context: Context, appWidgetManager: AppWidgetManager, appWidgetId: Int) {
            // memasang RemoteAdapter ke dalam widget dengan menggunakan obyek Intent dan nilai id dari RemoteView
            val intent = Intent(context, StackWidgetService::class.java)
            intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId)
            intent.data = intent.toUri(Intent.URI_INTENT_SCHEME).toUri()

            val views = RemoteViews(context.packageName,
                R.layout.image_banner_widget
            )
            views.setRemoteAdapter(R.id.stack_view, intent)
            views.setEmptyView(
                R.id.stack_view,
                R.id.empty_view
            )

            // menjalankan getBroadcast() untuk melakukan proses broadcast ketika salah satu widget ditekan.
            val toastIntent = Intent(context, ImageBannerWidget::class.java)
            toastIntent.action =
                TOAST_ACTION
            toastIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId)
            intent.data = intent.toUri(Intent.URI_INTENT_SCHEME).toUri()
            val toastPendingIntent = PendingIntent.getBroadcast(context, 0, toastIntent, PendingIntent.FLAG_UPDATE_CURRENT)
            views.setPendingIntentTemplate(R.id.stack_view, toastPendingIntent)

            appWidgetManager.updateAppWidget(appWidgetId, views)
        }
    }

    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray
    ) {
        // There may be multiple widgets active, so update all of them
        for (appWidgetId in appWidgetIds) {
            updateAppWidget(
                context,
                appWidgetManager,
                appWidgetId
            )
        }
    }

    override fun onReceive(context: Context, intent: Intent) {
        super.onReceive(context, intent)
        if (intent.action != null) {
            if (intent.action == TOAST_ACTION) {
                // memanggil favorite activity
                val intentFavorite = Intent(context, FavoriteUserActivity::class.java)
                context.startActivity(intentFavorite)
            }
        }
    }

}