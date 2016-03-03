package com.arradar.androidradar;

public class Settings
{	
	// Members
	public int radarBackgroundSkin;
	public int radarPlayerSkin;
	public boolean radarShowHealth;
	public boolean radarShowFriendly;
	public boolean notificationSound;
	public boolean notificationVibration;
	
	// public functions
	public Settings()
	{
		setDefaultSettings();
	}
	
	// private functions
	private void setDefaultSettings()
	{		
		radarBackgroundSkin = 0;
		radarPlayerSkin = 0;
		radarShowHealth = false;
		radarShowFriendly = false;
		notificationSound = false;
		notificationVibration = false;
	}
}
