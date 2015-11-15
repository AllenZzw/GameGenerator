import java.util.*;

public class Edge {
	private Hashtable<String, Integer> varMap;
	private Node src;
	private Node dst;

	public Edge(Vector<String> varNameList, Node src, Node dst, String valueStr){
		this.src = src;
		this.dst = dst;
		varMap = new Hashtable<String, Integer>();
		String[] valueList = valueStr.split(", ");
		for( int i = 0; i < varNameList.size(); i++ ){
			varMap.put( varNameList.elementAt(i), Integer.parseInt(valueList[i]));
		}
	}

	public Integer getVar(String varName){
		return varMap.get(varName);
	}

	public Node getSrc() {
		return this.src;
	}

	public Node getDst() {
		return this.dst;
	}
}