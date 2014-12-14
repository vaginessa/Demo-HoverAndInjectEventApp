import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.Socket;

public class ServerClient
{
	static String ip = "192.168.1.22";
	static int port = 8585 ;
	public static void main(String[] args) throws InterruptedException
	{
		try
		{
			send("input swipe 800 800 100 100");
			Thread.sleep(1000);
			send("input swipe 100 100 800 100");
			Thread.sleep(1000);
			send("input swipe 100 100 800 100");
			Thread.sleep(1000);
			send("input tap 1000 1000");
		}
		catch (Exception e)
		{
			System.out.println(e.getMessage());
		}
	}

	public static void send(String string)
			throws IOException
	{
		Socket s = new Socket(ip, 8585);
		BufferedWriter bw = new BufferedWriter(
				new OutputStreamWriter(s.getOutputStream()));
		bw.write(string + "\n");
		bw.close();
		s.close();
	}
	
}
