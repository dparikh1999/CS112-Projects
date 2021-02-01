package apps;

import structures.*;
import java.util.ArrayList;

public class MST {
	
	/**
	 * Initializes the algorithm by building single-vertex partial trees
	 * 
	 * @param graph Graph for which the MST is to be found
	 * @return The initial partial tree list
	 */
	public static PartialTreeList initialize(Graph graph) {
		/* COMPLETE THIS METHOD */
		
		PartialTreeList initial = new PartialTreeList(); //step 1: create empty list
		
		for (int i=0; i<graph.vertices.length; i++) { //step 2: sep for each vertex v
			Vertex current = graph.vertices[i];
			PartialTree vertices = new PartialTree(current); //PT containing only v
			current.parent = vertices.getRoot(); 
			MinHeap arcs = vertices.getArcs(); //pq
			
			while (current.neighbors != null) { //insert all arcs
				arcs.insert(new PartialTree.Arc(current, current.neighbors.vertex, current.neighbors.weight));
				current.neighbors = current.neighbors.next;
			}
			initial.append(vertices); //add PT to list
		}
		return initial;
	}


	/**
	 * Executes the algorithm on a graph, starting with the initial partial tree list
	 * 
	 * @param ptlist Initial partial tree list
	 * @return Array list of all arcs that are in the MST - sequence of arcs is irrelevant
	 */
	public static ArrayList<PartialTree.Arc> execute(PartialTreeList ptlist) {
		/* COMPLETE THIS METHOD */
		 
		ArrayList<PartialTree.Arc> result = new ArrayList<PartialTree.Arc>();
		PartialTree PTX = null; 
		MinHeap<PartialTree.Arc> PQX = null; 
		PartialTree.Arc a = null; 
		
		while(ptlist.size() > 1){ //keep going until reaching every vertex
			PTX = ptlist.remove(); //step 3
			PQX = PTX.getArcs(); //pq of PTX
			a = PQX.deleteMin(); //delete highest priority from PQX
			Vertex v1 = a.v1; //vertices of arc a
 			Vertex v2 = a.v2;
			
			while(!PQX.isEmpty()){ //while heap is not empty
				if (PTX.getRoot().equals(v2.getRoot())){ //if v2 belongs to PTX, go back and pick new hp arc
					a = PTX.getArcs().deleteMin(); 
					v2 = a.v2;
				}else {
					break;
				}
			}
			
			PartialTree PTY = ptlist.removeTreeContaining(v2); //remove tree w/ v2 from ptlist
			MinHeap<PartialTree.Arc> PQY = PTY.getArcs();
			PTX.merge(PTY);
			ptlist.append(PTX);
			result.add(a);
		}
		
		return result;
	}
}
