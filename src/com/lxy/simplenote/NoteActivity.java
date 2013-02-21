package com.lxy.simplenote;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.TextView;

public class NoteActivity extends Activity{
	
	private SharedPreferences mPreferences;
	private int widgetId;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//Constant.log("onCreate");
		setContentView(R.layout.note_activity_layout);
		mPreferences = getSharedPreferences(Constant.PREFERENCES_NAME, 0);
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
		if(mPreferences == null) return;
		String title = mPreferences.getString(Constant.KEY_NOTE_TITLE + widgetId, "");
		if(!TextUtils.isEmpty(title)){
			TextView titleView = (TextView) findViewById(R.id.noteTitle);
			titleView.setText(title);
		}
		
		String content = mPreferences.getString(Constant.KEY_NOTE_CONTENT + widgetId, "");
		if(!TextUtils.isEmpty(content)){
			TextView contentView = (TextView) findViewById(R.id.noteContent);
			contentView.setText(content);
		}
	}
	
	/**
	 * 存储现在笔记和标题
	 */
	private void writeNote(){
		if(mPreferences == null) return;
		Editor editor = mPreferences.edit();
		TextView titleView = (TextView) findViewById(R.id.noteTitle);
		
		String title = titleView.getText().toString().trim();
		if(!TextUtils.isEmpty(title)){
			editor.putString(Constant.KEY_NOTE_TITLE + widgetId, title);
			NoteAppWidgetProvider.setSummary(widgetId, title);
		}
		
		TextView contentView = (TextView) findViewById(R.id.noteContent);
		String content = contentView.getText().toString().trim();
		if(!TextUtils.isEmpty(content)){
			editor.putString(Constant.KEY_NOTE_CONTENT + widgetId, content);
		}
		editor.commit();
	}
	
}
