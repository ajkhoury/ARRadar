package com.arradar.androidradar;

public class RadarPlayer
{
	public boolean isValid;
	public boolean isEnemy;
	public float x;
	public float y;
	public float yaw;
	public int health;
	public long timestamp;
	
	public RadarPlayer()
	{
		isValid = false;
		timestamp = 0;
		x = y = 99999.9f;
		health = 0;
		yaw = 0.0f;
	}
}
