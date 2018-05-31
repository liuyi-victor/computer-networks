import java.net.*;
import java.io.*;

class router implements Comparable<router> {
	public int name;			//node's name
	public double[] link;
	public double minDistance = Double.POSITIVE_INFINITY;		//Minimum weight initially inf
	
	public int previous;
	//public Node previous;		//to keep the path
	public router(int argName)	//constructor to create an instance of this class
	{
		name = argName;
	}
	public int compareTo(router other)
	{
		return Double.compare(minDistance,other.minDistance);
	}
}
