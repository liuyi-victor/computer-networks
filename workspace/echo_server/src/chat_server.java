import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.DataInputStream;
import java.net.Socket;
import java.net.ServerSocket;
import java.util.Scanner;
import java.lang.Thread;

public class chat_server
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
			Scanner src = new Scanner(System.in);
			DataOutputStream out = new DataOutputStream (server.getOutputStream());
			DataInputStream in = new DataInputStream(server.getInputStream());
			out.writeBytes("You just connected to server at port 1234!\n");
			String from_client, server_msg;
			
			/*
			Thread server_read=new Thread(new RunnableClass(server));
			server_read.start();
			//System.out.println(in.readUTF());*/
			
			
			do
			{
				from_client = in.readLine();
				System.out.println("The client has send: "+ from_client);
				
				if(from_client.equals("quit"))
				{
					System.out.println("The connection to: "+ server.getRemoteSocketAddress()+" has terminated");
					server.close();
					out.close();
					in.close();
					return;
				}
				
				
				server_msg = src.nextLine();
				out.writeBytes(server_msg + "\r\n");			//send the server's message to the client socket
				
				
				
				
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