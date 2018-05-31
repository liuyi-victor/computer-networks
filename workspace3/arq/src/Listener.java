import java.net.*;
import java.io.*;
import java.lang.Thread;

public class Listener implements Runnable
{
     private Socket socket;
     private int noPacket;
  //   private String temp; 


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
         DataInputStream reader;
		try {
			reader = new DataInputStream(socket.getInputStream());
			int ack;

			while(true)
			{
				ack=reader.readByte();
				gobackn_client.setAckNum(ack);
				System.out.println("the received ack is:"+ack);
				if(ack==noPacket)
					break;
			}
			//System.out.println("reached here!");
			reader.close();
			socket.close();
		} 
		catch (IOException e) {
			e.printStackTrace();
		}

      }


}