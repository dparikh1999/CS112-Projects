package structures;

import java.util.*;

/**
 * This class implements an HTML DOM Tree. Each node of the tree is a TagNode, with fields for
 * tag/text, first child and sibling.
 * 
 */
public class Tree {
	
	/**
	 * Root node
	 */
	TagNode root=null;
	
	/**
	 * Scanner used to read input HTML file when building the tree
	 */
	Scanner sc;
	
	/**
	 * Initializes this tree object with scanner for input HTML file
	 * 
	 * @param sc Scanner for input HTML file
	 */
	public Tree(Scanner sc) {
		this.sc = sc;
		root = null;
	}
	
	private boolean isTag(TagNode last) {
		if (last.tag.equals("html") || last.tag.equals("body") || last.tag.equals("p") || last.tag.equals("em")
				|| last.tag.equals("b") || last.tag.equals("table") || last.tag.equals("tr") || last.tag.equals("td")
				|| last.tag.equals("ol") || last.tag.equals("ul") || last.tag.equals("li")) {
			return true;
		}else {
			return false;
		}
	}
	
	/**
	 * Builds the DOM tree from input HTML file, through scanner passed
	 * in to the constructor and stored in the sc field of this object. 
	 * 
	 * The root of the tree that is built is referenced by the root field of this object.
	 */
	public void build() {
		if (sc == null) {
			return;
		}
		
		Stack<TagNode> treenodes = new Stack<TagNode>();
		TagNode temp = null;
		
		String line = sc.nextLine(); //html line
		
		while (sc.hasNextLine()==true) { //keep going as long as html text has more lines in it
			if (line.contains("<")==true && line.contains(">")==true && line.contains("/")==false) { //starting tag 
				line = line.replaceAll("<", "");
				line = line.replaceAll(">", ""); //turns <em> into just em
				
				TagNode current = new TagNode(line, null, null); 
				
				if (root==null) { //html tag
					root = new TagNode ("html", null, null); //root has to be html tag
					treenodes.push(root);
				}
				else if (root.firstChild==null) { //child of html tag, body tag
					root.firstChild = current;
				}
				else if (isTag(treenodes.peek())==true) { //two tags in a row
					if (treenodes.peek().firstChild==null){//tag doesn't have child yet
						treenodes.peek().firstChild = current;
					}else { //need to add as sibling
						temp = treenodes.peek().firstChild;
						while (temp.sibling!=null) {
							temp = temp.sibling;
						}
						temp.sibling = current;
					}
				}else if (isTag(treenodes.peek())==false) { //text and then tag, make tag sibling
					temp = treenodes.peek();
					if (temp.sibling==null) {
						temp.sibling = current;
					}else {
						while (temp.sibling!=null) {
							temp = temp.sibling;
						}
						temp.sibling = current;
					}
				}
				treenodes.push(current);
				
			}else if (line.contains("<")==false && line.contains(">")==false) { //text
				TagNode current = new TagNode(line, null, null);
				if (isTag(treenodes.peek())==true) { //becomes a child
					temp = treenodes.peek();
					while (temp.firstChild!=null) {
						temp = temp.firstChild;
					}
					temp.firstChild = current;
				}else { //becomes a sibling
					temp = treenodes.peek();
					while (temp.sibling!=null) {
						temp = temp.sibling;
					}
					temp.sibling = current;
				}
				treenodes.push(current);
			
			}else if (line.contains("</")==true && line.contains(">")==true) { //closing tag
				line = line.replaceAll("</", "");
				line = line.replaceAll(">", "");
				String closing = line;
				
				while (!(closing.equals(treenodes.peek().tag))) { //while closing doesn't equal, pop from stack
					treenodes.pop();
				}treenodes.pop(); //pop one more time to get rid of opening tag
			}
			line = sc.nextLine();
		}
	}

	
	/**
	 * Replaces all occurrences of an old tag in the DOM tree with a new tag
	 * 
	 * @param oldTag Old tag
	 * @param newTag Replacement tag
	 */
	public void replaceTag(String oldTag, String newTag) {
		if (sc==null) {
			return;
		}
		repHelper(root, oldTag, newTag);
	}
	
	private void repHelper(TagNode ptr, String oldTag, String newTag) {
		if (ptr==null) {
			return;
		}if (ptr.firstChild!=null) {
			if (ptr.tag.equals(oldTag)) {
				ptr.tag = newTag;
			}
			repHelper(ptr.firstChild, oldTag, newTag);
		}if (ptr.sibling!=null) {
			if (ptr.tag.equals(oldTag)) {
				ptr.tag = newTag;
			}
			repHelper(ptr.sibling, oldTag, newTag);
		}
		return; 
	}
	
	/**
	 * Boldfaces every column of the given row of the table in the DOM tree. The boldface (b)
	 * tag appears directly under the td tag of every column of this row.
	 * 
	 * @param row Row to bold, first row is numbered 1 (not 0).
	 */
	public void boldRow(int row) {
		if (sc==null) {
			return;
		}
		TagNode table = findTable(root);
		if (table==null) { //not found
			return;
		}
		TagNode boldrow = findRow(table, row); //need to bold all td's under this row
		if (boldrow==null) {
			return;
		}
		TagNode td = boldrow.firstChild; //first column
		TagNode temp = td.firstChild; //text under column
		TagNode addbold = new TagNode("b", null, null);
		td.firstChild = addbold; //new child of td becomes bold tag
		addbold.firstChild = temp; //child of bold tag becomes text
		
		while (td.sibling!=null) { //continue for next columns as well (if they exist)
			td = td.sibling; 
			temp = td.firstChild; 
			addbold = new TagNode("b", null, null);
			td.firstChild = addbold; 
			addbold.firstChild = temp; 
		}
	}
	
	private TagNode findRow(TagNode table, int row) {
		int counter = 0;
		TagNode temp = table;
		if (temp.firstChild!=null && temp.firstChild.tag.equals("tr")) {
			temp = temp.firstChild; //this is the first "tr" tag
			counter++;
			while (counter!=row && temp.sibling!=null) {
				temp = temp.sibling;
				counter++;
			}
			if (counter==row) {
				return temp;
			}
		}	
		return null;
	}
	
	private TagNode findTable(TagNode ptr) { //finds table node and returns it
		if (ptr!=null){
	        if (ptr.tag.equals("table")){
	           return ptr;
	        }else{
	            TagNode tableNode = findTable(ptr.firstChild);
	            if (tableNode == null) {
	                tableNode = findTable(ptr.sibling);
	            }
	            return tableNode;
	         }
	    }else{
	        return null;
	    }
	}
	
	/**
	 * Remove all occurrences of a tag from the DOM tree. If the tag is p, em, or b, all occurrences of the tag
	 * are removed. If the tag is ol or ul, then All occurrences of such a tag are removed from the tree, and, 
	 * in addition, all the li tags immediately under the removed tag are converted to p tags. 
	 * 
	 * @param tag Tag to be removed, can be p, em, b, ol, or ul
	 */
	public void removeTag(String tag) {
		if (tag == null || sc==null) {
			return;
		}
		if (isCategory1(tag)) {
			removeCat1(root, root.firstChild, tag);
		}else if (isCategory2(tag)) {
			removeCat2(root, root.firstChild, tag);
		}
	}
	
	private void removeCat1(TagNode prev, TagNode ptr, String tag) {
		if (ptr==null || prev==null) {
			return;
		}
		if (ptr.tag.equals(tag) && ptr.firstChild!=null) {
			if (prev.firstChild==(ptr)) { //if prev is prior parent of ptr
				//System.out.println("fc");
				
				prev.firstChild = ptr.firstChild;
				TagNode sib = null;
				if (ptr.sibling!=null) {
					sib = ptr.sibling;
				}
				while (ptr.firstChild.sibling != null) { //get last sib
					ptr.firstChild = ptr.firstChild.sibling;
				}
				ptr.firstChild.sibling = sib;
				
				removeCat1(prev.firstChild, prev.firstChild.sibling, tag);
				removeCat1(prev.firstChild, prev.firstChild.firstChild, tag);

			}else if (prev.sibling==(ptr)){ //if prev is prior sibling of ptr
				//System.out.println("sib");
				
				TagNode sib = null;
				if (ptr.sibling!=null) {
					sib = ptr.sibling;
				}
				TagNode temp = ptr.firstChild;
				while (ptr.firstChild.sibling != null) { //get last sib
					ptr.firstChild = ptr.firstChild.sibling;
				}
				ptr.firstChild.sibling = sib;
				prev.sibling = temp;
				
				removeCat1(prev.sibling, prev.sibling.sibling, tag);
				removeCat1(prev.sibling, prev.sibling.firstChild, tag);
			}
			return;
		}
		removeCat1(ptr, ptr.sibling, tag);
		removeCat1(ptr, ptr.firstChild, tag);
	}
	
	private void removeCat2(TagNode prev, TagNode ptr, String tag) {
		if (ptr==null || prev==null) {
			return;
		}
		if (ptr.tag.equals(tag) && ptr.firstChild!=null) {
			if (prev.firstChild==(ptr)) {
				prev.firstChild = ptr.firstChild;
				TagNode sib = null;
				if (ptr.sibling!=null) {
					sib = ptr.sibling;
				}
				while (ptr.firstChild.sibling != null) { //get last sib
					ptr.firstChild = ptr.firstChild.sibling;
				}
				ptr.firstChild.sibling = sib;
				
				removeLi(prev.firstChild);
				
				removeCat2(prev.firstChild, prev.firstChild.sibling, tag);
				removeCat2(prev.firstChild, prev.firstChild.firstChild, tag);
				
			}else if (prev.sibling==(ptr)) {
				TagNode sib = null;
				if (ptr.sibling!=null) {
					sib = ptr.sibling;
				}
				TagNode temp = ptr.firstChild;
				while (ptr.firstChild.sibling != null) { //get last sib
					ptr.firstChild = ptr.firstChild.sibling;
				}
				ptr.firstChild.sibling = sib;
				prev.sibling = temp;
				
				removeLi(prev.sibling);
				
				removeCat2(prev.sibling, prev.sibling.sibling, tag);
				removeCat2(prev.sibling, prev.sibling.firstChild, tag);
			}
			return;
		}
		removeCat2(ptr, ptr.sibling, tag);
		removeCat2(ptr, ptr.firstChild, tag);
	}
	
	private void removeLi(TagNode ptr) {
		if (ptr.tag.equals("li")) {
			ptr.tag = "p";
			ptr = ptr.sibling;
		}
		while (ptr.tag.equals("li") && ptr.sibling!=null) {
			ptr.tag = "p";
			ptr = ptr.sibling;
		}
		if (ptr.tag.equals("li")) {
			ptr.tag = "p";
		} 
		return;
	}
	
	private boolean isCategory1(String tag) {
		if (tag.equals("p") || tag.equals("em") || tag.equals("b")) {
			return true;
		}
		return false;
	}
	
	private boolean isCategory2(String tag) {
		if (tag.equals("ol") || tag.equals("ul")) {
			return true;
		}
		return false;
	}
	
	/**
	 * Adds a tag around all occurrences of a word in the DOM tree.
	 * 
	 * @param word Word around which tag is to be added
	 * @param tag Tag to be added
	 */
	public void addTag(String word, String tag) {
		if (word==null || tag==null || sc==null) {
			return;
		}
		addHelper(root.firstChild, word, tag);
	}
	
	private void addHelper(TagNode ptr, String word, String tag) {
		if (ptr==null) {
			return;
		}else {
			if (ptr.tag.toLowerCase().contains(word.toLowerCase())) {
				
				if (equalsIgnoreCaseandPunct(word, ptr.tag)) { //if just one word in the tag
					String text = ptr.tag; 
					ptr.tag = tag; //tag becomes new tag
					ptr.firstChild = new TagNode(text, null, null); //create child node
					
					if (ptr.sibling!=null) {
						addHelper(ptr.sibling, word, tag); //only need to send it on sibling b/c can't add a tag to a tag
					}	
				}else { //contains word but in a larger amount of text
					
					String[] split = ptr.tag.split("\\s+"); //create array of tokens
					TagNode sib = ptr.sibling;
					
					for (int i=0; i<split.length; i++) { //parse through array
						if (equalsIgnoreCaseandPunct(word, split[i])) {
							String before="";
							for (int x=0; x<i; x++) {
								before = before + split[x] + " ";
							}
							String foundword = split[i];
							String after="";
							for (int y=i+1; y<split.length; y++) {
								after = after + split[y] + " ";
							}
							if (before.equals("")==false && after.equals("")==false) {
								ptr.tag = before;
								ptr.sibling = new TagNode(tag, null, null);
								ptr.sibling.firstChild = new TagNode(foundword, null, null);
								if (sib!=null) {
									ptr.sibling.sibling = new TagNode(after, null, sib);
									addHelper(ptr.sibling.sibling, word, tag);
								}else {
									ptr.sibling.sibling = new TagNode(after, null, null);
									addHelper(ptr.sibling.sibling, word, tag);
								}
								return;
							}
							else if (before.equals("")==true && after.equals("")==false) { //if its first word in sentence
								ptr.tag = tag;
								ptr.firstChild = new TagNode(foundword, null, null);
								if (sib!=null) {
									ptr.sibling = new TagNode(after, null, sib);
									addHelper(ptr.sibling.sibling, word, tag);
								}else {
									ptr.sibling = new TagNode(after, null, null);
									addHelper(ptr.sibling, word, tag);
								}
								return;
							}
							else if (before.equals("")==false && after.equals("")==true) { //if its last word in sentence
								ptr.tag = before;
								ptr.sibling = new TagNode(tag, null, null);
								ptr.sibling.firstChild = new TagNode(foundword, null, null);
								if (sib!=null) {
									ptr.sibling.sibling = sib;
									addHelper(ptr.sibling.sibling, word, tag);
								}
								return;
							}
						}
					}
					addHelper(ptr.firstChild, word, tag);
					addHelper(ptr.sibling, word, tag);
				}
			}else {
				addHelper(ptr.firstChild, word, tag);
				addHelper(ptr.sibling, word, tag);
			}
		}
	}
	
	private boolean equalsIgnoreCaseandPunct(String inputword, String foundword) { //input is what user types, found is what's in text
		if (hasOnePunct(foundword)==true) {
			foundword = foundword.substring(0, foundword.length()-1); //creates substring w/o punctuation at end
		}
		if (foundword.equalsIgnoreCase(inputword)) {
			return true;
		}
		return false;
	}
	
	private boolean hasOnePunct(String word) {
		if (word.charAt(word.length()-1)=='.' || word.charAt(word.length()-1)==',' || word.charAt(word.length()-1)=='?'
				|| word.charAt(word.length()-1)=='!' || word.charAt(word.length()-1)==':' || word.charAt(word.length()-1)==';') {
			if ((word.charAt(word.length()-2) >= 65 && word.charAt(word.length()-2) <= 90) ||
					(word.charAt(word.length()-2) >= 97 && word.charAt(word.length()-2) <= 122)) { //has only one punctuation
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Gets the HTML represented by this DOM tree. The returned string includes
	 * new lines, so that when it is printed, it will be identical to the
	 * input file from which the DOM tree was built.
	 * 
	 * @return HTML string, including new lines. 
	 */
	public String getHTML() {
		StringBuilder sb = new StringBuilder();
		getHTML(root, sb);
		return sb.toString();
	}
	
	private void getHTML(TagNode root, StringBuilder sb) {
		for (TagNode ptr=root; ptr != null;ptr=ptr.sibling) {
			if (ptr.firstChild == null) {
				sb.append(ptr.tag);
				sb.append("\n");
			} else {
				sb.append("<");
				sb.append(ptr.tag);
				sb.append(">\n");
				getHTML(ptr.firstChild, sb);
				sb.append("</");
				sb.append(ptr.tag);
				sb.append(">\n");	
			}
		}
	}
	
	/**
	 * Prints the DOM tree. 
	 *
	 */
	public void print() {
		print(root, 1);
	}
	
	private void print(TagNode root, int level) {
		for (TagNode ptr=root; ptr != null;ptr=ptr.sibling) {
			for (int i=0; i < level-1; i++) {
				System.out.print("      ");
			};
			if (root != this.root) {
				System.out.print("|---- ");
			} else {
				System.out.print("      ");
			}
			System.out.println(ptr.tag);
			if (ptr.firstChild != null) {
				print(ptr.firstChild, level+1);
			}
		}
	}
}
