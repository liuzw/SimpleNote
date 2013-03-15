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
	
	public static final String KEY_WIDGET_ID = "widget_id";
	
	//标点集，项数字后面标点，包括中英文的逗号句号。
	public static final char[] NUMBER_SET = new char[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9'};
	public static final char[] PUNCTUATION_SET = new char[]{',', '.', '，', '。'};
	public static final char NEW_LINE_SYMBOL = '\n';
	public static final String DEFAULT_CONTENT = "1.";
	public static final char DEFAULT_PUNCTUATION = '.';
	
	/**
	 * 判断是否所指定的标点
	 * @param c
	 * @return
	 */
	private static boolean isPunctuation(char c){
		for(int i = 0; i < PUNCTUATION_SET.length; i++){
			if(c == PUNCTUATION_SET[i]) return true;
		}
		return false;
	}
	
	/**
	 * 判断一个字符是不是数字
	 * @param c
	 * @return
	 */
	private static boolean isNumber(char c){
		for(int i = 0; i < NUMBER_SET.length; i ++){
			if(c == NUMBER_SET[i]) return true;
		}
		return false;
	}
	
	/**
	 * 判断s 从start开始的位置是否为数字之后跟随一个标点
	 * @param s
	 * @param start
	 * @return	返回数字的位数
	 */
	private static int isNumberAndPunctuation(CharSequence s, int start){
		if(s.length() < start + 1) return 0;
		if(!isNumber(s.charAt(start))) return 0;
		int numberCount = 0;
		for(int i = start; i < s.length()-1; i++){
			if(isNumber(s.charAt(i)) && isNumber(s.charAt(i+1))){
				numberCount ++;
			}else if(isNumber(s.charAt(i)) && isPunctuation(s.charAt(i + 1))){
				numberCount ++;
				return numberCount;
			}else if(isNumber(s.charAt(i)) && !isPunctuation(s.charAt(i + 1)) && !isNumber(s.charAt(i + 1))){
				return 0;
			}
		}
		return numberCount;
	}
	
	/**
	 * 笔记内容智能处理
	 * @param s
	 * @param isDelete: 是删除过程(true)、还是编辑输入过程(false)
	 * @param cp 光标位置处理
	 * @return
	 */
	public static String contentSmartProcess(CharSequence s, boolean isDelete, CursorPositionManager cp){
		
//		log("需要处理的内容是： " + s + ", isDelete = " + isDelete + ", cp = " + cp);
		
		if(s == null || s.length() < 3) return s.toString();

		//如果不是以 “1.”（即，1 跟上一个变点符号） 开头，则不做处理,但是以换行开头的情况除外
		if((s.charAt(0) != NUMBER_SET[1] || !isPunctuation(s.charAt(1)))
				&& s.charAt(0) != NEW_LINE_SYMBOL) return s.toString();
		
		int number = 1;						//标号
		int lastItemPosInS = 0;				//换行的下一个字符位置
		int cursorOffset = 0;				//光标移动量
		
		StringBuffer sb = new StringBuffer();
		//检测里面的换行符
		for(int i = 0; i < s.length(); i++){
			char c = s.charAt(i);
			//查找换行
			if(c == NEW_LINE_SYMBOL){
				if(i == 0){
					//一开头就是换行符，就把它显示为"1."
					int numberCount = isNumberAndPunctuation(s, i+1);
					if(numberCount > 0){
						sb.append(number).append(DEFAULT_PUNCTUATION).append(c).append(++number);
						lastItemPosInS = i + numberCount + 1;	//+1：为逗点
						cursorOffset += 1;
					}
				}else if(i == s.length()-1){
					//最后一个字符是换行
					sb.append(s.subSequence(lastItemPosInS, s.length())).append(++number).append(DEFAULT_PUNCTUATION);
					lastItemPosInS = s.length();
					int numberLength = String.valueOf(number).length() + 1; // +1:为逗点
					if(!isDelete && i + 1 <= cp.getStart()) cursorOffset += numberLength;
				}else{
					//中间的换行
					int numberCount = isNumberAndPunctuation(s, i+1);
					//isDelete: 如果所删除过错，运行有空行，如果是编辑输入态，回车直接添加标号。
					if(!isDelete && i<s.length()-1 && s.charAt(i+1) == NEW_LINE_SYMBOL){
						sb.append(s.subSequence(lastItemPosInS, i+1)).append(++number).append(DEFAULT_PUNCTUATION);
						lastItemPosInS = i + 1;
						int numberLength = String.valueOf(number).length() + 1; // +1:为逗点
						if(i + 1 <= cp.getStart()) cursorOffset += numberLength;
					}else if(numberCount > 0){
						sb.append(s.subSequence(lastItemPosInS, i+1)).append(++number).append(DEFAULT_PUNCTUATION);
						lastItemPosInS = i + numberCount + 2;	//+2:下次从逗点后的那个字符开始
					}
				}
			}
		}
		if(lastItemPosInS == 0){
			sb.append(s);
		}else if(lastItemPosInS < s.length()){
			sb.append(s.subSequence(lastItemPosInS, s.length()));
		}
		
		//光标位置处理
		if(isDelete){
			cp.setCursoPosition(cp.getStart() - 1);
		}else{
			cp.setCursoPosition(cp.getStart() + cursorOffset);
		}
//		log("处理后的内容是： " + sb.toString() + ", cp = " + cp);
		return sb.toString();
	}
	
	
	
	
	
}
