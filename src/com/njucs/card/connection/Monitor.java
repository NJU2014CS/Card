package com.njucs.card.connection;

import android.util.Log;

//启动并监视通信线程是否超时
public class Monitor implements Runnable{
	
	private SendMessage sm;
	private long TTL;
	private Object Result;
	private int errorcode;
	private boolean isOver=false;

	public Monitor(SendMessage sm,long TTL) {
		// TODO Auto-generated constructor stub
		this.sm=sm;
		this.TTL=TTL;
	}
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		long starttime=System.currentTimeMillis();
		Thread t=new Thread(sm);
		t.start();
		Log.i("Monitor","EnterLoop "+(System.currentTimeMillis()-starttime));
		while(System.currentTimeMillis()-starttime <= TTL){
			if(sm.isOver()){
				Log.i("Monitor","Get FeedBack");
				errorcode=sm.getErrorCode();
				Result=sm.getResult();
				isOver=true;
				Log.i("Monitor","Used "+(System.currentTimeMillis()-starttime)+"ms");
				return;
			}
		}
		Log.i("Monitor","EndLoop "+(System.currentTimeMillis()-starttime));
		errorcode=3;
		isOver=true;
		return;
	}
	
	public boolean isOver(){
		return isOver;
	}
	
	public int GetErrorcode(){
		return errorcode;
	}
	
	public String GetErrorInfo(){
		switch(errorcode){
		case -1:return "一切正常";
		case 0:return "错误的IP地址";
		case 1:return "无法连接服务器";
		case 2:return "与服务器连接断开";
		case 3:return "连接超时";
		default:return "未定义异常";
		}
	}
	
	public Object GetResult(){
		return Result;
	}
	
}
