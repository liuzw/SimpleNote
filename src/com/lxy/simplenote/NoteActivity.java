package com.lxy.simplenote;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.TextView;

public class NoteActivity extends Activity{
	
	private int widgetId;
	private EditText mTitleView;
	private EditText mContentView;
	private NoteTextWatcher mTextWatcher;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//Constant.log("onCreate");
		setContentView(R.layout.note_activity_layout);
		Intent i = getIntent();
		widgetId = i.getIntExtra(Constant.KEY_WIDGET_ID, -1);
		Constant.log("onCreate: widgetId = " + widgetId + ", intent = " + i);
		mTitleView = (EditText) findViewById(R.id.noteTitle);
		mContentView = (EditText) findViewById(R.id.noteContent);
		mTextWatcher = new NoteTextWatcher();
		mContentView.addTextChangedListener(mTextWatcher);
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
		String content = preferences.getString(Constant.KEY_NOTE_CONTENT + widgetId, "");
		if(!TextUtils.isEmpty(content)){
			mContentView.setText(content);
			mContentView.setSelection(content.length());
		}else{
			mContentView.setText(Constant.DEFAULT_CONTENT);
		}
		
		String title = preferences.getString(Constant.KEY_NOTE_TITLE + widgetId, "");
		if(!TextUtils.isEmpty(title)){
			mTitleView.setText(title);
			mContentView.requestFocus();
		}
	}
	
	/**
	 * 存储现在笔记和标题
	 */
	private void writeNote(){
		SharedPreferences preferences = getSharedPreferences(Constant.PREFERENCES_NAME, 0);
		Editor editor = preferences.edit();
		
		String editTitle = mTitleView.getText().toString().trim();
		String savedTitle = preferences.getString(Constant.KEY_NOTE_TITLE + widgetId, "");
		if((!TextUtils.isEmpty(editTitle) || !TextUtils.isEmpty(savedTitle)) 
				/*&& !editTitle.equals(savedTitle)*/){	//修改系统语言后，widget不能更新
			editor.putString(Constant.KEY_NOTE_TITLE + widgetId, editTitle);
			NoteAppWidgetProvider.setSummary(this,widgetId, editTitle);
		}
		
		String editContent = mContentView.getText().toString().trim();
		String savedContent = preferences.getString(Constant.KEY_NOTE_CONTENT + widgetId, "");
		if((!TextUtils.isEmpty(editContent) || !TextUtils.isEmpty(savedContent))
				&& !editContent.equals(savedContent)){
			editor.putString(Constant.KEY_NOTE_CONTENT + widgetId, editContent);
		}
		editor.commit();
	}
	
	class NoteTextWatcher implements TextWatcher{

		@Override
		public void afterTextChanged(Editable s) {
			// TODO Auto-generated method stub
		}

		@Override
		public void beforeTextChanged(CharSequence s, int start, int count,
				int after) {
			// TODO Auto-generated method stub
		}

		@Override
		public void onTextChanged(CharSequence s, int start, int before,
				int count) {
			// TODO Auto-generated method stub
			mContentView.removeTextChangedListener(mTextWatcher);
			boolean isDelete = (before == 1);
			CursorPositionManager cpm = new CursorPositionManager(start + 1);	//start + 1: set start begin from 1
			String srt = Constant.contentSmartProcess(s, isDelete, cpm);
			mContentView.setText(srt);
			int cp = cpm.getCursorPosition() <= srt.length() ? cpm.getCursorPosition() : srt.length();
			mContentView.setSelection(cp);
			mContentView.addTextChangedListener(mTextWatcher);
		}
		
	}
}
