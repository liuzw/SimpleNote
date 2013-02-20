package com.lxy.simplenote;

import android.app.Activity;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.TextView;

public class NoteActivity extends Activity{
	
	private SharedPreferences mPreferences;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Constant.log("onCreate");
		setContentView(R.layout.note_activity_layout);
		mPreferences = getSharedPreferences(Constant.PREFERENCES_NAME, 0);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		Constant.log("onDestroy");
	}

	@Override
	protected void onPause() {
		super.onPause();
		Constant.log("onPause");
		writeNote();
	}

	@Override
	protected void onResume() {
		super.onResume();
		Constant.log("onResume");
		
		//Read note
		readNote();
	}
	
	private void readNote(){
		if(mPreferences == null) return;
		String title = mPreferences.getString(Constant.KEY_NOTE_TITLE, "");
		if(!TextUtils.isEmpty(title)){
			TextView titleView = (TextView) findViewById(R.id.noteTitle);
			titleView.setText(title);
		}
		
		String content = mPreferences.getString(Constant.KEY_NOTE_CONTENT, "");
		if(!TextUtils.isEmpty(content)){
			TextView contentView = (TextView) findViewById(R.id.noteContent);
			contentView.setText(content);
		}
	}
	
	private void writeNote(){
		if(mPreferences == null) return;
		Editor editor = mPreferences.edit();
		TextView titleView = (TextView) findViewById(R.id.noteTitle);
		
		String title = titleView.getText().toString().trim();
		if(!TextUtils.isEmpty(title)){
			editor.putString(Constant.KEY_NOTE_TITLE, title);
		}
		
		TextView contentView = (TextView) findViewById(R.id.noteContent);
		String content = contentView.getText().toString().trim();
		if(!TextUtils.isEmpty(content)){
			editor.putString(Constant.KEY_NOTE_CONTENT, content);
		}
		editor.commit();
	}

}
