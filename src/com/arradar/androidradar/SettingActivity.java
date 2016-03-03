package com.arradar.androidradar;

import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.preference.PreferenceFragment;

public class SettingActivity extends PreferenceActivity 
{
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		getFragmentManager().beginTransaction().replace(android.R.id.content, new SettingPreferenceFragment()).commit();
	}
	
	public static class SettingPreferenceFragment extends PreferenceFragment
    {
        @Override
        public void onCreate(final Bundle savedInstanceState)
        {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.layout.setting_screen);
        }
    }
	
	//ListPreference backgroundSkinListPreference;
	//ListPreference playerSkinListPreference;
	//CheckBoxPreference showHealthCheckBoxPreference;
	//CheckBoxPreference showFriendlyCheckBoxPreference;
	//CheckBoxPreference soundCheckBoxPreference;
	//CheckBoxPreference vibrationCheckBoxPreference;
	
	//OnPreferenceChangeListener onPreferenceChangeListener = new OnPreferenceChangeListener() {
	//	@Override
	//	public boolean onPreferenceChange(Preference preference, Object newValue) 
	//	{
	//		GlobalApplication app = (GlobalApplication)getApplication();
	//		Settings settings = app.getSettings();
	//		
	//		SharedPreferences SP = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
	//		SP.
	//		if (preference == backgroundSkinListPreference) 
	//		{
	//			settings.radarBackgroundSkin = Integer.parseInt(newValue.toString());
	//		}
	//		else if (preference == playerSkinListPreference) 
	//		{
	//			settings.radarPlayerSkin = Integer.parseInt(newValue.toString());
	//		}
	//		else if (preference == showHealthCheckBoxPreference)
	//		{
	//			settings.radarShowHealth = (Boolean)newValue;
	//		}
	//		else if (preference == showFriendlyCheckBoxPreference)
	//		{
	//			settings.radarShowFriendly = (Boolean)newValue;
	//		}
	//		else if (preference == soundCheckBoxPreference)
	//		{
	//			settings.notificationSound = (Boolean)newValue;
	//		} 
	//		else if (preference == vibrationCheckBoxPreference)
	//		{
	//			settings.notificationVibration = (Boolean)newValue;
	//		}
	//					
	//		return true;
	//	}
	//};
	
	//@SuppressWarnings("deprecation")
	//@Override
	//protected void onCreate(Bundle savedInstanceState)
	//{
	//	super.onCreate(savedInstanceState);
	//	addPreferencesFromResource(R.layout.setting_screen);
	//	
	//	backgroundSkinListPreference = (ListPreference)getPreferenceManager().findPreference("radar_background_skin");
	//	backgroundSkinListPreference.setOnPreferenceChangeListener(onPreferenceChangeListener);
	//	playerSkinListPreference = (ListPreference)getPreferenceScreen().findPreference("radar_player_skin");
	//	playerSkinListPreference.setOnPreferenceChangeListener(onPreferenceChangeListener);
	//	showHealthCheckBoxPreference = (CheckBoxPreference)getPreferenceScreen().findPreference("radar_show_health");
	//	showHealthCheckBoxPreference.setOnPreferenceChangeListener(onPreferenceChangeListener);
	//	showFriendlyCheckBoxPreference = (CheckBoxPreference)getPreferenceScreen().findPreference("radar_show_friendly");
	//	showFriendlyCheckBoxPreference.setOnPreferenceChangeListener(onPreferenceChangeListener);
	//	soundCheckBoxPreference = (CheckBoxPreference)getPreferenceScreen().findPreference("notification_sound");
	//	soundCheckBoxPreference.setOnPreferenceChangeListener(onPreferenceChangeListener);
	//	vibrationCheckBoxPreference = (CheckBoxPreference)getPreferenceScreen().findPreference("notification_vibration");
	//	vibrationCheckBoxPreference.setOnPreferenceChangeListener(onPreferenceChangeListener);
	//}
}
