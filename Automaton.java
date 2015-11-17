import java.io.*;
import java.util.*;
import java.util.regex.*;

public class Automaton {
	private Vector<String> signList;
	private Hashtable<Integer, Node> nodeTable;
	private Hashtable<String, Integer> currentVarMap;
	private Node current;
	private Integer input;
	private Integer nodeNum = null;

	private GameInterface view;

	public Automaton(String filename){
		signList = new Vector<String>();
		nodeTable = new Hashtable<Integer, Node>();
		currentVarMap = new Hashtable<String, Integer>();
		current = null;
		input = 0;
		Vector<String> varNameList = new Vector<String>();
		try{
			InputStream in = new FileInputStream(filename);

			BufferedReader reader = new BufferedReader(new InputStreamReader(in));

			//get the number of nodes
			//the line is of the form: "# Number of nodes = 26"
			String[] tmplist = reader.readLine().split(" ");
			nodeNum = Integer.parseInt(tmplist[tmplist.length-1]);

			//get the variable list
			//the line is of the form: "# engineer1 engineer2 reaction _V0 _V1 _V2 _V3 _V4"
			Scanner scan = new Scanner(reader.readLine());
			scan.next();
			while (scan.hasNext()) {
				String name = scan.next();
				currentVarMap.put(name, 0);
				varNameList.add(name);
			}

			//get the signature list ( or the ordered set of historical variables)
			//the line is of the form: "# _V1 _V2 _V3"
			scan = new Scanner(reader.readLine());
			scan.next();
			while (scan.hasNext()) {
				signList.add(scan.next());
			}

			//construct the node table 
			//the node line is of the form: "0 [label="0: S"];"
			//the edge line is of the form: "0 -> 1 [label="0, 0, 0, 0, 0, 0, 0, 0"];"
			//use regular expression to indentify nodes and edges 
			Pattern nodePattern = Pattern.compile("([0-9]+) \\[label=\"(.*)\"\\];");
			Pattern edgePattern = Pattern.compile("([0-9]+) -> ([0-9]+) \\[label=\"(.*)\"\\];");
			String line = reader.readLine();
			while (line != null){
				Matcher nodematcher = nodePattern.matcher(line);
				if ( nodematcher.matches()) {
					Node node = getNode(Integer.parseInt(nodematcher.group(1)));
					node.setSignature(nodematcher.group(2));
				}
				Matcher edgematcher = edgePattern.matcher(line);
				if ( edgematcher.matches()) {
					Node src = getNode(Integer.parseInt(edgematcher.group(1)));
					Node dst = getNode(Integer.parseInt(edgematcher.group(2)));
					src.addEdge(new Edge(varNameList, src, dst, edgematcher.group(3)));
				}
				line = reader.readLine();
			}
			reader.close();

			//set the start node to be 0 node
			current = getNode(0);
		} catch (FileNotFoundException e ){
			System.err.println("Cannot find the solutions.dot file.");
		} catch (IOException ioe) {
			System.err.println("Cannot read dot file from input stream.");
		} 
	}

	//function for getting node 
	//if node not exists, construct a new node and add to node table
	private Node getNode(Integer nodeNumber){
		Node node = nodeTable.get(nodeNumber);
		if (node == null){
			node = new Node(signList, nodeNumber);
			nodeTable.put(nodeNumber, node);
			if (current == null) {
				current = node;
			}
		}
		return node;
	}

	//function to return currentVarMap
	public Hashtable<String, Integer> currentStatus(){
		return currentVarMap;
	}

	//function for game restart by setting current node to be 0 node 
	public void restart(){
		current = getNode(0);
	}

	//function for game running 
	//stepping to the next node according to aim node
	//false means step fail since there is no edge from current node to aim node
	//success means current node has change to aim node
	public boolean nextStep(){
		//if game is over, then no display update
		
		Vector<Edge> allowEdges = new Vector<Edge>();
		for (Edge edge : this.current.outEdges() ){
			if (edge.getVar("Input") == this.input){
				allowEdges.add(edge);
			}
		}
		if(allowEdges.size() == 0)
			return false;
		int rand = (int) Math.round( Math.random() * (allowEdges.size() -1));
		Edge outEdge = allowEdges.get(rand);
		current = outEdge.getDst();
		Set<String> keys = currentVarMap.keySet();
		for(String key: keys ){
			currentVarMap.put(key, outEdge.getVar(key));
		}
		//for one key one step, input need reset
		//for one key one location, input remains unless changes
		//this.input = 0;
		if(this.currentVarMap.get("GameOver") == 1){
			view.updateDisplay(currentVarMap, true);
		}
		else{
			view.updateDisplay(currentVarMap, false);
		}
		
		return true;
	}

	// function for use to set current input
	public void setInput(Integer input) {
		this.input = input;
	}

	//function to set view 
	public void setView(GameInterface view){
		this.view = view;
	}

	//function to print the current state
	public void printCurrent() {
		current.print();
		for (Map.Entry<String, Integer> entry : currentVarMap.entrySet()) { 
			System.out.println("Key: " + entry.getKey() + ", Value: " + entry.getValue() + ";"); 
		}
	}

}


