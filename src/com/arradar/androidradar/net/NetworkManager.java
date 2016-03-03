package com.arradar.androidradar.net;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class NetworkManager 
{	
	private Socket socket;
	private ServerSocket serverSocket;
	
	private DataInputStream reader;
	private DataOutputStream writer;
	
	public Socket getSocket() 
	{
		return socket;
	}
	
	public boolean Listen(int port) 
	{
		try 
		{
			serverSocket = new ServerSocket(port);
			serverSocket.setSoTimeout(10000);
			//serverSocket.setReuseAddress(true);
			socket = serverSocket.accept();
			socket.setReuseAddress(true);
			serverSocket.close();
			reader = new DataInputStream(socket.getInputStream());
			writer = new DataOutputStream(socket.getOutputStream());
		} 
		catch (IOException e) 
		{
			try
			{
				if (serverSocket != null) {
					serverSocket.close();
					serverSocket = null;
				}
				if (socket != null && socket.isConnected()) {
					socket.close();
				}
			} 
			catch (IOException e2) 
			{ 
			}
			
			return false;
		}
		return true;
	}
	
	public void Close()
	{
		try 
		{
			if (serverSocket != null)
				serverSocket.close();
			if (socket != null && socket.isConnected())
				socket.close();
		}
		catch (IOException e) 
		{ 
		}
	}
	
	public boolean SendPacket(Packet packet)
	{
		try
		{
			writer.writeInt(packet.id);
			return packet.send(writer);
		} 
		catch (IOException e)
		{
			return false;
		}
	}
	
	public Packet ReceivePacket() 
	{
		Packet packet = null;
		try 
		{
			switch (reader.readInt()) 
			{
				case 1:
					packet = new PacketStatus();
					break;
				case 2:
					packet = new PacketPlayerData();
					break;
			}
		} 
		catch (IOException e) 
		{
			return null;
		}
		
		if (packet == null || !packet.receive(reader))
			return null;
		
		return packet;
	}
}
