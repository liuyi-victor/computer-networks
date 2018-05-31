class Node implements Comparable<Node>
{
	public final int name;			//node's name
	public Edge[] neighbors;		//set of neighbors to this node
	public double minDistance = Double.POSITIVE_INFINITY;		//Minimum weight initially inf
	
	public Node previous = null;		//to keep the path
	public Node(int argName)	//constructor to create an instance of this class
	{
		name = argName;
	}
	
	public int compareTo(Node other)
	{
		return Double.compare(minDistance,other.minDistance);
	}
}