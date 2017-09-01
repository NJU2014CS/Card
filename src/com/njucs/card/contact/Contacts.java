package com.njucs.card.contact;

import java.util.HashMap;

import com.njucs.card.connection.Monitor;
import com.njucs.card.connection.SendMessage;
import com.njucs.card.connection.Utils;

import android.util.Log;

/*
 * 联系人字段，暂时这些，以后需要再添加
 * 2016-11-02
 */

public class Contacts{
	private String name=null;				// 姓名
	private String duty=null;				// 职务
	private String company=null;			// 公司
	private String address=null;			// 地址
	private String telephone=null;			// 电话
	private String mobilephone=null;		// 手机
	private String mail=null;				// 邮箱
	private String fax=null;				// 传真
	private String note=null;				// 备注
	
	// 构造函数
	public Contacts(String s){
		/*String []info=s.split("\n");
		for(int i=0;i<info.length;i++){
			if(info[i].contains("公司")){
				company=info[i];
			}
			if(info[i].contains("地址")&&address==null){
				address=info[i].substring(info[i].indexOf(":")+1);
			}
			if(info[i].contains("邮箱")){
				mail=info[i].substring(info[i].indexOf(":")+1);
			}
			if(info[i].contains("网址")){
				note=info[i].substring(info[i].indexOf(":")+1);
			}
			if(info[i].matches("^((13[0-9])|(15[^4,\\D])|(18[0,5-9]))\\d{8}$")){
				mobilephone=info[i];
			}
			if(info[i].contains("黄")){
				name=info[i];
			}
			if(info[i].contains("传真")){
				fax=info[i].substring(info[i].indexOf(":")+1);
			}
		}*/
		Monitor m=new Monitor(new SendMessage("192.168.1.105", Utils.Transformer(2, s.getBytes())), 10000);
		new Thread(m).start();
		while(!m.isOver()){}
		if(m.GetErrorcode()!=-1)
			note=m.GetErrorInfo();
		else{
			HashMap<String,String> map=new HashMap<String,String>();
			String res=(String)m.GetResult();
			Log.i("Contacts",res);
			String[] list=res.split(" ");
			Log.i("Test","list length "+list.length);
			for(int i=0;i<list.length;i++){
				String[] buf=list[i].split(":");
				Log.i("Test",list[i]);
				map.put(buf[0], buf[1]);
			}
			name=map.get("name");
			duty=map.get("duty");
			company=map.get("company");
			address=map.get("address");
			telephone=map.get("telephone");
			mobilephone=map.get("mobilephone");
			mail=map.get("mail");
			fax=map.get("fax");
			note=map.get("note");
		}
	}
	
	//	Get Set 函数
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDuty() {
		return duty;
	}
	public void setDuty(String duty) {
		this.duty = duty;
	}
	public String getCompany() {
		return company;
	}
	public void setCompany(String company) {
		this.company = company;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getTelephone() {
		return telephone;
	}
	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}
	public String getMobilephone() {
		return mobilephone;
	}
	public void setMobilephone(String mobilephone) {
		this.mobilephone = mobilephone;
	}
	public String getMail() {
		return mail;
	}
	public void setMail(String mail) {
		this.mail = mail;
	}
	public String getFax() {
		return fax;
	}
	public void setFax(String fax) {
		this.fax = fax;
	}
	public String getNote() {
		return note;
	}
	public void setNote(String note) {
		this.note = note;
	}

}
