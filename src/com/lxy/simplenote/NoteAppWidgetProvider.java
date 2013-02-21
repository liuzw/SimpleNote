package com.lxy.simplenote;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

public class NoteAppWidgetProvider extends AppWidgetProvider{
	
	private static RemoteViews mRemoteViews;
	private static AppWidgetManager mAppWidgeManger;
	
	/**
	 * 每删除一次窗口小部件就调用一次
	 */
	@Override
	public void onDeleted(Context context, int[] appWidgetIds) {
		super.onDeleted(context, appWidgetIds);
		Constant.log("onDeleted --- appWidgetIds.length = " + appWidgetIds.length);
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
	 * 接收窗口小部件发送的广播
	 */
	@Override 
	public void onReceive(Context context, Intent intent) {
		super.onReceive(context, intent);
		Constant.log("onReceive : intent = " + intent);
		
		if(Constant.ACTION_SIMPLE_NOTE_WIGET_CLICK.equals(intent.getAction())){
			//这里是点击“窗口小部件”要执行的内容
			
			//获得是哪一个小部件被点击,并告诉显示界面。
			int widgetId = intent.getIntExtra(Constant.KEY_WIDGET_ID, 0);
			Constant.log("onReceive --- 一个窗口小部被点击： id = " + widgetId);
			Intent i = new Intent(Constant.ACTION_SIMPLE_NOTE_START_ACTIVITY); 
			i.putExtra(Constant.KEY_WIDGET_ID, widgetId);
			i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
			i.setClassName(Constant.PACKAGE_NAME, Constant.ACTIVITY_NAME);  
			context.startActivity(i); 
		}else if("android.appwidget.action.APPWIDGET_UPDATE".equals(intent.getAction())){
			//这里是新添加一个窗口小部件要执行的内容
			Constant.log("onReceive --- 新添加一个窗口小部");

		}else if("android.appwidget.action.APPWIDGET_ENABLED".equals(intent.getAction())){
			//这里是添加第一个窗口小部件要执行的内容 -- 也有update
			Constant.log("onReceive --- 加第一个窗口小部件");
			
		}else if("android.appwidget.action.APPWIDGET_DELETED".equals(intent.getAction())){
			//这里是删除一个窗口小部件要执行的内容
			Constant.log("onReceive --- 删除一个窗口小部件");
			
		}else if("android.appwidget.action.APPWIDGET_DISABLED".equals(intent.getAction())){
			//这里是添删除最后一个窗口小部件要执行的内容 -- 也有deleted
			Constant.log("onReceive --- 删除最后一个窗口小部件");
			
		}
	}

	/**
	 * 每次窗口小部件被添加都调用一次该方法
	 */
	@Override 
	public void onUpdate(Context context, AppWidgetManager appWidgetManager,
			int[] appWidgetIds) {
		super.onUpdate(context, appWidgetManager, appWidgetIds);
		Constant.log("onUpdate --- appWidgetIds.length = " + appWidgetIds.length);

		final int counter = appWidgetIds.length;
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
		
		Constant.log("onWidgetUpdate: appWidgetId = " + appWidgetId);
		mAppWidgeManger = appWidgeManger;
		mRemoteViews = new RemoteViews(context.getPackageName(), R.layout.note_widget_layout);
		
		//"窗口小部件"点击事件发送的Intent广播
		Intent intentClick = new Intent();
		intentClick.setAction(Constant.ACTION_SIMPLE_NOTE_WIGET_CLICK);
		intentClick.putExtra(Constant.KEY_WIDGET_ID, appWidgetId);
		Constant.log("onWidgetUpdate: intentClick = " + intentClick);
		PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intentClick, appWidgetId);
		mRemoteViews.setOnClickPendingIntent(R.id.imageview_widget, pendingIntent);
		appWidgeManger.updateAppWidget(appWidgetId, mRemoteViews);
	}
	
	/**
	 * 更新窗口小部件上显示的文字
	 * @param summary
	 */
	public static void setSummary(int widgetId, String summary){
		Constant.log("setSummary: summary = " + summary + ", mRemoteViews = " + mRemoteViews);
		if(mRemoteViews == null) return;
		mRemoteViews.setTextViewText(R.id.textview_summary, summary);
		mAppWidgeManger.updateAppWidget(widgetId, mRemoteViews);
	}
}
