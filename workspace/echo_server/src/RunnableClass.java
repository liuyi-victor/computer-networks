import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.DataInputStream;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.ServerSocket;
import java.util.Scanner;

public class RunnableClass implements Runnable {
	
	Socket client_socket;
	BufferedReader reader;
	String msg;
	
	RunnableClass (Socket s){
		client_socket = s;
	}
	
	public void run()
	{
		//String client_msg;
		
		while(client_socket.isConnected())
		{
			try {
				reader = new BufferedReader(new InputStreamReader(client_socket.getInputStream()));
			} catch (IOException e) {
				e.printStackTrace();
			}	//get the server's response from the socket's input stream
			try {
				msg= reader.readLine();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			System.out.println("The server said: "+msg);

		
		}
		try {
			reader.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}

