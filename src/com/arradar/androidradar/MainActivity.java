package com.arradar.androidradar;

import java.math.BigInteger;
import java.net.InetAddress;
import java.net.UnknownHostException;

import com.arradar.androidradar.net.NetworkManager;
import com.arradar.androidradar.net.Packet;
import com.arradar.androidradar.net.PacketStatus;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.wifi.SupplicantState;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity
{
	private WifiInfo wifiInfo;
	private Thread waitThread = null;
	private Activity instance;
	private static boolean listen;
	
	private void prepareToListen()
	{
		((TextView)findViewById(R.id.waitingLabel)).setText(R.string.waiting);
    	findViewById(R.id.waitingProgressBar).setVisibility(View.VISIBLE);
		
		WifiManager wifiManager = (WifiManager)getSystemService(Context.WIFI_SERVICE);
        wifiInfo = wifiManager.getConnectionInfo();
        if (wifiInfo.getSupplicantState() != SupplicantState.COMPLETED)
        {
        	AlertDialog.Builder builder = new AlertDialog.Builder(this);
        	builder.setMessage(R.string.nowifi).setPositiveButton("Ok", null).show();
        	
        	((TextView)findViewById(R.id.waitingLabel)).setText(R.string.noconnection);
        	findViewById(R.id.waitingProgressBar).setVisibility(View.INVISIBLE);
        } 
        else 
        {
	        waitThread = new Thread(new Runnable() {
				public void run()
				{
					GlobalApplication app = (GlobalApplication)getApplication();
					NetworkManager networkManager = app.getNetworkManager();
					
					//listen = true;					
					while (listen)
					{
						try
						{	
							if (networkManager.Listen(3323))
							{
								instance.runOnUiThread(new Runnable() {
								    public void run()
								    {
								        Toast.makeText(instance, "connected with: " + ((GlobalApplication)getApplication()).getNetworkManager().getSocket().getInetAddress().getHostAddress(), Toast.LENGTH_LONG).show();
								    }
								});
								
								Packet packet = networkManager.ReceivePacket();
								if (packet instanceof PacketStatus)
								{
									PacketStatus packetStatus = (PacketStatus)packet;
									if (packetStatus.status == 1)
									{
										packetStatus.status = 3;
										try
										{
											packetStatus.data = getPackageManager().getPackageInfo(getPackageName(), 0).versionCode;
										}
										catch (NameNotFoundException e)
										{
											packetStatus.data = 1;
										}
										networkManager.SendPacket(packetStatus);
										Intent radarIntent = new Intent(app.getBaseContext(), RadarActivity.class);
										startActivity(radarIntent);		
										break;
									}
								}
								networkManager.Close();
							}
							Thread.sleep(25);
						} 
						catch (InterruptedException e) {
					        // We've been interrupted: no more messages.
					        listen = false;
						} 
					} 
				}
			});
			waitThread.start();
        }
	}

	@Override
	public void onCreate(Bundle savedInstanceState) 
    {
        setContentView(R.layout.main);
        instance = this;
        listen = true;

		super.onCreate(savedInstanceState);
    }

	@Override
	protected void onPause() 
	{
		super.onPause();
		listen = false;
	}

	@Override
	protected void onResume() 
	{
		super.onResume();
		prepareToListen();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		MenuInflater inflater = getMenuInflater();
	    inflater.inflate(R.menu.menu, menu);
	    return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		switch (item.getItemId())
		{
	    case R.id.action_settings:
	    	Intent settingIntent = new Intent(this, SettingActivity.class);
	    	startActivity(settingIntent);
	    	break;
	    	
	    case R.id.action_address:
	    	// Format IP address
	    	byte[] ipAddress = BigInteger.valueOf(wifiInfo.getIpAddress()).toByteArray();
	    	// reverse the byte array before conversion
	    	for(int i = 0; i < ipAddress.length / 2; i++) {
	    		byte temp = ipAddress[i];
	    	    ipAddress[i] = ipAddress[ipAddress.length - i - 1];
	    	    ipAddress[ipAddress.length - i - 1] = temp;
	    	}     	
	    	
	    	try 
	    	{
	    		InetAddress myInetIP = InetAddress.getByAddress(ipAddress);
	    		// Convert to string
		    	String myIP = myInetIP.getHostAddress();
			
		    	AlertDialog.Builder builder = new AlertDialog.Builder(this);
		    	builder.setMessage(myIP).setPositiveButton("OK", null).show();
	    	} 
	    	catch (UnknownHostException e) {
	    		e.printStackTrace();
	    	}	
	    	break;
	    	
	    case R.id.action_exit:
	    	if (waitThread.isAlive())
	    		waitThread.interrupt();

	    	NetworkManager networkManager = ((GlobalApplication)getApplication()).getNetworkManager();
	    	networkManager.Close();
		
	    	// Close activity
	    	finish();
			// Force close app
			//System.exit(0);
		
	    	Intent intent = new Intent(Intent.ACTION_MAIN);
	    	intent.addCategory(Intent.CATEGORY_HOME);
	    	intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
	    	startActivity(intent);
	    	
	    	break;
	    	
	    default:
	    	return super.onOptionsItemSelected(item);
		}
		return true;
	}
}