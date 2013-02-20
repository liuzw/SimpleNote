package com.lxy.simplenote;

import android.util.Log;

public class Constant {

	//Log part
	public static final String TAG = "note";
	private static final boolean DEBUG = true;
	public static void logE(String msg){
		Log.e(TAG, msg);
	}
	public static void log(String msg){
		if(DEBUG){
			Log.d(TAG, msg);
		}
	}
	
	//
	public static final String ACTION_SIMPLE_NOTE_WIGET_CLICK = "com.lxy.simplenote.action.WIGET_CLICK";
	public static final String ACTION_SIMPLE_NOTE_START_ACTIVITY = "com.lxy.simplenote.action.START_ACTIVITY";
	
	public static final String PACKAGE_NAME = "com.lxy.simplenote";
	public static final String ACTIVITY_NAME = "com.lxy.simplenote.NoteActivity";
	
	//preferences
	public static final String PREFERENCES_NAME = "com.lxy.note";
	public static final String KEY_NOTE_TITLE = "note_title";
	public static final String KEY_NOTE_CONTENT = "note_content";
}
