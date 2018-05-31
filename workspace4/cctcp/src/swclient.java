import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;


public class swclient {

	public static final long RTT = 10000;
	public static final long RTT_offset = 0;
	
	//define the variables needed for the congestion control
	volatile static int lastAck;
	volatile static int cwnd = 1;
	volatile static int ssthresh = 15;
	volatile static int lastgrouppacket = 0;
	volatile static boolean congestion_avoidance = false;
	volatile static boolean done = false;
	volatile static boolean starting = true;
	
	public static void main(String[] args){
		
		String servername = args[0];					//obtain the server name from argument 0
		int serverport = Integer.parseInt(args[1]);		//obtain server's port from argument 1
		
		try{
			System.out.println("Trying connecting to "+ servername+" on port "+ serverport);	
			Scanner src = new Scanner(System.in);
			Socket swclient=new Socket (servername, serverport);				//trys to connect with the new socket
			
			System.out.println("Enter the number of packets to be send (packet only contains the \"sequence number\", and MSS = 1): ");
			int noPackets = src.nextInt();
			long[] timer = new long[noPackets];
			
			long timeOut = RTT + RTT_offset;
			
			DataOutputStream to_server = new DataOutputStream(swclient.getOutputStream());
			
			to_server.writeByte(noPackets);
		    int sent=1;
		    Thread thread=new Thread(new swListener(swclient, noPackets));
		    thread.start();
		    long start=System.currentTimeMillis();
		      
		    while(lastAck < noPackets)
		    {
		    	//System.out.println("last packet acknowledged is: "+lastAck);			//lastAck is updated
		    	while(sent <= noPackets && !done)
		    	{
		    		  //System.out.println("sent - lastAck equals:"+ (sent-lastAck));
		    		  System.out.println("sending packet no (in window):"+sent);
		              to_server.writeByte(sent);
		              timer[sent-1]=System.currentTimeMillis();
		              sent = sent + 1;			//going to send: sent + 1 in next iteration
		              if((sent - lastgrouppacket) > cwnd)
		              {
		            	  done = true;
		              }
		              //System.out.println("the next packet that's waiting to send is: "+sent );
		    	}
		    	if(starting == true && (System.currentTimeMillis() - timer[sent - 2]) > timeOut)
		    	{
		    		System.out.println("timed out in here");
		    		int temp = sent -1;
	    			System.out.println("re-sending packet no: "+ temp);
	    			congestion_avoidance = false;											//enters slow start
	    			ssthresh =  cwnd/2;
	    			cwnd = 1;
	                to_server.writeByte(temp);
		    	}
		    	if( done && !starting && (System.currentTimeMillis() - timer[lastAck -1]) > timeOut)  
		    	{
		    		 System.out.println("the value is equal to = "+ (System.currentTimeMillis() - timer[lastAck -1]));
		    		  sent = lastAck + 1;
		    		  //System.out.println("the last group packet is "+ lastgrouppacket);
		    		  //System.out.println("in time out sent"+ sent);
		    		  congestion_avoidance = false;											//enters slow start
		    		  ssthresh =  cwnd/2;
		    		  cwnd = 1;
		    		  //for(int i=temp +1;i < temp+cwnd && i<=noPackets;i++)
		    		 // {
		    			  System.out.println("timed out");
		    			  System.out.println("re-sending packet no:"+ sent);
		                  to_server.writeByte(sent);
		                  timer[sent-1]=System.currentTimeMillis();
		                  done = true;
		                  lastgrouppacket = lastAck;
		                  
		                  sent = sent +1;				//the packet that will be sent next
		                  starting = true;
		    		  //}
		    		  
		    	  }
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
	    swclient.lastAck =ackNum;
	    //System.out.println("entered here!");
	    if(cwnd == 1)
	    	starting = false;
	    if(ackNum - lastgrouppacket == cwnd)
	    {
	    	//System.out.println("last group packet value is: "+lastgrouppacket);
	    	//System.out.println("the difference is equal to: "+(ackNum - lastgrouppacket));
	    	lastgrouppacket = ackNum;
	    	if(congestion_avoidance)
	    	{
	    		cwnd = cwnd + 1;
	    	}
	    	else
	    	{
	    		cwnd = 2 * cwnd;
	    		if(swclient.cwnd >= swclient.ssthresh)
				{
					swclient.congestion_avoidance = true;
				}
	    	}
	    	done = false;
	    }
	    
	   // System.out.println("the sent variable is"+ lastAck);

	}
}
