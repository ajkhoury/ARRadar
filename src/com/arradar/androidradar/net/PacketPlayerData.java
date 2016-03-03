package com.arradar.androidradar.net;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class PacketPlayerData extends Packet
{
	public int index;		// 0 - 64
	public int x;			// 0 - 4
	public int y;			// 0 - 4
	public float yaw;		// -180 - 180
	public int health;		// 0 - 100
	public boolean isEnemy;	// true or false

	public PacketPlayerData()
	{
		id = 2;
	}

	public boolean send(DataOutputStream writer)
	{
		try
		{
			writer.writeInt(index);
			writer.writeInt(x);
			writer.writeInt(y);
			writer.writeFloat(yaw);
			writer.writeFloat(health);
			writer.writeBoolean(isEnemy);
		} 
		catch (IOException e) {
			return false;
		}
		return true;
	}

	public boolean receive(DataInputStream reader)
	{
		try
		{
			index = reader.readInt();
			x = reader.readInt();
			y = reader.readInt();
			yaw = reader.readFloat();
			health = reader.readInt();
			isEnemy = reader.readBoolean();
		}
		catch (IOException e) {
			return false;
		}
		return true;
	}
}