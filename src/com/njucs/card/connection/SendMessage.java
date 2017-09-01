package com.njucs.card.connection;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.Socket;
import java.net.UnknownHostException;

import android.util.Log;


//调用该模块时，需要判断线程是否在运行，运行完毕后才可读取返回结果！

public class SendMessage implements Runnable{
	private Socket socket;
	private String ip="localhost";
	private Object Message;
	private Object Result;
	private boolean isOver=false;
	private final int ttl=5000;
	
	/*
	 *errorcode对应的错误 
	 *-1		无错误
	 *0			错误的ip地址
	 *1			无法连接服务器
	 *2			与服务器连接断开
	 */
	private int errorcode=-1;
	
	public SendMessage(String ip,Serializable Message){
		this.Message=Message;
		Result=null;
		this.ip=ip;
	}
	
	//该函数在初始化时不可使用，因为Android不允许在主线程中使用网络通信
	private void connect(){
		try {
			socket=new Socket(ip,8080);
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
			errorcode=0;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
			errorcode=1;
		}
	}
	
	private void send(Object Message){
		try {
			ObjectOutputStream out=new ObjectOutputStream(socket.getOutputStream());
			out.writeObject(Message);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
			errorcode=2;
		}
	}
	
	private void get(){
		try {
			ObjectInputStream in=new ObjectInputStream(socket.getInputStream());
			Result=in.readObject();
		} catch (Exception e) {
			//只要捕捉到异常就说明数据出错，因而不能使用该数据，需要舍弃
			Result=null;
		}
	}
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		
		connect();
		
		//连接正常时等待服务器的返回结果
		if(errorcode==-1){
			send(Message);
			
			long starttime=System.currentTimeMillis();
			
			while(System.currentTimeMillis()-starttime<=ttl){
				Log.i("SendMessage Run",(System.currentTimeMillis()-starttime)+"");
				get();
				if(Result!=null){
					//deal with the Result received here
					
					
					break;
				}
			}
			try {
				if(socket!=null)
					socket.close();
			} catch (IOException e) {
				e.printStackTrace();
			} finally{
				isOver=true;
			}
		}
		else{
			isOver=true;
		}
	}
	
	//get the result
	public Object getResult() {
		return Result;
	}
	
	//judge if the Thread has shut down
	public boolean isOver(){
		return isOver;
	}
	
	//get the errorcode
	public int getErrorCode(){
		return errorcode;
	}
}
