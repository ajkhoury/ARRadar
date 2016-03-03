package com.arradar.androidradar;

import android.app.Activity;
import android.content.Intent;
import android.media.AudioManager;
import android.media.SoundPool;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.arradar.androidradar.net.NetworkManager;
import com.arradar.androidradar.net.Packet;
import com.arradar.androidradar.net.PacketPlayerData;
import com.arradar.androidradar.net.PacketStatus;
import com.arradar.androidradar.opengl.OpenGLRenderer;

public class RadarActivity extends Activity
{
	private RadarPlayer[] radarPlayers = new RadarPlayer[64];
	private Activity instance;
	private Thread radarThread;
	private Thread invalidDataThread;
	private boolean checkInvalidData = true;
	
	private SoundPool soundPool;
	private int enemyNearSoundId;
	
	@Override
	@SuppressWarnings("deprecation")
    public void onCreate(Bundle savedInstanceState)
	{
        super.onCreate(savedInstanceState);
        
        instance = this;
        
        setVolumeControlStream(AudioManager.STREAM_MUSIC);
		soundPool = new SoundPool(10, AudioManager.STREAM_MUSIC, 0);
		enemyNearSoundId = soundPool.load(this, R.raw.enemynear, 1);
        
        GlobalApplication app = (GlobalApplication)getApplication();
        app.setKeepScreenOn(instance, true);
        
        for (int i = 0; i < radarPlayers.length; ++i)
        	radarPlayers[i] = new RadarPlayer();
        
        GLSurfaceView view = new GLSurfaceView(this);
   		view.setRenderer(new OpenGLRenderer(radarPlayers, app.getSettings()));
   		setContentView(view);
        
   		invalidDataThread = new Thread(new Runnable() {
   			public void run() 
   			{
   				while (checkInvalidData) 
   				{
   					long timestamp = System.currentTimeMillis() - 500;
   					for (int i = 0; i < radarPlayers.length; ++i) 
   					{
   			        	RadarPlayer radarPlayer = radarPlayers[i];
   			        	if (radarPlayer.isValid && radarPlayer.timestamp < timestamp)
   			        		radarPlayer.isValid = false;
   			        }
   					try 
   					{
						Thread.sleep(100);
					} 
   					catch (InterruptedException e) { }
   				}
   			}
   		});
   		invalidDataThread.start();
   		
        radarThread = new Thread(
        new Runnable() 
        {
			public void run()
			{
				GlobalApplication app = (GlobalApplication)getApplication();
				Settings settings = app.getSettings();
				NetworkManager networkManager = app.getNetworkManager();
				AudioManager audioManager = (AudioManager)getSystemService(AUDIO_SERVICE);
				Vibrator vibrator = (Vibrator)getSystemService(VIBRATOR_SERVICE);
				
				long nextNotification = 0;
				int errorCount = 0;
				Packet packet;
				while (errorCount < 10)
				{
					packet = networkManager.ReceivePacket();
					if (packet instanceof PacketPlayerData)
					{
						PacketPlayerData packetPlayerData = (PacketPlayerData)packet;
						
						RadarPlayer radarPlayer = radarPlayers[packetPlayerData.index];
						radarPlayer.isValid = true;
						radarPlayer.x = (packetPlayerData.x / 100.0f) * 4.0f;
						radarPlayer.y = (packetPlayerData.y / 100.0f) * 4.0f;
						radarPlayer.yaw = packetPlayerData.yaw;
						radarPlayer.health = packetPlayerData.health;
						radarPlayer.isEnemy = packetPlayerData.isEnemy;
						radarPlayer.timestamp = System.currentTimeMillis();
						
						if (packetPlayerData.isEnemy)
						{
							if (packetPlayerData.x < 10 && packetPlayerData.y < 10)
							{
								if (nextNotification < System.currentTimeMillis())
								{
									if (settings.notificationSound)
									{
										float actualVolume = (float)audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
										float maxVolume = (float)audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
										float volume = actualVolume / maxVolume;
										soundPool.play(enemyNearSoundId, volume, volume, 1, 0, 1f);
									}
									if (settings.notificationVibration)
									{
										vibrator.vibrate(200);
									}

									nextNotification = System.currentTimeMillis() + 2000;
								}
							}
						}
					} 
					else if (packet instanceof PacketStatus)
					{
						PacketStatus packetStatus = (PacketStatus)packet;
						if (packetStatus.status == 2)
						{
							checkInvalidData = false;
							break;
						}
					} 
					else
					{
						++errorCount;
					}
				}
				
				networkManager.Close();
				
				instance.runOnUiThread(
				new Runnable() 
				{
				    public void run()
				    {
				        Toast.makeText(instance, "disconnected", Toast.LENGTH_LONG).show();
				    }
				});

				app.setKeepScreenOn(instance, false);

				instance.finish();
			}
		});
        
		radarThread.start();
    }

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		MenuInflater inflater = getMenuInflater();
	    inflater.inflate(R.menu.menu_radar, menu);
	    return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		switch (item.getItemId())
		{
	        case R.id.menu_radar_settings:
	        {
	        	Intent settingIntent = new Intent(this, SettingActivity.class);
	        	startActivity(settingIntent);
	        	return true;
	        }
	        case R.id.menu_radar_disconnect:
	        {
	        	GlobalApplication app = (GlobalApplication)getApplication();
	        	NetworkManager networkManager = app.getNetworkManager();
	        	networkManager.Close();
				Toast.makeText(app.getBaseContext(), "disconnected", Toast.LENGTH_LONG).show();
				instance.finish();
				return true;
	        }
			default:
				return super.onOptionsItemSelected(item);
		}
	}
}
