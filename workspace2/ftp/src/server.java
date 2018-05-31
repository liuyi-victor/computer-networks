import java.io.File;
import java.io.FileInputStream;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.DataInputStream;
import java.net.Socket;
import java.net.ServerSocket;
import java.net.UnknownHostException;
import java.util.Scanner;


/*
 * server_socket = base socket for control connection
 * server = control connection
 * serverdata = base socket for data connection
 * datasocket = data connection
 * 
 */

class ftpserver_runnable implements Runnable {

	ServerSocket datasocket;
	Socket serverdata;
	DataOutputStream filetoclient;
	File file;
	String filename;
//ftpserver_runnable(int dataport, String f){
	 ftpserver_runnable(Socket temp0, ServerSocket temp1, String name, File f){
			filename = name;
			file = f;
			serverdata = temp0;
			datasocket = temp1;
			try {
				filetoclient = new DataOutputStream(serverdata.getOutputStream());
			} catch (IOException e) {
				e.printStackTrace();
			}
	 }
	 
	 public void run() {
		long filesize;
		System.out.println("the server now is running the file transfer process in a separate thread!\n");
		try {
			filetoclient.writeLong(file.length());											//tell the client how large the file transfer is
			
			java.io.FileInputStream fin = new FileInputStream(file);
			byte[] buff = new byte[32];
			while(fin.read(buff) != -1)
			{
				filetoclient.write(buff,0,buff.length);
			}
			System.out.println("Done with file transfer\n");
			datasocket.close();
			serverdata.close();
			filetoclient.close();
			fin.close();
		} 
		catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println("Finished with transfer of file: "+filename);
	 }
}

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
			String requestedfile;
			int data_port = 1235;

			do
			{
			requestedfile = in.readLine();
			System.out.println("The name of requested file is: "+requestedfile);
			File file = new File(requestedfile);
			if(file.isFile())
			{
				//System.out.println("reached here s1");
				System.out.println("File found, file transfer is about to begin\n");
				out.writeBytes("File found, file transfer is about to begin \r\n");
				
				
				ServerSocket serverdata = new ServerSocket(data_port);
				System.out.println("Waiting for client on port "+ serverdata.getLocalPort()+"...");
				
				
				out.writeInt(data_port);
				Socket datasocket = serverdata.accept();								//datasocket for transferring the file data over to the client
				//DataOutputStream filetoclient = new DataOutputStream (datasocket.getOutputStream());
				//out.writeBytes("The data connection for file transfer successfully established \r\n");
				
				Thread thread=new Thread (new ftpserver_runnable(datasocket,serverdata,requestedfile,file));
				thread.start();
				/*
				filetoclient.writeLong(file.length());											//tell the client how large the file transfer is
				
				java.io.FileInputStream fin = new FileInputStream(file);
				byte[] buff = new byte[32];
				while(fin.read(buff) != -1)
				{
					filetoclient.write(buff,0,buff.length);
				}
				System.out.println("Done with file transfer\n");
				datasocket.close();
				filetoclient.close();
				fin.close();*/
			}
			else{
				System.out.println("The requested file not found\n");
				out.writeBytes("File not found\r\n");
			}
			
				
			}
			while(!requestedfile.equals("quit"));
			//System.out.println("The connection to: "+ server.getRemoteSocketAddress()+" has terminated");
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