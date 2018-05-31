import java.net.*;
import java.io.*;
import java.lang.Thread;

public class swListener implements Runnable{

    private Socket socket;
    private int noPacket;
 //   private String temp; 


    public swListener(Socket socket, int noPacket)
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
				System.out.println("the received ack is:"+ack);
				swclient.setAckNum(ack);
				//System.out.println("entered here after set new ack");
				//System.out.println("the received ack is:"+ack);
				/*if(!swclient.congestion_avoidance)									//slow start
				{
					swclient.cwnd++;
					//System.out.println("reached here!");
					
					if(swclient.cwnd == swclient.ssthresh)
					{
						swclient.congestion_avoidance = true;
					}
				}*/
				/*else															//congestion avoidance
				{
					if(client.sent - client.lastAck == client.cwnd)	
					{
						client.cwnd = client.cwnd + 1;
					}
				}*/
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
