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
         DataInputStream reader;
		try {
			reader = new DataInputStream(socket.getInputStream());
			int ack = 0;

			while(ack<noPacket)
			{
				ack=reader.readByte();
				client.setAckNum(ack);
				System.out.println("the received ack is:"+ack);
				if(!client.congestion_avoidance)									//congestion avoidance
				{
					client.cwnd++;
					//System.out.println("reached here!");
					
					if(client.cwnd == client.ssthresh)
					{
						client.congestion_avoidance = true;
					}
				}
				else															//slow start
				{
					if(client.sent - client.lastAck == client.cwnd)	
					{
						client.cwnd = client.cwnd + 1;
					}
				}
			}
			//
			reader.close();
			socket.close();
		} 
		catch (IOException e) {
			e.printStackTrace();
		}

      }


}