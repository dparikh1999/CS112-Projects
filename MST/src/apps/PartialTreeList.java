package apps;

import java.util.Iterator;
import java.util.NoSuchElementException;

import structures.Vertex;


public class PartialTreeList implements Iterable<PartialTree> {
    
	/**
	 * Inner class - to build the partial tree circular linked list 
	 * 
	 */
	public static class Node {
		/**
		 * Partial tree
		 */
		public PartialTree tree;
		
		/**
		 * Next node in linked list
		 */
		public Node next;
		
		/**
		 * Initializes this node by setting the tree part to the given tree,
		 * and setting next part to null
		 * 
		 * @param tree Partial tree
		 */
		public Node(PartialTree tree) {
			this.tree = tree;
			next = null;
		}
	}

	/**
	 * Pointer to last node of the circular linked list
	 */
	private Node rear;
	
	/**
	 * Number of nodes in the CLL
	 */
	private int size;
	
	/**
	 * Initializes this list to empty
	 */
    public PartialTreeList() {
    	rear = null;
    	size = 0;
    }

    /**
     * Adds a new tree to the end of the list
     * 
     * @param tree Tree to be added to the end of the list
     */
    public void append(PartialTree tree) {
    	Node ptr = new Node(tree);
    	if (rear == null) {
    		ptr.next = ptr;
    	} else {
    		ptr.next = rear.next;
    		rear.next = ptr;
    	}
    	rear = ptr;
    	size++;
    }

    /**
     * Removes the tree that is at the front of the list.
     * 
     * @return The tree that is removed from the front
     * @throws NoSuchElementException If the list is empty
     */
    public PartialTree remove() 
    throws NoSuchElementException {
    		/* COMPLETE THIS METHOD */
    		
    		PartialTree removedtree = null;
    		
    		if (size < 1) {
    			throw new NoSuchElementException("Empty List");
    			
    		}else if (size == 1){ //only one node in CLL
    			Node temp = rear;
    			rear = null;
    			size--;
    			temp.next = null;
    			removedtree = temp.tree;
    		}
    		else {
    			Node beg = rear.next; //front of CLL
    			Node temp = beg;
    			rear.next = beg.next;
    			size--;
    			beg.next = null;
    			removedtree = temp.tree;
    			
    		}
    		
    		return removedtree;
    }

    /**
     * Removes the tree in this list that contains a given vertex.
     * 
     * @param vertex Vertex whose tree is to be removed
     * @return The tree that is removed
     * @throws NoSuchElementException If there is no matching tree
     */
    public PartialTree removeTreeContaining(Vertex vertex) 
    throws NoSuchElementException {
    		/* COMPLETE THIS METHOD */
    		
    		if(size == 0 || rear == null){
			throw new NoSuchElementException("No Matching Tree");
		}
    		
    		Vertex root = vertex.getRoot();
    		PartialTree removedtree = null;
		Node ptr = rear.next; //front 
		Node prev = rear;
		do{
			if (ptr.tree.getRoot().equals(root)){ //if match!!!!!!
				removedtree = removenode(ptr, prev, vertex);
				return removedtree;
			}
			ptr = ptr.next;
			prev = prev.next;
		}while(ptr != rear.next);
		throw new NoSuchElementException("No Matching Tree"); //go through entire CLL and no match
     }
    
    private PartialTree removenode(Node ptr, Node prev, Vertex vertex) {
    		if(size == 1){ //only one node in CLL
			rear = null;
			size--;
			return ptr.tree;
		}
		if (vertex.getRoot().equals(rear.tree.getRoot())){ //if vertex matched with rear
			prev.next = rear.next;
			rear = prev;
			size--;
			return ptr.tree;
		}
		prev.next = ptr.next; //all other cases
		size--;
		return ptr.tree;
    }
    
    /**
     * Gives the number of trees in this list
     * 
     * @return Number of trees
     */
    public int size() {
    	return size;
    }
    
    /**
     * Returns an Iterator that can be used to step through the trees in this list.
     * The iterator does NOT support remove.
     * 
     * @return Iterator for this list
     */
    public Iterator<PartialTree> iterator() {
    	return new PartialTreeListIterator(this);
    }
    
    /*public static void printList(PartialTreeList a){
		Iterator<PartialTree> iter = a.iterator();
		   while (iter.hasNext()) {
		       PartialTree pt = iter.next();
		       System.out.println(pt.toString());
		   }
	}*/
    
    private class PartialTreeListIterator implements Iterator<PartialTree> {
    	
    	private PartialTreeList.Node ptr;
    	private int rest;
    	
    	public PartialTreeListIterator(PartialTreeList target) {
    		rest = target.size;
    		ptr = rest > 0 ? target.rear.next : null;
    	}
    	
    	public PartialTree next() 
    	throws NoSuchElementException {
    		if (rest <= 0) {
    			throw new NoSuchElementException();
    		}
    		PartialTree ret = ptr.tree;
    		ptr = ptr.next;
    		rest--;
    		return ret;
    	}
    	
    	public boolean hasNext() {
    		return rest != 0;
    	}
    	
    	public void remove() 
    	throws UnsupportedOperationException {
    		throw new UnsupportedOperationException();
    	}
    	
    }
}


