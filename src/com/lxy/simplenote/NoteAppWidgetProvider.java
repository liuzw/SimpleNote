package com.lxy.simplenote;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

public class NoteAppWidgetProvider extends AppWidgetProvider{
private static RemoteViews mRemoteViews;
	
	/**
	 * 每删除一次窗口小部件就调用一次
	 */
	@Override
	public void onDeleted(Context context, int[] appWidgetIds) {
		super.onDeleted(context, appWidgetIds);
		Constant.log("onDeleted");
	}

	/**
	 * 当最后一个该窗口小部件删除时调用该方法，注意是最后一个
	 */
	@Override
	public void onDisabled(Context context) {
		super.onDisabled(context);
		Constant.log("onDisabled");
	}

	/**
	 * 当该窗口小部件第一次添加到桌面时调用该方法，可添加多次但只第一次调用
	 */
	@Override 
	public void onEnabled(Context context) {
		super.onEnabled(context);
		Constant.log("onEnabled");
	}

	/**
	 * 接收窗口小部件点击时发送的广播
	 */
	@Override 
	public void onReceive(Context context, Intent intent) {
		super.onReceive(context, intent);
		Constant.log("onReceive : action = " + intent.getAction());
		
		if(Constant.ACTION_SIMPLE_NOTE_WIGET_CLICK.equals(intent.getAction())){
			//这里是点击“窗口小部件”要执行的内容
			Intent i = new Intent(Constant.ACTION_SIMPLE_NOTE_START_ACTIVITY);  
			i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
			i.setClassName(Constant.PACKAGE_NAME, Constant.ACTIVITY_NAME);  
			context.startActivity(i); 
		}
	}

	/**
	 * 每次窗口小部件被点击更新都调用一次该方法
	 */
	@Override 
	public void onUpdate(Context context, AppWidgetManager appWidgetManager,
			int[] appWidgetIds) {
		super.onUpdate(context, appWidgetManager, appWidgetIds);
		Constant.log("onUpdate");

		final int counter = appWidgetIds.length;
		Constant.log("counter = " + counter);
		for (int i = 0; i < counter; i++) {
			int appWidgetId = appWidgetIds[i];
			onWidgetUpdate(context, appWidgetManager, appWidgetId);
		}
		
	}
	
	/**
	 * 窗口小部件更新
	 * @param context
	 * @param appWidgeManger
	 * @param appWidgetId
	 */
	private void onWidgetUpdate(Context context,
			AppWidgetManager appWidgeManger, int appWidgetId) {
		
		Constant.log("appWidgetId = " + appWidgetId);
		mRemoteViews = new RemoteViews(context.getPackageName(), R.layout.note_widget_layout);
		
		//"窗口小部件"点击事件发送的Intent广播
		Intent intentClick = new Intent();
		intentClick.setAction(Constant.ACTION_SIMPLE_NOTE_WIGET_CLICK);
		PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intentClick, 0);
		mRemoteViews.setOnClickPendingIntent(R.id.imageview_widget, pendingIntent);
		appWidgeManger.updateAppWidget(appWidgetId, mRemoteViews);
	}
}
