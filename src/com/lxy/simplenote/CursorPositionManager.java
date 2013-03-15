package com.lxy.simplenote;

public class CursorPositionManager {
	private int start;				//字符串处理前，光标的位置
	private int cursorPosition;		//字符串处理后，光标应该出现的位置
	
	public CursorPositionManager(int start){
		this.start = start;
	}
	
	public int getCursorPosition(){
		return cursorPosition;
	}
	
	public void setCursoPosition(int p){
		this.cursorPosition = p;
	}
	
	public int getStart(){
		return start;
	}

	@Override
	public String toString() {
		return "CursorPositionManager [start=" + start + ", cursorPosition="
				+ cursorPosition + "]";
	}
}
