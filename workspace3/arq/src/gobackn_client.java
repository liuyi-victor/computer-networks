import java.net.*;
import java.io.*;
import java.lang.Thread;
import java.util.Scanner;

public class gobackn_client {

volatile static int lastAck;

public static void main(String[] args)
{
    String servername = "128.100.13.139";//args[0]; //obtain the server name from argument 0
    int serverport = 9876;//Integer.parseInt(args[1]); //obtain server's port from argument 1


    try
    {
     
     
      while(true){
    	  lastAck=0;
      System.out.println("Trying connecting to "+ servername+" on port "+ serverport);
      Scanner src = new Scanner(System.in);
      Socket client=new Socket (servername, serverport);
      DataOutputStream to_server = new DataOutputStream (client.getOutputStream());
      //while(true){
      System.out.println("Enter number of packets to be sent to the server, 0 to Quit: ");
      int noPackets= src.nextInt();
      if(noPackets==0)
    	  break;
      System.out.println("Enter the probability of dropping packets (0-100): ");
      int probError= src.nextInt();
      System.out.println("Enter the window size: ");
      int wSize=src.nextInt();
      System.out.println("Enter the timeout interval:");
      int timeOut=src.nextInt();
      long[] timer = new long[wSize];
     // DataOutputStream to_server = new DataOutputStream (client.getOutputStream());
  
      to_server.writeByte(noPackets);
      to_server.writeByte(probError);
      int sent=1;
      Thread thread=new Thread(new Listener(client, noPackets));
      thread.start();
      long start=System.currentTimeMillis();
      /*while(sent<wSize||sent==wSize)
      {
           System.out.println("sending packet no:"+sent);
           to_server.writeInt(sent);
           sent=sent+1;
           timer[(sent-1)%wSize]=(int) System.currentTimeMillis();
      }

      while(lastAck==0){}*/
/*
      while (sent<noPackets || sent==noPackets)
      {
          if((sent-lastAck)<wSize ||(sent-lastAck)==wSize)
          {
            // if(System.currentTimeMillis()-timer)
        	 System.out.println("sending packet no:"+sent);
             to_server.writeInt(sent);
             sent=sent+1;
             timer[(sent-1)%wSize]=(int) System.currentTimeMillis();
            
          }
      }
      
    */
      
      while(lastAck < noPackets)
      {
    	  //System.out.println("last packet acknowledged is: "+lastAck);			//lastAck is updated
    	  if(sent <= noPackets && ((sent - lastAck)<= wSize))
    	  {
    		  //System.out.println("sent - lastAck equals:"+ (sent-lastAck));
    		  System.out.println("sending packet no (in window):"+sent);
              to_server.writeByte(sent);
              timer[(sent-1)%wSize]=System.currentTimeMillis();
              sent = sent + 1;			//going to send: sent + 1 in next iteration
              //System.out.println("the next packet that's waiting to send is: "+sent );
    	  }
    	  if( (System.currentTimeMillis() - timer[(lastAck) % wSize]) > timeOut)
    		  
    	  {
    		  int temp=lastAck;
    		  //System.out.println("in time out last ack"+ (lastAck));
    		  //System.out.println("in time out sent"+ sent);
    		  for(int i=temp +1;i < temp+wSize && i<=noPackets;i++)
    		  {
    			  //System.out.println("timed out");
    			  //System.out.println("re-sending packet no:"+i);
                  to_server.writeByte(i);
                  sent = i;
                  timer[(i-1)%wSize]=System.currentTimeMillis();
    		  }
    		  
    	  }
      }
      
      long finish=System.currentTimeMillis();
      long time=(finish-start)/1000;
      System.out.println("Total time to send all packet is "+time+" seconds");
      System.out.println("All packets have been sent successfully");
      
      to_server.close();
      client.close();
      }
      System.out.println("Quit");
      //to_server.close();
      //client.close();
      
    }
    catch(IOException e)
   {
     e.getStackTrace();
     e.printStackTrace();
   }

}

public static void setAckNum (int ackNum)
{
    gobackn_client.lastAck =ackNum;
   // System.out.println("the sent variable is"+ lastAck);

}


}