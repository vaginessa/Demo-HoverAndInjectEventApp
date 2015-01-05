package com.example.hoversample;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.Socket;
import android.graphics.Point;

public class ServerClient
{
	private static String ip = "127.0.0.1";
	private static int port = 8585;
	
	
	private static void send(String string) throws IOException
	{
		Socket s = new Socket(ip, 8585);
		BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(
				s.getOutputStream()));
		bw.write(string + "\n");
		bw.close();
		s.close();
	}
	
	//------------------------------>
	
	public static void test() throws Exception
	{
		send("input swipe 800 800 100 100");
		Thread.sleep(1000);
		send("input swipe 100 100 800 100");
		Thread.sleep(1000);
		send("input swipe 100 100 800 100");
		Thread.sleep(1000);
		send("input tap 1000 1000");
	}

	public static void tap(Point point) throws IOException
	{
		String cmd = String.format("input tap %d %d",point.x,point.y);
		send(cmd);
	}

}
