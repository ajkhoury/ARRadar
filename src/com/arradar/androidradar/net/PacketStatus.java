package com.arradar.androidradar.net;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class PacketStatus extends Packet
{
	public byte status; //1 = connect, 2 = disconnect, 3 = version
	public int data;

	public PacketStatus() {
		id = 1;
	}

	public boolean send(DataOutputStream writer)
	{
		try
		{
			writer.writeByte(status);
			writer.writeInt(data);
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
			status = reader.readByte();
			data = reader.readInt();
		}
		catch (IOException e) {
			return false;
		}
		return true;
	}
}