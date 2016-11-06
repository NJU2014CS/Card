package com.njucs.card.initializtion;

/*
 * 每次处理在我们自定义的路径下保存一条处理记录，
 * 记录内容可以是一个联系人的数据结构
 * 程序开始运行会首先到这个路径读取所有记录，导出姓名和公司（有价值）在主页面显示
 * 2016-11-02
 */
public class GetRecentCard {
	private static String[] data = { "Apple", "Banana", "Orange", "Watermelon",
			"Pear", "Grape", "Pineapple", "Strawberry", "Cherry", "Mango" , "Banana", "Orange", "Watermelon",
			"Pear", "Grape", "Pineapple", "Strawberry", "Cherry", "Mango" };
	
	public static String[] getData(){
		return data;
	}
}
