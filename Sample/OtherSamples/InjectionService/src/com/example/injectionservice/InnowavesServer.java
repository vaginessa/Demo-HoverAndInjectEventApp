package com.example.injectionservice;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class InnowavesServer
{
	private static final int PORT = 8585;
	private static Socket s;

	public static void logError(Exception e)
	{
		System.out.println("Error " + e.getMessage());
	}

	public static void readFromProcess(final BufferedReader br,
			final BufferedReader br1)
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
					logError(e);
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
					logError(e);
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

	public static void launchServerSocketThread() throws IOException
	{
		final ServerSocket ss = new ServerSocket(PORT);
		while (true)
		{
			s = ss.accept();

			System.out.println("New client ! ");

			if (s != null && !ss.isClosed())
			{
				Thread t = new Thread()
				{
					public void run()
					{
						try
						{
							Process exec = Runtime.getRuntime().exec("sh");

							// Read from process
							final BufferedReader processReader = new BufferedReader(
									new InputStreamReader(exec.getInputStream()));
							final BufferedReader processErrorReader = new BufferedReader(
									new InputStreamReader(exec.getErrorStream()));
							readFromProcess(processReader, processErrorReader);

							// Write to process
							BufferedWriter bw = new BufferedWriter(
									new OutputStreamWriter(
											exec.getOutputStream()));
							// Socket Reader
							final BufferedReader socketReader = new BufferedReader(
									new InputStreamReader(s.getInputStream()));
							String cmd = socketReader.readLine();
							System.out.println(cmd);
							write(bw, cmd);
						}
						catch (Exception e)
						{
							logError(e);
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
		System.out.println("Hello Inno Server");
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
