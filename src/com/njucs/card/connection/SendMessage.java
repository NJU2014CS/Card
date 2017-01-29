package com.njucs.card.connection;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.io.StreamCorruptedException;
import java.net.Socket;
import java.net.UnknownHostException;


//调用该模块时，需要判断线程是否在运行，运行完毕后才可读取返回结果！

public class SendMessage implements Runnable{
	private Socket socket;
	private static String ip="localhost";
	private Object Message;
	private Object Result;
	
	/*
	 *errorcode对应的错误 
	 *-1		无错误
	 *0			错误的ip地址
	 *1			无法连接服务器
	 *2			连接超时
	 *3			与服务器连接断开
	 */
	private int errorcode=-1;
	
	public SendMessage(Serializable Message){
		this.Message=Message;
		Result=null;
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
			errorcode=3;
		}
	}
	
	private void get(){
		try {
			ObjectInputStream in=new ObjectInputStream(socket.getInputStream());
			Result=in.readObject();
		} catch (StreamCorruptedException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
			Result=null;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
			Result=null;
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
			Result=null;
		}
	}
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		if(errorcode==-1){
			send(Message);
			long starttime=System.currentTimeMillis();
			while(true){
				get();
				if(Result!=null){
					//deal with the Result received here
					
					
					break;
				}
				if(System.currentTimeMillis()-starttime>=5000){
					//overtime and exit
					errorcode=2;
					break;
				}
			}
		}
		try {
			socket.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	//get the result
	public Object getResult() {
		return Result;
	}
	
	//get the errorcode
	public int getErrorCode(){
		return errorcode;
	}
}
