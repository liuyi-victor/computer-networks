import java.net.*;
import java.util.Scanner;
import java.io.*;
import java.lang.Thread;

public class client {
	
	public static final int RTT = 1000;
	public static final int RTT_offset = 500;
	
	//define the variables needed for the congestion control
	volatile static int lastAck = 0;
	volatile static int cwnd = 1;
	volatile static int ssthresh = 16;
	volatile static int sent=1;
	volatile static boolean congestion_avoidance = false;
	volatile static boolean done = false; 
	
	
	public static void main(String[] args){
		
		String servername = args[0];					//obtain the server name from argument 0
		int serverport = Integer.parseInt(args[1]);		//obtain server's port from argument 1
		
		try{
			System.out.println("Trying connecting to "+ servername+" on port "+ serverport);	
			Scanner src = new Scanner(System.in);
			Socket client=new Socket (servername, serverport);				//trys to connect with the new socket
			
			System.out.println("Enter the number of packets to be send (packet only contains the \"sequence number\", and MSS = 1): ");
			int noPackets = src.nextInt();
			
			
			long timeOut = RTT + RTT_offset;
			long[] timer = new long[noPackets];
			
			DataOutputStream to_server = new DataOutputStream(client.getOutputStream());
			
			
			to_server.writeByte(noPackets);
		    Thread thread=new Thread(new Listener(client, noPackets));
		    thread.start();
		    long start=System.currentTimeMillis();
		      
		    while(lastAck < noPackets)
		    {
		    	//System.out.println("last packet acknowledged is: "+lastAck);			//lastAck is updated
		    	if(sent <= noPackets && ((sent - lastAck)<= cwnd))
		    	{
		    		  System.out.println("sent = "+sent+"; lastAck equals: "+ lastAck+"; and congestion window equals: "+cwnd);
		    		  System.out.println("sending packet no (in window):"+sent);
		              to_server.writeByte(sent);
		              timer[sent-1]=System.currentTimeMillis();
		              sent = sent + 1;			//going to send: sent + 1 in next iteration
		              //System.out.println("the next packet that's waiting to send is: "+sent );
		    	}
		    	/*if((System.currentTimeMillis() - timer[lastAck]) > timeOut)				//time out has occurred
		    	{
		    		int temp=lastAck;
		    		
		    		congestion_avoidance = false;											//enters slow start
		    		ssthresh =  cwnd/2;
		    		cwnd = 1;
		    		
		    		//System.out.println("in time out last ack"+ (lastAck));
		    		//System.out.println("in time out sent"+ sent);
		    		for(int i=temp +1;i <= temp+cwnd&& i<=noPackets;i++)
		    		{
		    			//System.out.println("timed out");
		    			//System.out.println("re-sending packet no:"+i);
		    			to_server.writeByte(i);
		    			sent = i;
		    			timer[sent - 1]=System.currentTimeMillis();
		    		}
		    		  
		    	}*/
		      }
			
		}
		catch(IOException e)
		{
			e.getStackTrace();
			e.printStackTrace();
		}
	}
	
	public static void setAckNum (int ackNum)
	{
	    client.lastAck =ackNum;
	   // System.out.println("the sent variable is"+ lastAck);


	}
}
