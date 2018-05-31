import java.net.*;
import java.io.*;
import java.lang.Thread;

public class Listener implements Runnable
{
     private Socket socket;
     private int noPacket;


     public Listener(Socket socket, int noPacket)
     {
         this.socket = socket;
         this.noPacket = noPacket;
     }
     public void run()
     {
         try {

            handleSocket();
         } catch (Exception e) {
            e.printStackTrace();
         }
      }

       private void handleSocket()
      {
         BufferedReader reader=new BufferedReader(new InputStreamReader (socket.getInputStream()));
         int ack;

         while(1)
         {
             ack=reader.readInt();
             gobackn_client.setAckNum(ack);
             if(ack==noPacket)
            	 break;
         }
         reader.close();
         socket.close();

      }



}
