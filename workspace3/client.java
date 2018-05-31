import java.net.*;
import java.io.*;

public class client {

public static void main(String[] args){
   String servername = args[0]; //obtain the server name from argument 0
   int serverport = Integer.parseInt(args[1]); //obtain server's port from argument 1

try{
   System.out.println("Trying connecting to "+ servername+" on port "+ serverport);
   Scanner src = new Scanner(System.in);
   Socket client=new Socket (servername, serverport); //trys to connect with the new
   BufferedReader reader=new BufferedReader(new InputStreamReader (client.getInputStream()));
   int noPackets= src.nextInt();
   DataOutputStream to_server = new DataOutputStream (client.getOutputStream());
   to_server.writeInt(noPackets);
   int sent=1;
   int ack;
   while (sent<noPackets || sent==noPackets)
   {
       to_server.writeInt(sent);
       ack=reader.readInt();
       if(ack==sent)
      {
       sent=sent+1;
      }
   }

   System.out.println("Closing the connection");
   reader.close();
   to_server.close();
   client.close();


}
catch(IOException e)
 {
   e.getStackTrace();
   e.printStackTrace();
 }

}
}
