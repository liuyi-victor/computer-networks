import java.net.*;
import java.util.Scanner;
import java.io.*;
import java.lang.Thread;

/*(1) the client trys to connect to the server
 *(2) client requests a file from the server by specifying the file's name(by typing on the keyboard)
 *after successful connection
 *user types file name (System.in)
 *DataInputStream from_server = new DataInputStream (client.getInputStream());
 *DataOutputStream to_server = new DataOutputStream (client.getOutputStream());
 *send the file name to server through socket;
 *if(respond message from server != error)
 *{
 *	File req_file=new File(filename);		//create new file
 *	java.io.FileOutputStream fout = new FileOutputStream(File file);
 *	while()
 *	{
 *		receive the file in chunks;
 *		fout
 *	}
 *}
 *else
 *	print error message (System.out);
 *socket.close();
 *
 *
 *client = control connection
 *data = data connnection
 **/


class ftpclient_runnable implements Runnable {

	Socket data;
	DataInputStream filefromserver;
	String filename;

	 ftpclient_runnable(String servername, int dataport, String f){
			filename = f;
		    try {
				data = new Socket(servername,dataport);
				filefromserver = new DataInputStream (data.getInputStream());
				
			} catch (UnknownHostException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
	 }
	 
	 public void run() {
		long filesize;
		System.out.println("now running the file transfer process in a separate thread!\n");
		try {
			filesize = filefromserver.readLong();
			int remaining = (int) filesize;
			DataInputStream filefromserver = new DataInputStream (data.getInputStream());
			File req_file = new File("/homes/l/liuyi34/"+filename);									//server has found the file, start receiving: creates new file 
			java.io.FileOutputStream fout = new FileOutputStream(req_file);							//file output stream to write data into the file
			byte[] buffer= new byte[32];															//a buffer of size 32
			//System.out.println("reached here c1");
			while(filefromserver.read(buffer) != -1)
			{
				if((filesize/32) < 1)
				{
					fout.write(buffer,0,remaining-1);												//writes (buffer.length) bytes from byte array buffer to output stream
				}
				else{
					fout.write(buffer);
				}
				remaining = remaining - 32;
			}
			fout.close();
		} 
		catch (IOException e) {
			e.printStackTrace();
		}

		System.out.println("Finished with transfer of file: "+filename);
		try {
			data.close();
			filefromserver.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	 }
/*
	 	public void start ()
	 	{
	 		System.out.println("Starting " + threadName );
	 		if (t == null)
	 		{
	 			t = new Thread (this, threadName);
	 			t.start ();
	 		}
	 	}
	 	*/
}
	 
public class client {

	public static void main(String[] args){
	
		String servername = args[0];					//obtain the server name from argument 0
		int serverport = Integer.parseInt(args[1]);		//obtain server's port from argument 1
		/*
		long filesize;
		int remaining;*/
		int dataport;
		
		
		String filename = null;
		
		try{
			System.out.println("Trying connecting to "+ servername+" on port "+ serverport);	
			Scanner src = new Scanner(System.in);
			Socket client=new Socket (servername, serverport);											//trys to connect with the new socket
			BufferedReader reader=new BufferedReader(new InputStreamReader(client.getInputStream()));	//get the server's response from the socket's input stream
			String msg= reader.readLine();
			System.out.println("After successful connection, server's message: "+msg);					//print out server's response message
			
			DataInputStream from_server=new DataInputStream (client.getInputStream());					//client socket stream for reading the data sent from server
			DataOutputStream to_server = new DataOutputStream (client.getOutputStream());				//client socket stream for sending data over to server side
			//long sent_time, respond_time,trip_time; 
			
			do{
			System.out.println("Enter the name of the requested file: ");
			filename = src.nextLine();																	//get the user input from keyboard
			//sent_time = System.currentTimeMillis();
			to_server.writeBytes(filename +"\r\n");														//send the user message to the server
			msg = from_server.readLine();
			
			
			System.out.println(msg);
			
			//reader.close();
			System.out.println("The name of the requested file is: "+filename);
			
			
			if(!msg.equals("File not found"))
			{
				dataport = from_server.readInt();
				Thread thread=new Thread (new ftpclient_runnable(servername,dataport,filename));
				thread.start();
				/*try{
					
					Socket data = new Socket(servername,dataport);
					msg = reader.readLine();
					System.out.println("After successful data connection, server's message: "+msg);
					
					
					filesize = from_server.readLong();
					remaining = (int) filesize;
					DataInputStream filefromserver = new DataInputStream (data.getInputStream());
					File req_file = new File("/homes/l/liuyi34/"+filename);									//server has found the file, start receiving: creates new file 
					java.io.FileOutputStream fout = new FileOutputStream(req_file);							//file output stream to write data into the file
					byte[] buffer= new byte[32];															//a buffer of size 32
					//System.out.println("reached here c1");
					while(filefromserver.read(buffer) != -1)
					{
						if((filesize/32) < 1)
						{
							fout.write(buffer,0,remaining-1);												//writes (buffer.length) bytes from byte array buffer to output stream
						}
						else{
							fout.write(buffer);
						}
						remaining = remaining - 32;
					}
					System.out.println("Finished with transfer of file: "+filename);
					data.close();
					filefromserver.close();
					fout.close();
					
				}
				catch(IOException e)
				{
					e.getStackTrace();
					e.printStackTrace();
				}
				*/
			}
			else{
				System.out.println("The requested file isn't found on server's file system.\n");
			}
			System.out.println("lalallalala\n");	
		}while(!filename.equals("quit"));	
			//respond_time = System.currentTimeMillis();																		//System.out.println("you just entered: "+client_msg);
			//trip_time = respond_time-sent_time;
			/*
			System.out.println("The server said: "+msg);
			System.out.println("The network round trip time is : "+ trip_time);
			*/
				
			//System.out.println("Closing the connection");		
			from_server.close();
			to_server.close();
			client.close();
			//reader.close();
			
		}
		catch(IOException e)
		{
			e.getStackTrace();
			e.printStackTrace();
		}
		
	}
}