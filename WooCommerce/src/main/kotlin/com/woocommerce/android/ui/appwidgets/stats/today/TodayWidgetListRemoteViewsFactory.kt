package com.woocommerce.android.ui.appwidgets.stats.today

import android.appwidget.AppWidgetManager
import android.content.Context
import android.content.Intent
import android.widget.RemoteViews
import android.widget.RemoteViewsService.RemoteViewsFactory
import com.woocommerce.android.R
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.android.EntryPointAccessors
import dagger.hilt.components.SingletonComponent

/**
 * Class extends [RemoteViewsFactory] and acts as an interface for the current day stats widget ListView adapter
 */
class TodayWidgetListRemoteViewsFactory(
    val context: Context,
    intent: Intent
) : RemoteViewsFactory {

    @EntryPoint
    @InstallIn(SingletonComponent::class)
    interface TodayWidgetListEntryPoint {
        fun viewModel(): TodayWidgetListViewModel
        fun widgetUpdater(): TodayWidgetUpdater
    }

    lateinit var viewModel: TodayWidgetListViewModel
    lateinit var widgetUpdater: TodayWidgetUpdater

    private val appWidgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, -1)

    override fun onCreate() {
        val hiltEntryPoint =
            EntryPointAccessors.fromApplication(context, TodayWidgetListEntryPoint::class.java)
        viewModel = hiltEntryPoint.viewModel()
        widgetUpdater = hiltEntryPoint.widgetUpdater()

        viewModel.start(appWidgetId)
    }

    /**
     * [RemoteViewsFactory] provides a custom loading view. Currently not used
     * so a default loading view will be displayed
     */
    override fun getLoadingView(): RemoteViews? = null

    /**
     * This is triggered when [AppWidgetManager] notifyAppWidgetViewDataChanged is called
     * on the list view corresponding to this factory. Heavy lifting such as processing an image
     * or fetching data from the api takes place here. The data is fetched synchronously.
     * The widget will remain in its current state while work is being done here, and a loading view
     * will show up in lieu of the actual contents in the interim
     * */
    override fun onDataSetChanged() {
        viewModel.onDataSetChanged { appWidgetId ->
            widgetUpdater.updateAppWidget(
                context,
                appWidgetId = appWidgetId
            )
        }
    }

    @Suppress("EmptyFunctionBlock")
    override fun onDestroy() { }

    override fun hasStableIds(): Boolean = true

    override fun getViewTypeCount(): Int = 1

    override fun getCount(): Int {
        return viewModel.data.size
    }

    override fun getItemId(position: Int): Long {
        return viewModel.data[position].key.hashCode().toLong()
    }

    override fun getViewAt(position: Int): RemoteViews {
        // position will always range from 0 to getCount() - 1.
        // We construct a remote views item based on our widget item xml file, and set the
        // text based on the position.
        val uiModel = viewModel.data[position]
        val rv = RemoteViews(context.packageName, uiModel.layout)

        rv.setTextViewText(R.id.list_item_title, uiModel.key)
        rv.setTextViewText(R.id.list_item_value, uiModel.value)

        val intent = Intent()
        rv.setOnClickFillInIntent(R.id.container, intent)
        return rv
    }
}
