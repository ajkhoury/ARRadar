package com.arradar.androidradar;

import com.arradar.androidradar.net.NetworkManager;

import android.app.Activity;
import android.app.Application;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.view.WindowManager;

public class GlobalApplication extends Application 
{
	private ConnectionKey connectionKey;
	private Settings settings;
	private NetworkManager networkManager;
	@SuppressWarnings("unused")
	private WindowManager windowManager;

    @Override
    public void onCreate() 
    {
		super.onCreate();

    	connectionKey = new ConnectionKey();
    	connectionKey.generateConnectionKey(4);
    	
    	settings = new Settings();
    	SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
    	settings.radarBackgroundSkin = Integer.parseInt(preferences.getString("radar_background_skin", "0"));
    	settings.radarPlayerSkin = Integer.parseInt(preferences.getString("radar_player_skin", "0"));
    	settings.radarShowHealth = preferences.getBoolean("radar_show_health", false);
    	settings.radarShowFriendly = preferences.getBoolean("radar_show_friendly", false);
    	settings.notificationSound = preferences.getBoolean("notification_sound", false);
    	settings.notificationVibration = preferences.getBoolean("notification_vibration", false);
    	
    	networkManager = new NetworkManager();
    }

	public void setKeepScreenOn(Activity activityRef, boolean enable)
	{
		if (enable)
			activityRef.getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		else
			activityRef.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
	}
    
    public Settings getSettings() 
    {
    	SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
    	settings.radarBackgroundSkin = Integer.parseInt(preferences.getString("radar_background_skin", "0"));
    	settings.radarPlayerSkin = Integer.parseInt(preferences.getString("radar_player_skin", "0"));
    	settings.radarShowHealth = preferences.getBoolean("radar_show_health", false);
    	settings.radarShowFriendly = preferences.getBoolean("radar_show_friendly", false);
    	settings.notificationSound = preferences.getBoolean("notification_sound", false);
    	settings.notificationVibration = preferences.getBoolean("notification_vibration", false);
    	
    	return settings;
    }
    
    public ConnectionKey getConnectionKey() 
    {
    	return connectionKey;
    }

    public NetworkManager getNetworkManager() { return networkManager; }
}
