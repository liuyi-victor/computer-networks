class Edge {
	public Node target;		//destination node
	public final double weight;		//the delay, in ms
	public Edge(Node argTarget, double argWeight)		//constructor to create an instance
	{
		target = argTarget;
		weight = argWeight;
	}
}
