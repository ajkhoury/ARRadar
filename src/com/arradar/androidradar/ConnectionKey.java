package com.arradar.androidradar;

import java.util.Random;

public class ConnectionKey
{
	private String key;
	
	public String getKey()
	{
		return key;
	}
	
	public void generateConnectionKey(int length)
	{
    	final String keychars = "abcdefghijkmnpqrstuvwxyz0123456789ABCDEFGHJKLMNPRSTUVWXYZ";
    	key = "";
    	
    	Random rand = new Random();
    	for (int i = 0; i < length; ++i)
    	{
    		key += keychars.charAt(rand.nextInt(keychars.length()));
    	}
    }
}
