package com.banhong.APPBox;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RemoteViews;
import android.widget.TextView;

public class Lrcwidget extends AppWidgetProvider {
	
	@Override
	public void onEnabled(Context context) {
		// TODO Auto-generated method stub
		super.onEnabled(context);
		
		
	}
	
	@Override
	public void onUpdate(Context context, AppWidgetManager appWidgetManager,
			int[] appWidgetIds) {
		// TODO Auto-generated method stub
		super.onUpdate(context, appWidgetManager, appWidgetIds);
		RemoteViews remoteview = new RemoteViews(context.getPackageName(), R.layout.song_lrc_widget_layout);
		remoteview.setTextViewText(R.id.lrc_widget_text, context.getString(R.string.no_lrc).toString());
		appWidgetManager.updateAppWidget(appWidgetIds, remoteview);
	}
	
	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		super.onReceive(context, intent);
	}
}
