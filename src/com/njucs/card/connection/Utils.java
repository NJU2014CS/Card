package com.njucs.card.connection;

public class Utils {
	static public byte[] Transformer(int type,byte[] src){
		byte[] buffer=new byte[4+src.length];
		buffer[0]=(byte)((type>>0)&0xff);
		buffer[1]=(byte)((type>>8)&0xff);
		buffer[2]=(byte)((type>>16)&0xff);
		buffer[3]=(byte)((type>>24)&0xff);
		for(int i=4;i<buffer.length;i++){
			buffer[i]=src[i-4];
		}
		return buffer;
	}
}
