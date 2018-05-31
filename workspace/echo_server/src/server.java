import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.DataInputStream;
import java.net.Socket;
import java.net.ServerSocket;
import java.util.Scanner;


public class server
{
	public static void main(String[] args)
	{
		//int port =Integer.parseInt(args[0]);
		try
		{
			//try to establish a server socket first
			ServerSocket server_socket = new ServerSocket(1234);
			System.out.println("Waiting for client on port "+ server_socket.getLocalPort()+"...");
			Socket server = server_socket.accept();
			System.out.println("Just connected to "+ server.getRemoteSocketAddress());
			DataOutputStream out = new DataOutputStream (server.getOutputStream());
			DataInputStream in = new DataInputStream(server.getInputStream());
			out.writeBytes("You just connected to server at port 1234!\n");
			String from_client;
			//System.out.println(in.readUTF());
			do
			{
				from_client = in.readLine();
				System.out.println("The client has send: "+ from_client);
				out.writeBytes(from_client + "\r\n");			//echo back what the client had send to the server
			}
			while(!from_client.equals("quit"));
			System.out.println("The connection to: "+ server.getRemoteSocketAddress()+" has terminated");
			out.writeBytes("goodbye\n");
			server.close();
			out.close();
			in.close();
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}
		
	}

}

/*
public class GreetingServer extends Thread
{
	private ServerSocket serverSocket;
	
	
	public GreetingServer(int port)throws IOException
	{
		serverSocket =new ServerSocket(port);
 		serverSocket.setSoTimeout(10000);
	}
	public void run()
	{
		while(true)
		{
			try
			{
				System.out.println("Waiting for client on port "+
				serverSocket.getLocalPort()+"...");
				Socket server = serverSocket.accept();
				System.out.println("Just connected to "
				+ server.getRemoteSocketAddress());
				DataInputStream in=new DataInputStream(server.getInputStream());
				System.out.println(in.readUTF());
				DataOutputStream out=new DataOutputStream(server.getOutputStream());
				out.writeUTF("Thank you for connecting to "+
				server.getLocalSocketAddress()+"\nGoodbye!");
 				server.close();
			}
			catch(SocketTimeoutException s)
			{
				System.out.println("Socket timed out!");
				TUTORIALS POINT
				Simply!Easy!Learning
				break;
			}
			catch(IOException e)
			{
				e.printStackTrace();
				break;
			}
		}
	}
	
	public static void main(String[] args)
	{
		int port =Integer.parseInt(args[0]);
		try
		{
			Thread t =new GreetingServer(port);
			t.start();
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}
	}
}
*/
