import java.net.*;
import java.io.*;
//import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.*;


/*
 * 1. establish connection first
 * 2. client specify the number of nodes in the graph and send that number to the server
 * 3. the server sends back an adjacency matrix in array form
 * 4. extract the edge weights from the string and stores them into the matrix
 */
public class RoutingClient {
	
	volatile static int noNodes;
	//static List<Node> nodeList = new ArrayList<Node>();
	volatile static router[][] nodeList;

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
			StringTokenizer processing = new StringTokenizer(adj_link," ");
			String temp;
			//created the adjacency matrix for the nodes in the network graph
			for(int i=0; i < noNodes; i++)
			{
				for(int j=0; j < noNodes; j++)
				{
					temp = processing.nextToken();
					if(i == j)
					{
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
			
			
			nodeList = new router[noNodes][noNodes];
			
			
			
			/*if(nodeList.get(0).neighbors==null)
				System.out.println("it entered here!!! in main");*/
			adjacenyToEdges(matrix);
			//Iterator iter = nodeList.iterator()
			
		/*	for(int t=0; t < noNodes;t++)
			{
				for(int s=0; s < noNodes;s++)
				{
				//*************************************	System.out.println("the shortest distance from node "+t+" to node "+s+" is set to "+nodeList.get(t).neighbors[s].target.minDistance);
					/*if(nodeList.get(t).neighbors[s].weight < Double.POSITIVE_INFINITY && t != s)
						System.out.println("found node "+s+"with previous node set to"+nodeList.get(t).neighbors[s].target.previous.name);*/
					//*******************************for(int z=0; z < noNodes; z++)
					 //*******************System.out.println("based on node t, this weight of the link from node "+t+" to node "+s+" is: "+nodeList.get(t).neighbors[s].target.neighbors[z].weight);				
				//}
			//}
			//there is still something wrong with the nodeList constructed
			
			
			
			
			for(int j=0; j < noNodes; j++)
			{
				System.out.println("Node "+j);
				computePaths(j);
				//for(int i=0; i < noNodes; i++)
				//{
					
				//}
			}
			
		}
		catch(IOException e)
		{
			e.getStackTrace();
			e.printStackTrace();
		}
	}

	
	public static void adjacenyToEdges(double[][] matrix)
	{
		/*
		 * 1. iterate through all of nodes (set as starting points)
		   2. for a particular starting point 
		   		-if there exists a direct path between a node and the starting node then set the minDistance of that node to the weight of link and its previous = starting node
		   3. everything else for will be the same for every starting point
				ie. each node will have a double [noNodes] storing the cost from that particular node to all of the other nodes in the network (graph)
		 */

		for(int i=0; i < noNodes; i++)
		{	
			for(int j=0; j < noNodes; j++)
			{
				nodeList[i][j] = new router(j);
				nodeList[i][j].link = new double[noNodes];
				//nodeList.get(i).neighbors[j] = new Edge(temp.get(j),matrix[i][j]);
				for(int k=0; k < noNodes; k++)
				{
					nodeList[i][j].link[k] = matrix[j][k];
					//if there exists a direct connect between the current node i and one of its neighbors j
					if(matrix[i][j] < Double.POSITIVE_INFINITY)
					{
						//System.out.println("the shortest distance from node "+i+"to node "+ j +" is set to"+matrix[i][j]);
						if(i==j)
						{
							nodeList[i][j].previous = -1;					//a node does not have a previous node for itself
							//nodeList.get(i).neighbors[j].target.previous = null;
						}
						else
						{
							/*make the current node i the previous node to node j since node j is directed connected/reachable by node i 
							 *(assuming node i -> node j is the shortest path from node i to node j)*/
							nodeList[i][j].previous = i;
						}
						nodeList[i][j].minDistance = matrix[i][j];			//must do this since initially all nodes are set with a minDistance = infinity
						//this will cause the minDistance of itself (when i = j) to equal zero
						
					}
				} 
			}
		}
	}

public static void computePaths(int source)
{	
	//everything centered around source point, using source as the starting/relative position
	//nodeList.get(source).neighbors[source].target.minDistance = 0;
	//System.out.println("the source is node "+source);
	PriorityQueue<router> routerQueue = new PriorityQueue<router>();
	router[] list = new router[noNodes];
	for(int i=0; i < noNodes; i++)
	{
		// i = index of all of the nodes in the network (graph) with respect to the the starting point
		list[i] = new router(i);
		list[i].link = new double[noNodes];
		for(int j=0; j < noNodes; j++)
		{
			list[i].link[j] = nodeList[source][i].link[j];
			//System.out.println("this weight of the link from node "+i+" to node "+j+" is: "+nodeList.get(source).neighbors[i].target.neighbors[j].weight);
		}
		list[i].minDistance = nodeList[source][i].minDistance;
		if(nodeList[source][i].previous != -1)
		{
			list[i].previous = nodeList[source][i].previous;
		}
		else
			list[i].previous = -1;
			
	}
	
	//THIS POINT!!!!!!!!!
	/*for(int z=0; z < noNodes; z++)
	{
		System.out.println("this weight of the link to node "+z+" is: "+list[0].link[z]);
	}*/
	for(int i=0; i < noNodes; i++)
	{
		routerQueue.add(list[i]);
	}
	router src;
	//NodeQueue.poll();			//first removes the source node itself since the distance to itself is zero
	
	double distanceThroughsrc;
	
	while(!routerQueue.isEmpty())
	{
		src = routerQueue.poll();				//this is the node that's currently the closest in the current iteration
		
		//**************System.out.println("this extracted node is: "+src.name);
		
		
		
		
		
		
		
		
		
		
		
		
		/*System.out.println("the value of minimum src is "+src.minDistance);
		for(int k=0;k < noNodes; k++)
		{
			System.out.println("while comparing to node distance "+k+" is: "+source.neighbors[k].target.minDistance);
		}*/
		//System.out.println("The first neighbor for node is "+src.neighbors[0].target.name);
		//System.out.println("2. The first neighbor for node 0 is "+nodeList.get(source).neighbors[0].target.name);
		//nodeList.get(source).neighbors[src.name].target = src;
		
		/*for(int t=0; t < noNodes;t++)
			System.out.println("the shortest distance to node "+t+" is set to"+nodeList.get(0).neighbors[t].target.minDistance+"\n");*/
		
		//System.out.println("1. the shortest distance to node 5 is set to"+nodeList.get(0).neighbors[5].target.minDistance+"\n");
		
		//System.out.println("the discoveried shortest is node: "+src.name+" with minDistance = "+src.minDistance);
		
		
		
		
		
		
		
		
		
		
		//*****************************System.out.println("the discoveried shortest is node: "+src.name+" with minimum distance = "+ src.minDistance);
		for(int j=0;j < noNodes;j++)
		{
			//index j represents the destination in this case
			//int target = src.link[j];
			distanceThroughsrc = src.minDistance + src.link[j];
			
			
			
			/*System.out.println("The "+j+"th neighbor for node is "+src.neighbors[j].target.name);
			
			System.out.println("2. the shortest distance to this node is set to "+target.minDistance+" while the distance through source is set to "+distanceThroughsrc+"\n");
			
			
			System.out.println("this src node is "+src.name+" with a minimum distance of "+src.minDistance);*/
			//System.out.println("this value of distance through source for node "+j+" is: "+distanceThroughsrc);
			
			//nodeList.get(source).neighbors[src.name].target.minDistance
			if(distanceThroughsrc < list[j].minDistance)
			{
				//*********************************System.out.println("this dest node = "+j); 
				
				
				//**************************************System.out.println("2. the shortest distance to this node is set to "+src.minDistance+" while the distance through source is set to "+distanceThroughsrc+"\n");
				
				routerQueue.remove(j);
				list[j].minDistance = distanceThroughsrc; 
				list[j].previous = src.name;
				routerQueue.add(list[j]);
				//System.out.println("3. the shortest distance to node 5 is set to"+nodeList.get(0).neighbors[5].target.minDistance+"\n");
			}
		}
	}
	
	
	//Node temp = nodeList.get(0);
/*	for(int k=1; k < noNodes; k++)
	{
		System.out.println("the shortest distance from node 0 to node"+ k +" is "+list[k].minDistance+", with its own name: "+list[k].name);
	}
*/
	
	for(int k=0; k < noNodes; k++)
	{
		System.out.println("Total time to reach Node "+k+" from node "+source+" is: "+list[k].minDistance+" ms");
		getShortestPathTo(list,k);
		//Path: "+getShortestPathTo(nodeList.get(j).neighbors[k].target))
	}
}

public static void getShortestPathTo(router[] list,int target)
{
	/*
	 * 1. put the destination as the first element of the list
	 * 2. iteratively tracing the shortest path found to the destination and adding each node to the list
	 * 3. reverse the list 
	 */
	List<Integer> path = new ArrayList<Integer>();
	path.add(target);
	int traverse = list[target].previous;
	
	if(traverse != -1)
	{		//if the target itself is in case the last and the first element at the same time (going from itself to itself or node unreachable)
	while(list[traverse].previous != -1)
	{
		path.add(traverse);
		traverse = list[traverse].previous;
	}
	//insert the root element
	path.add(traverse);
	}
	
	//List<Integer> reverse = new ArrayList<Integer>();
	int temp;
	//in-place reversal the list path
	for(int i=0,j=path.size()-1; i < j;i++,j--)
	{
		temp = path.get(i);
		path.set(i,path.get(j));
		path.set(j,temp);
	}
	System.out.println("with path: "+path);
}

}



/*
public static void adjacenyToEdges(double[][] matrix)
{

for(int i=0; i < noNodes; i++)
{
	List<Node> temp = new ArrayList<Node>();
	for(int t=0; t < noNodes; t++)
	{
		temp.add(new Node(t));
	}
	
	nodeList.get(i).neighbors = new Edge[noNodes];
	
	for(int j=0; j < noNodes; j++)
	{
		nodeList.get(i).neighbors[j] = new Edge(temp.get(j),matrix[i][j]);
		
		//if there exists a direct connect between the current node i and one of its neighbors j
		if(matrix[i][j] < Double.POSITIVE_INFINITY)
		{
			//System.out.println("the shortest distance from node "+i+"to node "+ j +" is set to"+matrix[i][j]);
			if(i==j)
			{
				nodeList.get(i).neighbors[j].target.previous = null;					//a node does not have a previous node for itself
				//nodeList.get(i).neighbors[j].target.previous = null;
			}
			else
			{
				/*make the current node i the previous node to node j since node j is directed connected/reachable by node i 
				 *(assuming node i -> node j is the shortest path from node i to node j)
				 
				 
				 
				nodeList.get(i).neighbors[j].target.previous = temp.get(i);
			}
			nodeList.get(i).neighbors[j].target.minDistance = matrix[i][j];			//must do this since initially all nodes are set with a minDistance = infinity
			//this will cause the minDistance of itself (when i = j) to equal zero

		}
		/*else
			nodeList.get(i).neighbors[j] = new Edge(nodeList.get(j),0.0);
			
			
			
			
		nodeList.get(i).neighbors[j].target.neighbors = new Edge[noNodes];
		for(int k=0; k < noNodes; k++)
		{
			for(int l=0; l < noNodes; l++)
				nodeList.get(i).neighbors[j].target.neighbors[k] = new Edge(temp.get(l),matrix[k][l]);
		}
	}
}
}

*/