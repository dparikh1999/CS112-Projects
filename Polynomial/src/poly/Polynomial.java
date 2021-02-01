package poly;

import java.io.IOException;
import java.util.Scanner;

/**
 * This class implements evaluate, add and multiply for polynomials.
 * 
 * @author runb-cs112
 *
 */
public class Polynomial {
	
	/**
	 * Reads a polynomial from an input stream (file or keyboard). The storage format
	 * of the polynomial is:
	 * <pre>
	 *     <coeff> <degree>
	 *     <coeff> <degree>
	 *     ...
	 *     <coeff> <degree>
	 * </pre>
	 * with the guarantee that degrees will be in descending order. For example:
	 * <pre>
	 *      4 5
	 *     -2 3
	 *      2 1
	 *      3 0
	 * </pre>
	 * which represents the polynomial:
	 * <pre>
	 *      4*x^5 - 2*x^3 + 2*x + 3 
	 * </pre>
	 * 
	 * @param sc Scanner from which a polynomial is to be read
	 * @throws IOException If there is any input error in reading the polynomial
	 * @return The polynomial linked list (front node) constructed from coefficients and
	 *         degrees read from scanner
	 */
	public static Node read(Scanner sc) 
	throws IOException {
		Node poly = null;
		while (sc.hasNextLine()) {
			Scanner scLine = new Scanner(sc.nextLine());
			poly = new Node(scLine.nextFloat(), scLine.nextInt(), poly);
			scLine.close();
		}
		return poly;
	}
	
	/**
	 * Returns the sum of two polynomials - DOES NOT change either of the input polynomials.
	 * The returned polynomial MUST have all new nodes. In other words, none of the nodes
	 * of the input polynomials can be in the result.
	 * 
	 * @param poly1 First input polynomial (front of polynomial linked list)
	 * @param poly2 Second input polynomial (front of polynomial linked list
	 * @return A new polynomial which is the sum of the input polynomials - the returned node
	 *         is the front of the result polynomial
	 */
	public static Node add(Node poly1, Node poly2) {
		/** COMPLETE THIS METHOD **/
		if (poly1 == null && poly2 == null) {
			return null;
		}if (poly1 == null && poly2 != null) {
			return poly2;
		}if (poly1 != null && poly2 == null) {
			return poly1;
		}
		Node addedNode = null;
		Node front = addedNode;
		
		while (poly1 != null && poly2 != null) {
			if (poly1.term.degree == poly2.term.degree) {
				//adding to new linked list
				float coeffnew = poly1.term.coeff + poly2.term.coeff;
				if (coeffnew == 0) {
					poly1 = poly1.next;
					poly2 = poly2.next;
				}else { 
					if (front == null) {
					addedNode = new Node (coeffnew, poly1.term.degree, null);
					front = addedNode;
					
					}else {
						Node ptr = front;
						while (ptr.next != null) {
							ptr = ptr.next;
						}
						ptr.next = new Node (coeffnew, (poly1.term.degree), null);
					}	
					poly1 = poly1.next;
					poly2 = poly2.next;
				}
			}else if (poly1.term.degree != poly2.term.degree) {
				if (poly1.term.degree > poly2.term.degree) {
					if (front == null) {
						addedNode = new Node ((poly2.term.coeff), (poly2.term.degree), null);
						front = addedNode;
					}else {
						Node ptr = front;
						while (ptr.next != null) {
							ptr = ptr.next;
						}
						ptr.next = new Node ((poly2.term.coeff), (poly2.term.degree), null);
					}
					poly2 = poly2.next;
					
				}else if (poly1.term.degree < poly2.term.degree) {
					if (front == null) {
						addedNode = new Node ((poly1.term.coeff), (poly1.term.degree), null);
						front = addedNode;
					}else {
						Node ptr = front;
						while (ptr.next != null) {
							ptr = ptr.next;
						}
						ptr.next = new Node ((poly1.term.coeff), (poly1.term.degree), null);
					}
					poly1 = poly1.next;
				}
			}if (poly1 == null && poly2 != null) { //if poly1 runs out of terms
				while (poly2 != null) {
					if (front == null) {
						addedNode = new Node ((poly2.term.coeff), (poly2.term.degree), null);
						front = addedNode;
						
					}else {
						Node ptr = front;
						while (ptr.next != null) {
							ptr = ptr.next;
						}
						ptr.next = new Node ((poly2.term.coeff), (poly2.term.degree), null);
					}
					poly2 = poly2.next;
				}
				
			}else if (poly1 != null && poly2 == null) { //if poly2 runs out of terms
				while (poly1 != null) {
					if (front == null) {
						addedNode = new Node ((poly1.term.coeff), (poly1.term.degree), null);
						front = addedNode;
						
					}else {
						Node ptr = front;
						while (ptr.next != null) {
							ptr = ptr.next;
						}
						ptr.next = new Node ((poly1.term.coeff), (poly1.term.degree), null);
					}
					poly1 = poly1.next;
				}
			}
		}
		return front;
	}
	
	/**
	 * Returns the product of two polynomials - DOES NOT change either of the input polynomials.
	 * The returned polynomial MUST have all new nodes. In other words, none of the nodes
	 * of the input polynomials can be in the result.
	 * 
	 * @param poly1 First input polynomial (front of polynomial linked list)
	 * @param poly2 Second input polynomial (front of polynomial linked list)
	 * @return A new polynomial which is the product of the input polynomials - the returned node
	 *         is the front of the result polynomial
	 */
	public static Node multiply(Node poly1, Node poly2) {
		/** COMPLETE THIS METHOD **/
		if (poly1 == null || poly2 == null) {
			return null;
		}
		
		Node currentNode = null;
		Node frontCurrent = null;
		Node finalNode = null;
		Node front = finalNode;
		
		while (poly1 != null && poly2 != null) {
			float multcoeff;
			int multdeg;
			Node ptr2 = poly2; //keeps track of first node in poly2
			
			while (ptr2 != null) {
				multcoeff = poly1.term.coeff*ptr2.term.coeff;
				multdeg = poly1.term.degree + ptr2.term.degree;
				
				if (frontCurrent == null) {
					currentNode = new Node (multcoeff, multdeg, null);
				}else {
					Node pointer = frontCurrent;
					while (pointer.next != null) {
						pointer = pointer.next; //gets to end of list
					}
					pointer.next = new Node (multcoeff, multdeg, null);
				}
				finalNode = add(finalNode, currentNode);
				currentNode = null;
				frontCurrent = currentNode;
				ptr2 = ptr2.next;
			}
			ptr2 = poly2; //back to beg of poly2
			poly1 = poly1.next;
			
		}
		return finalNode;
	}
		
	/**
	 * Evaluates a polynomial at a given value.
	 * 
	 * @param poly Polynomial (front of linked list) to be evaluated
	 * @param x Value at which evaluation is to be done
	 * @return Value of polynomial p at x
	 */
	public static float evaluate(Node poly, float x) {
		/** COMPLETE THIS METHOD **/
		float value = 0;
		if (poly == null) {
			return value;
		}
		while (poly != null) {
			value += (poly.term.coeff)*(Math.pow(x, poly.term.degree));
			poly = poly.next;
		}
		return value;
	}
	
	/**
	 * Returns string representation of a polynomial
	 * 
	 * @param poly Polynomial (front of linked list)
	 * @return String representation, in descending order of degrees
	 */
	public static String toString(Node poly) {
		if (poly == null) {
			return "0";
		} 
		
		String retval = poly.term.toString();
		for (Node current = poly.next ; current != null ;
		current = current.next) {
			retval = current.term.toString() + " + " + retval;
		}
		return retval;
	}	
}
