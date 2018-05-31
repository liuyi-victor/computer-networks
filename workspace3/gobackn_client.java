import java.net.*;
import java.io.*;
import java.lang.Thread;

public class gobackn_client {

public static int lastAck;

public static void main(String[] args)
{
    String servername = args[0]; //obtain the server name from argument 0
    int serverport = Integer.parseInt(args[1]); //obtain server's port from argument 1


    try
    {
     
      lastAck=0;
      System.out.println("Trying connecting to "+ servername+" on port "+ serverport);
      Scanner src = new Scanner(System.in);
      Socket client=new Socket (servername, serverport);
      while(1){
      System.out.println("Enter number of packets to be sent to the server, 0 to Quit: ");
      int noPackets= scr.nextInt();
      if(noPackets==0)
    	  break;
      System.out.println("Enter the probability of dropping packets (0-100): ");
      int probError= scr.nextInt();
      System.out.println("Enter the window size: ");
      int wSize=scr.nextInt();
      System.out.println("Enter the timeout interval:");
      int timeOut=scr.nextInt();
      int[] timer = new int[wSize];
      DataOutputStream to_server = new DataOutputStream (client.getOutputStream());
      to_server.writeInt(noPackets);
      to_server.writeInt(probError);
      int sent=1;

      Thread thread=new Thread(new Listener(client, noPackets));
      thread.start();
      long start=System.currentTimeMillis();
      while(sent<wSize||sent==wSize)
      {
           System.out.println("sending packet no:"+sent);
           to_server.writeInt(sent);
           sent=sent+1;
           timer[(sent-1)%wSize]=System.currentTimeMillis();
      }

      while(lastAck==0){}

      while (sent<noPackets || sent==noPacket)
      {
          if((sent-lastAck)<wSize ||(sent-lastAck)==wSize)
          {
             System.out.println("sending packet no:"+sent);
             to_server.writeInt(sent);
             while(System.currentTimeMillis()-timer[(sent-1)%wSize]<timeOut&&(lastAck!=sent))
             { }
             if(lastAck==sent)
             {	 
               sent=sent+1;
               timer[(sent-1)%wSize]=System.currentTimeMillis();
             }
          }
      }
      long finish=System.currentTimeMillis();
      long time=(finish-start)/1000000;
      System.out.println("Total time to send all packet is "+time+" seconds");
      System.out.println("All packets have been sent successfully");
      reader.close();
      to_server.close();
      }
      System.out.println("Quit");
      client.close();
      
    }
    catch(IOException e)
   {
     e.getStackTrace();
     e.printStackTrace();
   }

}

public static void setAckNum (int ackNum)
{
    this.lastAck =ackNum;

}


}
