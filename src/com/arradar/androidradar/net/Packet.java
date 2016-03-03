package com.arradar.androidradar.net;

import java.io.DataInputStream;
import java.io.DataOutputStream;

public abstract class Packet 
{
	public int id;

	public abstract boolean send(DataOutputStream writer);
	public abstract boolean receive(DataInputStream reader);
}