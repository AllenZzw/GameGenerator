import java.util.*;

public class Node {
	// signList represent the name of the signature variables
	// nodeNumber is the number of node in automata
	// outEdges are the edges whose source is the node instance
	// signatures are the values of signature variables in this node
	// miss means whether the locations have miss or not 
	private Vector<String> signList;
	private int[] signatures;
	private Integer nodeNumber;
	private Vector<Edge> outEdges;

	public Node(Vector<String> signList, Integer nodeNumber){
		this.signList = signList;
		this.nodeNumber = nodeNumber;
		outEdges = new Vector<Edge>();
	}

	// although I don't know whether the signature set is useful for 
	// automata running or not, but for now we keep this function here
	// the signString is of the form: "1: 0, 0, 0, 1, 0"
	public void setSignature(String signString){
		String signList = signString.split(": ")[1];
		if ( !signList.equals("S")) {
			String[] tmpSignatures = signList.split(", ");
			this.signatures = new int[tmpSignatures.length];
			for(int i = 0; i < this.signatures.length; i++){
				this.signatures[i] = Integer.parseInt(tmpSignatures[i]);
			}
		}
	}

	// function to return the node number
	public Integer NodeNumber(){
		return nodeNumber;
	}

	// function to add an out edge of current node
	public void addEdge(Edge outedge) {
		outEdges.add(outedge);
	}

	// function to return the out edges set
	public Vector<Edge> outEdges(){
		return this.outEdges;
	}
	public void print(){
		System.out.printf("Node " + this.nodeNumber + "\n");
	}

}

