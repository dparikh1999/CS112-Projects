package apps;

import structures.Graph;
import structures.Vertex;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

public class MSTDriver {
	public static void main(String[] args) throws IOException{
		Graph test1 = new Graph("graph4.txt");
		PartialTreeList list1 = MST.initialize(test1);
		Iterator<PartialTree> iter = list1.iterator();
		System.out.println("initialize method ------------------");
		while (iter.hasNext()) {
			PartialTree pt = iter.next();
		    System.out.println(pt.toString());
		}
		//System.out.println(list1.remove().toString());
		/*PartialTree removed = list1.remove();
		Vertex v2 = removed.getArcs().getMin().v2; 
		list1.append(removed);
		System.out.println(list1.removeTreeContaining(v2));
		System.out.println("---------------------------");
		Iterator<PartialTree> iters = list1.iterator();
		   while (iters.hasNext()) {
		       PartialTree pt = iters.next();
		       System.out.println(pt.toString());
		}*/
		System.out.println("execute method ------------------");
		ArrayList<PartialTree.Arc> ptarcs = MST.execute(list1);
		System.out.println(ptarcs.toString());
		/*for (int i = 0;i<ptarcs.size();i++){
			System.out.println(ptarcs.get(i));
		]*/
		System.out.println("remaining list after merging ------------------");
		Iterator<PartialTree> iters = list1.iterator();
		   while (iters.hasNext()) {
		       PartialTree pt = iters.next();
		       System.out.println(pt.toString());
		}
	}
}
