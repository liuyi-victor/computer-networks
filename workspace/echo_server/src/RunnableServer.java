import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.DataInputStream;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.ServerSocket;
import java.util.Scanner;

public class RunnableServer implements Runnable {
	
	Socket server_socket;
	Scanner src = new Scanner(System.in);
	
	RunnableServer (Socket s){
		server_socket = s;
	}
	
	public void run()
	{
		String server_msg;
		DataOutputStream out;
		try {
			out = new DataOutputStream (server_socket.getOutputStream());
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		while(server_socket.isConnected());
		{
			server_msg = src.nextLine();
			try {
				out.writeBytes(server_msg + "\r\n");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}			//send the server's message to the client socket
		}
		
	}
}