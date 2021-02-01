package app;

import java.io.*;
import java.util.*;
import java.util.regex.*;

import structures.Stack;

public class Expression {

	public static String delims = " \t*+-/()[]";
			
    /**
     * Populates the vars list with simple variables, and arrays lists with arrays
     * in the expression. For every variable (simple or array), a SINGLE instance is created 
     * and stored, even if it appears more than once in the expression.
     * At this time, values for all variables and all array items are set to
     * zero - they will be loaded from a file in the loadVariableValues method.
     * 
     * @param expr The expression
     * @param vars The variables array list - already created by the caller
     * @param arrays The arrays array list - already created by the caller
     */
    public static void 
    makeVariableLists(String expr, ArrayList<Variable> vars, ArrayList<Array> arrays) {
    	/** COMPLETE THIS METHOD **/
    	/** DO NOT create new vars and arrays - they are already created before being sent in
    	 ** to this method - you just need to fill them in.
    	 **/
    		String delimss = " \t*+-/()]";
    		String newexpr = expr.replaceAll("\\s+","");
    		//System.out.println(newexpr);
    		
    		StringTokenizer tokens = new StringTokenizer(newexpr, delimss);
    		List<String> elements = new ArrayList<String>();
    		while (tokens.hasMoreTokens()) {
    			String a = tokens.nextToken();
    			elements.add(a);
 
    		}/*System.out.println("\nPrinting elements in ArrayList ...");
            for(String element : elements) {
                System.out.println(element);
            }System.out.println("varz[".indexOf("["));*/
            
    		for (int i=0; i<elements.size(); i++) {
    			String current = elements.get(i);
    			if (current.charAt(0) >= 48 && current.charAt(0) <= 57) { //if # then don't count
    				continue;
    			}
    			if (current.indexOf("[") == -1) { //no [ means it's variable
    				Variable v1 = new Variable (current);
    				if (vars.contains(v1)) { //checks for duplicates
    					continue;
    				}else {
    					vars.add(v1);
    				}
    				
    			}else { //means it's array
    				String bracket = "[";
    				StringTokenizer arz = new StringTokenizer(current, bracket);
    				List<String> strs = new ArrayList<String>();
    				while (arz.hasMoreTokens()) {
    	    				strs.add(arz.nextToken());
    	    			}/*System.out.println("Printing items in strs");
    	    			for (int e=0; e<strs.size(); e++) {
    	    				System.out.println(strs.get(e));
    	    				System.out.println("size of strs is: "+strs.size());
    	    			}*/
    	    			
    				if (strs.size()==1) { //just one array like "vars["
    	    				Array aa2 = new Array(strs.get(0));
	    				if (arrays.contains(aa2)){ //checks for duplicates
	    					continue;
	    				}else {
	    					arrays.add(aa2);
	    				}
	    				continue;
    	    			}else {
	    				for (int a=0; a<strs.size()-1; a++) {
	    					Array aa2 = new Array(strs.get(a));
	    					if (arrays.contains(aa2)){ //checks for duplicates
	    						continue;
	    					}else {
	    						arrays.add(aa2);
	    					}
	    				}
    				}
    				if (strs.get(strs.size()-1).charAt(0) >= 48 && strs.get(strs.size()-1).charAt(0) <= 57) {
						continue;
				}else { //must be letter or word
					Variable vv2 = new Variable (strs.get(strs.size()-1));
					if (vars.contains(vv2)) { //checks for duplicates
						continue;
					}else {
						vars.add(vv2);
					}
				}
    			}
    		}
    }
    
    /**
     * Loads values for variables and arrays in the expression
     * 
     * @param sc Scanner for values input
     * @throws IOException If there is a problem with the input 
     * @param vars The variables array list, previously populated by makeVariableLists
     * @param arrays The arrays array list - previously populated by makeVariableLists
     */
    public static void 
    loadVariableValues(Scanner sc, ArrayList<Variable> vars, ArrayList<Array> arrays) 
    throws IOException {
        while (sc.hasNextLine()) {
            StringTokenizer st = new StringTokenizer(sc.nextLine().trim());
            int numTokens = st.countTokens();
            String tok = st.nextToken();
            Variable var = new Variable(tok);
            Array arr = new Array(tok);
            int vari = vars.indexOf(var);
            int arri = arrays.indexOf(arr);
            if (vari == -1 && arri == -1) {
            	continue;
            }
            int num = Integer.parseInt(st.nextToken());
            if (numTokens == 2) { // scalar symbol
                vars.get(vari).value = num;
            } else { // array symbol
            	arr = arrays.get(arri);
            	arr.values = new int[num];
                // following are (index,val) pairs
                while (st.hasMoreTokens()) {
                    tok = st.nextToken();
                    StringTokenizer stt = new StringTokenizer(tok," (,)");
                    int index = Integer.parseInt(stt.nextToken());
                    int val = Integer.parseInt(stt.nextToken());
                    arr.values[index] = val;              
                }
            }
        }
    }
    
    //come up w way to differentiate A[a[b]+a[b]] from A[b]+a[b]
    private static boolean onlyOneArray(String expr) {
    		if (expr.charAt(0)>=48 && expr.charAt(0)<=57) { //makes sure doesn't start with number
    			return false;
    		}if (expr.charAt(expr.length()-1)!=']') { //makes sure ends with ]
    			return false;
    		}if (expr.contains("[")==true) {
    			//int indexbracket = expr.indexOf("[");
    			Stack<Character> bracket = new Stack();
    			for (int i=0; i<expr.length(); i++) {
    				if (expr.charAt(i)=='[') {
    					bracket.push(expr.charAt(i));
    				}else if (expr.charAt(i)=='+' || expr.charAt(i)=='-'
    						|| expr.charAt(i)=='*' || expr.charAt(i)=='/') {
    					if (bracket.isEmpty()==true) {
    						return false;
    					}else {
    						bracket.push(expr.charAt(i));
    					}
    				}else if (expr.charAt(i)==']') {
    					while (bracket.peek()!='[') {
    						bracket.pop();
    					}bracket.pop(); //pop [
    				}
    			}
    			
    			/*int closingbracket = expr.indexOf("]");
    			if (expr.indexOf("[", closingbracket)!=-1) { //check if only one array
    				return false;
    			}*/
    		}
    		/*if (expr.contains("+")==true) {
    			if (!(expr.indexOf("+")>expr.indexOf("[") && expr.indexOf("+")<expr.indexOf("]"))) {
    				return false;
    			}
    		}if (expr.contains("-")==true) {
    			if (!(expr.indexOf("-")>expr.indexOf("[") && expr.indexOf("-")<expr.indexOf("]"))) {
    				return false;
    			}
    		}if (expr.contains("*")==true) {
    			if (!(expr.indexOf("*")>expr.indexOf("[") && expr.indexOf("*")<expr.indexOf("]"))) {
    				return false;
    			}
    		}if (expr.contains("/")==true) {
    			if (!(expr.indexOf("/")>expr.indexOf("[") && expr.indexOf("/")<expr.indexOf("]"))) {
    				return false;
    			}
    		}if (expr.contains("(")==true) {
    			if (!(expr.indexOf("(")>expr.indexOf("[") && expr.indexOf("(")<expr.indexOf("]"))) {
    				return false;
    			}
    		}*/
    		return true;
    }
    
    /**
     * Evaluates the expression.
     * 
     * @param vars The variables array list, with values for all variables in the expression
     * @param arrays The arrays array list, with values for all array items
     * @return Result of evaluation
     */
    public static float 
    evaluate(String expr, ArrayList<Variable> vars, ArrayList<Array> arrays) {
    	/** COMPLETE THIS METHOD **/
    	//System.out.println(onlyOneArray(expr));
    	expr = expr.replaceAll("\\s+",""); //getting rid of spaces
    	
    	if (expr.contains("+")==false && expr.contains("-")==false && 
    			expr.contains("*")==false && expr.contains("/")==false && expr.contains("(")==false) {
            if (expr.charAt(0) >= 48 && expr.charAt(0) <= 57) {
            		return Float.parseFloat(expr);
    			}else {
    				if (expr.contains("[")==false && expr.contains("(")==false) { //just one variable
    					Variable var = new Variable(expr);
    					int indexvar = vars.indexOf(var);
    					return vars.get(indexvar).value;
    				}
    				if (expr.contains("[")==true) { //single #, variable, or array inside brackets
    					int indexbracket = expr.indexOf("[");
    					Array arr = new Array(expr.substring(0, indexbracket));
    					int indexarr = arrays.indexOf(arr);
    					if (expr.indexOf("[", indexbracket+1) == -1) { //no nested array
    						int indexclosing = expr.indexOf("]");
    						float inbracket = evaluate((expr.substring(indexbracket+1, indexclosing)), vars, arrays);
    						//System.out.println(inbracket);
    						return arrays.get(indexarr).values[(int) inbracket]; 
    					}
    					int indexclosing = expr.indexOf("]");
    					int b;
    					for (b=expr.length()-1; expr.charAt(b) != '['; b--) { //gets innermost [ bracket
    						continue;
    					}
    					for (b=b-1; expr.charAt(b) != '['; b--) { //gets secondmost [ bracket
    						continue;
    					}
    					//System.out.println(b);
    					String beg = expr.substring(0, b+1);
    					String inside = expr.substring(b+1, indexclosing+1);
    					String outerbracket = "]";
    					String condense = beg+evaluate(inside, vars, arrays)+outerbracket;
    					return evaluate(condense, vars, arrays);
    				}
    			}
    		}
    	
    	//find a better if statement for evaluating A[2+3]
    	//FIX THISSSSSSSSSSS
    if (expr.contains("[")==true && onlyOneArray(expr)==true /*&& expr.contains("(")==false*/) { //array w/ operators inside   
    //System.out.println("hi");
    	int indexbracket = expr.indexOf("[");
    			Array arr = new Array(expr.substring(0, indexbracket));
    			int indexarr = arrays.indexOf(arr);
    			if (expr.indexOf("[", indexbracket+1) == -1) { //no nested array
    				int indexclosing = expr.indexOf("]");
    				float inbracket = evaluate((expr.substring(indexbracket+1, indexclosing)), vars, arrays);
    				//System.out.println(inbracket);
    				return arrays.get(indexarr).values[(int) inbracket]; 
    			}
    			int indexclosing = expr.length()-1;
    			//System.out.println(indexbracket);
    			//System.out.println(indexclosing);
    			String arrayname = expr.substring(0,indexbracket);
    			String inside = expr.substring(indexbracket+1, indexclosing);
    			String condense = arrayname+"["+evaluate(inside, vars, arrays)+"]";
    			return evaluate(condense, vars, arrays);
    			//System.out.println(arrayname +", "+inside);
    			/*int b;
    			for (b=expr.length()-1; expr.charAt(b) != '['; b--) { //gets innermost [ bracket
    				continue;
    			}
    			for (b=b-1; expr.charAt(b) != '['; b--) { //gets secondmost [ bracket
    				continue;
    			}
    			//System.out.println(b);
    			String beg = expr.substring(0, b+1);
    			String inside = expr.substring(b+1, indexclosing+1);
    			String outerbracket = "]";
    			String condense = beg+evaluate(inside, vars, arrays)+outerbracket;
    			return evaluate(condense, vars, arrays);*/
    	    }
    	
    	if (expr.contains("(")==false) {
    		//System.out.println("hey");
    		if (expr.charAt(0)=='-') {
    			if (expr.charAt(0)=='-') {
    				expr = "0"+expr;
    				return evaluate(expr,vars,arrays);
    			}
    		}
	    	int i;
	       for (i = expr.length() - 1; i >= 0; i--) {
	    	   		if (expr.charAt(i)==']') {
	    	   			int x;
	    	   			for (x=i; expr.charAt(x) != '['; x--) {
	    	   				continue;
	    	   			}
	    	   			i=x;
	    	   		}
	            if (expr.charAt(i) == '+' || expr.charAt(i) == '-') {
	                break;
	            } 
	       }if (i<0) {
	    	   		for (i = expr.length() - 1; i >= 0; i--) {
	    	   			if (expr.charAt(i)==']') {
		    	   			int x;
		    	   			for (x=i; expr.charAt(x) != '['; x--) {
		    	   				continue;
		    	   			}
		    	   			i=x;
		    	   		}
		    	   		if (expr.charAt(i) == '*' || expr.charAt(i) == '/') {
		                break;
		            }
	    	   		}
	       }
	       //System.out.println(expr.charAt(i));
	       
	    	String beg = expr.substring(0, i);
	    	String end = expr.substring(i+1, expr.length());
	    //System.out.println(beg +", " + end);
	    	
	    	if (expr.charAt(i)=='+') {
	    		return evaluate(beg, vars, arrays) + evaluate(end, vars, arrays);
	    	}else if (expr.charAt(i)=='-') {
	    		if (expr.charAt(i-1)=='-') {
	    			return evaluate(expr.substring(0, i-1), vars, arrays) + evaluate(end, vars, arrays);
	    		}if (expr.charAt(i-1)=='+') {
	    			return evaluate(expr.substring(0, i-1), vars, arrays) - evaluate(end, vars, arrays);
	    		}if (expr.charAt(i-1)=='*') {
	    			return -(evaluate(expr.substring(0, i-1), vars, arrays) * evaluate(expr.substring(i+1), vars, arrays));
	    		}if (expr.charAt(i-1)=='/') {
	    			return -(evaluate(expr.substring(0, i-1), vars, arrays) / evaluate(expr.substring(i+1), vars, arrays));
	    		}
	    		return evaluate(beg, vars, arrays) - evaluate(end, vars, arrays);
	    	}else if (expr.charAt(i)=='*') {
	    		return evaluate(beg, vars, arrays) * evaluate(end, vars, arrays);
	    	}else if (expr.charAt(i)=='/') {
	    		float denom = evaluate(end, vars, arrays);
	    		if (denom != 0) {
	    			return evaluate(beg, vars, arrays) / denom;
	    		}
	    	}
    }
    
    if (expr.contains("(")==true) {
    		Stack<String> operators = new Stack<String>();
    		Stack<String> operands = new Stack<String>();
    		int startp=0;
    		int endp=0;
    		for (int i=expr.indexOf("("); i<expr.length(); i++) {
    			if (expr.charAt(i) == '(') { //if opening parenthesis
    				operators.clear();
    				operands.clear();
    				startp = i;
    				operators.push(expr.substring(i,i+1));
    				//System.out.println("operator: " + operators.peek());
    			}else if (expr.charAt(i)=='+' || expr.charAt(i)=='-'
    					|| expr.charAt(i)=='*' || expr.charAt(i)=='/') { //if operator +, -, *, /
    					if (expr.charAt(i)=='-' && (expr.charAt(i-1)=='+' 
    						|| expr.charAt(i-1)=='-' || expr.charAt(i-1)=='*'
    							|| expr.charAt(i-1)=='/' || expr.charAt(i-1)=='(') && (expr.charAt(i+1) 
    							>= 48 && expr.charAt(i+1) <= 57)) { //checking for +-2
	    					int a=i+1;
	    	    				int start=i;
	    	    				while ((expr.charAt(a) >= 48 && expr.charAt(a) <= 57)
	    	    						|| expr.charAt(a)==46) { //get to end of #
	    	    					a++;
	    	    				}
	    	    				operands.push(expr.substring(start, a));
	    	    				i=a-1;
    					}else {
    						operators.push(expr.substring(i, i+1));
    					}
    					
    				//System.out.println("operator: " + operators.peek());
    			}else if (expr.charAt(i) >= 48 && expr.charAt(i) <= 57) { //if it's a #
    				int a=i;
    				int start=i;
    				while ((expr.charAt(a) >= 48 && expr.charAt(a) <= 57)
    						|| expr.charAt(a)==46) { //get to end of #
    					a++;
    				}
    				operands.push(expr.substring(start, a));
    				i=a-1;
    				//System.out.println("operand: "+ operands.peek());
    			}else if ((expr.charAt(i) >= 65 && expr.charAt(i) <= 90)
    					|| (expr.charAt(i) >= 97 && expr.charAt(i) <= 122)) { //if variable (or array)
    				int a=i;
    				int start=i;
    				while ((expr.charAt(a) >= 65 && expr.charAt(a) <= 90)
        					|| (expr.charAt(a) >= 97 && expr.charAt(a) <= 122)) { //get to end of variable or array
    					a++;
    					if (expr.charAt(a)=='[') {
    						while (expr.charAt(a)!=']') {
    							a++;
    						}
    						a++;
    					}
    				}
    				//System.out.println(expr.substring(start, a));
    				operands.push(expr.substring(start, a));
    				i=a-1;
    				//System.out.println("operand: "+ operands.peek());
    			}
    			else if (expr.charAt(i) == ')') { //if closing parenthesis
    				endp = i;
    				break;
    			}
    		}
	    	
	    	String insideparen = "";
    		while (operators.peek().equals("(")==false) {
    			insideparen = (operators.pop() + operands.pop()) + insideparen;
    		}
    		if (operators.peek().equals("(")) { //no operators besides (
    			insideparen = operands.pop() + insideparen;
    		}
	    	if (startp == 0 && endp == expr.length()-1) {
	    		return evaluate(insideparen, vars, arrays);
	    	}
	    
	    	String beg = expr.substring(0, startp);
    		String mid = String.valueOf(evaluate(insideparen, vars, arrays));
	    	String end = expr.substring(endp + 1, expr.length());
	    
	    	String condense = beg + mid + end;
	    	//System.out.println(condense);
    		return evaluate(condense, vars, arrays);
    		
    }
    
    	return 0;
    }
}
