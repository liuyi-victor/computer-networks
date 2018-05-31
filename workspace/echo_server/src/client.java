import java.net.*;
import java.util.Scanner;
import java.io.*;
import java.lang.Thread;

public class client {

	public static void main(String[] args){
	
		String servername = args[0];					//obtain the server name from argument 0
		int serverport = Integer.parseInt(args[1]);		//obtain server's port from argument 1
		String client_msg = null;
		
		try{
			System.out.println("Trying connecting to "+ servername+" on port "+ serverport);
			Socket client=new Socket (servername, serverport);											//trys to connect with the new socket
			Scanner src = new Scanner(System.in);
			
			BufferedReader reader=new BufferedReader(new InputStreamReader(client.getInputStream()));	//get the server's response from the socket's input stream
			
			String msg= reader.readLine();
			System.out.println("After successful connection, server's message: "+msg);					//print out server's response message
			DataOutputStream writer=new DataOutputStream (client.getOutputStream());					//bind the data output stream to the output stream of socket
			long sent_time, respond_time,trip_time;
			
			
			
			/*
			Thread client_read=new Thread(new RunnableClass(client));
			client_read.start();
			
			*/
			
			do{
				System.out.println("Enter the message that you wish to send to the server: ");
				client_msg = src.nextLine();															//get the user input from keyboard
				sent_time = System.currentTimeMillis();
				writer.writeBytes(client_msg +"\r\n");													//send the user message to the server
				
				if(client_msg.equals("quit"))
					break;
				msg = reader.readLine();
				
				
				respond_time = System.currentTimeMillis();																		//System.out.println("you just entered: "+client_msg);
				trip_time = respond_time-sent_time;
				System.out.println("The server said: "+msg);
				System.out.println("The network round trip time is : "+ trip_time);
			}while(!client_msg.equals("quit"));
			System.out.println("Closing the connection");							
			client.close();
			
			
			reader.close();
			
		}
		catch(IOException e)
		{
			e.getStackTrace();
			e.printStackTrace();
		}
		
	}
}