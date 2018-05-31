import java.net.*;
import java.io.*;
import java.io.IOException;
import java.net.Socket;
import java.util.*;


/*
 * 1. establish connection first
 * 2. client specify the number of nodes in the graph and send that number to the server
 * 3. the server sends back an adjacency matrix in String form
 * 4. extract the edge weights from the string and stores them into the matrix
 */
public class routing {
	
	volatile static int noNodes;
	//volatile static router[][] nodeList;

	public static void main(String[] args){
		
		String servername = args[0];					//obtain the server name from argument 0
		int serverport = Integer.parseInt(args[1]);		//obtain server's port from argument 1
		
		try{
			System.out.println("Trying connecting to "+ servername+" on port "+ serverport);
			Scanner src = new Scanner(System.in);
			Socket client = new Socket (servername, serverport);				//trys to connect with the new socket
			System.out.println("Enter the number of nodes in the network: ");
			noNodes = src.nextInt();
			DataOutputStream to_server = new DataOutputStream (client.getOutputStream());
			to_server.writeByte(noNodes);										//sends the number of nodes to the server
			DataInputStream from_server = new DataInputStream (client.getInputStream());
			String adj_link = from_server.readLine();
			double[][] matrix = new double[noNodes][noNodes];					//graph's adjacency list matrix
			StringTokenizer processing = new StringTokenizer(adj_link," ");		//using the " " (a single space character) as delimiter(separator) to cut/break the string into pieces
			String temp;
			
			//construct the adjacency matrix for the nodes in the network graph
			for(int i=0; i < noNodes; i++)
			{
				for(int j=0; j < noNodes; j++)
				{
					temp = processing.nextToken();
					if(i == j)
					{
						/*
						 * if the i = j (source and the destination are the same node in the graph)
						 * 		explicitly set the value of the distance to zero since the distance from any node to itself is zero 
						 */
						matrix[i][j] = 0.0;
					}
					else
					{
						if(temp.equals("Infinity"))
						{
							matrix[i][j] = Double.POSITIVE_INFINITY;
						}
						else
						{
							matrix[i][j] = Double.parseDouble(temp);
						}
					}
				}
			}
			
		/*
			for(int i=0; i < noNodes; i++)
			{
				for(int j=0; j < noNodes; j++)
				{
					System.out.println("the value of the adjacency matrix at index i,j is "+matrix[i][j]);
				}
			}
			
			the created matrix is correct
			
		*/
					
			
			for(int j=0; j < noNodes; j++)
			{
				List<Node> nodeList = new ArrayList<Node>();
				for(int i = 0; i < noNodes; i++){
					nodeList.add(new Node(i));
				}  
				adjacenyToEdges(matrix,nodeList);
				
				computePaths(nodeList.get(j));
				System.out.println("Node "+j);
				for(int k=0; k < noNodes; k++)
				{
					System.out.println("Total time to reach Node "+k+": "+nodeList.get(j).neighbors[k].target.minDistance+" ms, Path: "+getShortestPathTo(nodeList.get(j).neighbors[k].target));
				}
			}
			
			
		}
		catch(IOException e)
		{
			e.getStackTrace();
			e.printStackTrace();
		}
	}
	
	public static void adjacenyToEdges(double[][] matrix, List<Node> v)
	{
		for(int i = 0; i < noNodes; i++)
		{
			v.get(i).neighbors = new Edge[noNodes];
			for(int j = 0; j < noNodes; j++)
			{
				v.get(i).neighbors[j] =  new Edge(v.get(j), matrix[i][j]);	
			}
		}
	}
	
	public static void computePaths(Node source)						//creates the shortest path graph from source node using Dijkstra's algorithm
	{
		source.minDistance = 0;
		PriorityQueue<Node> NodeQueue = new PriorityQueue<Node>();		//define a priority queue
		Node src;
		NodeQueue.add(source);											//add the source node to the priority queue
		while(!NodeQueue.isEmpty())
		{
			src = NodeQueue.poll();										//poll out the top node of the priority queue, which is currently the nearest node from the list of unvisited nodes
			for(int j=0;j < noNodes;j++)								//for loop used to find if there are any new paths that have shorter distance		
			{
				/*for all of the neighbors of the source node, set that neighbor = target
				 * if we can find a new, shorter path to another node in the graph by passing the target
				 * 		update the minDistance to the new value = target's minDistance + distance from target to that node
				 */
				Node target = src.neighbors[j].target;
				double distanceThroughsrc = src.minDistance + src.neighbors[j].weight;
				if(distanceThroughsrc < target.minDistance)
				{
					NodeQueue.remove(target);
					target.minDistance = distanceThroughsrc;
					target.previous = src;
					NodeQueue.add(target);
				}
			}
		}
	}

	
	public static List<Integer> getShortestPathTo(Node target)
	{
		/*
		 * 1. put the destination as the first element of the list
		 * 2. iteratively tracing the shortest path found to the destination and adding each node to the list
		 * 3. reverse the list 
		 */
		List<Integer> path = new ArrayList<Integer>();
		path.add(target.name);
		Node traverse = target.previous;
		
		if(traverse != null)
		{			
			while(traverse.previous != null)
			{
				path.add(traverse.name);
				traverse = traverse.previous;
			}
			//insert the root element
			path.add(traverse.name);
		}

		int temp;
		//in-place reversal the list path
		for(int i=0,j=path.size()-1; i < j;i++,j--)
		{
			temp = path.get(i);
			path.set(i,path.get(j));
			path.set(j,temp);
		}
		return path;
	}
}
	
	