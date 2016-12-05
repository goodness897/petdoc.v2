package com.compet.petdoc;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.Toast;

import com.compet.petdoc.util.Constants;

/**
 * Created by Mu on 2016-12-05.
 */

public class MyCustomWidget extends AppWidgetProvider {

    private static final String TAG = MyCustomWidget.class.getSimpleName();

    private Context context;

    @Override
    public void onEnabled(Context context) {
        Log.i(TAG, "======================= onEnabled() =======================");
        super.onEnabled(context);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        super.onUpdate(context, appWidgetManager, appWidgetIds);
        Log.i(TAG, "======================= onUpdate() =======================");
        this.context = context;
        for (int i = 0; i < appWidgetIds.length; i++) {
            int appWidgetId = appWidgetIds[i];
            RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.view_widget);
            appWidgetManager.updateAppWidget(appWidgetId, views);
        }
    }

    @Override
    public void onDeleted(Context context, int[] appWidgetIds) {
        Log.i(TAG, "======================= onDeleted() =======================");
        super.onDeleted(context, appWidgetIds);
    }

    @Override
    public void onDisabled(Context context) {
        Log.i(TAG, "======================= onDisabled() =======================");
        super.onDisabled(context);
    }

    public void initUI(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        Log.i(TAG, "======================= initUI() =======================");
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.view_widget);

        Intent eventIntent = new Intent(Constants.ACTION_EVENT);

        Intent activityIntent = new Intent(Constants.ACTION_ACTIVITY);

        PendingIntent eventPIntent = PendingIntent.getBroadcast(context, 0, eventIntent, 0);

        PendingIntent activityPIntent = PendingIntent.getBroadcast(context, 0, activityIntent, 0);

        views.setOnClickPendingIntent(R.id.btn_event, eventPIntent);
        views.setOnClickPendingIntent(R.id.btn_activity, activityPIntent);

        for (int appWidgetId : appWidgetIds) {
            appWidgetManager.updateAppWidget(appWidgetId, views);
        }
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);

        String action = intent.getAction();
        Log.d(TAG, "onReceive() action = " + action);

        if (AppWidgetManager.ACTION_APPWIDGET_ENABLED.equals(action)) {

        } else if (AppWidgetManager.ACTION_APPWIDGET_UPDATE.equals(action)) {
            AppWidgetManager manager = AppWidgetManager.getInstance(context);
            initUI(context, manager, manager.getAppWidgetIds(new ComponentName(context, getClass())));
        } else if (AppWidgetManager.ACTION_APPWIDGET_DELETED.equals(action)) {

        } else if (AppWidgetManager.ACTION_APPWIDGET_DISABLED.equals(action)) {

        } // Custom Recevier
        else if (Constants.ACTION_EVENT.equals(action)) {
            Toast.makeText(context, "Receiver 수신 완료!!.", Toast.LENGTH_SHORT).show();
        } else if (Constants.ACTION_ACTIVITY.equals(action)) {
            callActivity(context);
        }
    }

    private void callActivity(Context context) {
        Log.d(TAG, "callActivity()");
        Intent intent = new Intent(Constants.ACTION_ACTIVITY);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

}
