package com.lxy.simplenote;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.TextView;

public class NoteActivity extends Activity{
	
//	private SharedPreferences preferences;
	private int widgetId;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//Constant.log("onCreate");
		setContentView(R.layout.note_activity_layout);
//		preferences = getSharedPreferences(Constant.PREFERENCES_NAME, 0);
		Intent i = getIntent();
		widgetId = i.getIntExtra(Constant.KEY_WIDGET_ID, -1);
		Constant.log("onCreate: widgetId = " + widgetId + ", intent = " + i);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		//Constant.log("onDestroy");
	}

	@Override
	protected void onPause() {
		super.onPause();
		//Constant.log("onPause");
		writeNote();
	}

	@Override
	protected void onResume() {
		super.onResume();
		//Constant.log("onResume");
		
		//Read note
		readNote();
	}
	
	/**
	 * 读取已经存储的笔记和标题用以显示
	 */
	private void readNote(){
		SharedPreferences preferences = getSharedPreferences(Constant.PREFERENCES_NAME, 0);
		String title = preferences.getString(Constant.KEY_NOTE_TITLE + widgetId, "");
		if(!TextUtils.isEmpty(title)){
			TextView titleView = (TextView) findViewById(R.id.noteTitle);
			titleView.setText(title);
		}
		
		String content = preferences.getString(Constant.KEY_NOTE_CONTENT + widgetId, "");
		if(!TextUtils.isEmpty(content)){
			TextView contentView = (TextView) findViewById(R.id.noteContent);
			contentView.setText(content);
			contentView.requestFocus();
		}
	}
	
	/**
	 * 存储现在笔记和标题
	 */
	private void writeNote(){
		SharedPreferences preferences = getSharedPreferences(Constant.PREFERENCES_NAME, 0);
		Editor editor = preferences.edit();
		TextView titleView = (TextView) findViewById(R.id.noteTitle);
		
		String editTitle = titleView.getText().toString().trim();
		String savedTitle = preferences.getString(Constant.KEY_NOTE_TITLE + widgetId, "");
		if((!TextUtils.isEmpty(editTitle) || !TextUtils.isEmpty(savedTitle)) 
				/*&& !editTitle.equals(savedTitle)*/){	//修改系统语言后，widget不能更新
			editor.putString(Constant.KEY_NOTE_TITLE + widgetId, editTitle);
			NoteAppWidgetProvider.setSummary(widgetId, editTitle);
		}
		
		TextView contentView = (TextView) findViewById(R.id.noteContent);
		String editContent = contentView.getText().toString().trim();
		String savedContent = preferences.getString(Constant.KEY_NOTE_CONTENT + widgetId, "");
		if((!TextUtils.isEmpty(editContent) || !TextUtils.isEmpty(savedContent))
				&& !editContent.equals(savedContent)){
			editor.putString(Constant.KEY_NOTE_CONTENT + widgetId, editContent);
		}
		editor.commit();
	}
	
}
