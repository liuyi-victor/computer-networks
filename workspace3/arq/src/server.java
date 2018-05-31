import java.net.*;
import java.io.*;

public class server{


  public static void main(String[] args)
{
  try
  {
    ServerSocket server_socket = new ServerSocket(1234);
    System.out.println("Waiting for client on port "+ server_socket.getLocalPort()+"...");
    Socket server = server_socket.accept();
    System.out.println("Just connected to "+ server.getRemoteSocketAddress());
    DataOutputStream out = new DataOutputStream (server.getOutputStream());
    DataInputStream in = new DataInputStream(server.getInputStream());
    //BufferedReader reader=new BufferedReader(new InputStreamReader (server.getInputStream()));
    int num= in.readInt();
    int lastAck =0;
    int input_client;
    while(lastAck<num)
    {
       input_client= in.readInt();
       if(input_client==lastAck+1)
       {
         out.writeInt(input_client);
         lastAck=lastAck+1;
       }
    }

    System.out.println("The connection to: "+ server.getRemoteSocketAddress()+" has terminated");
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
