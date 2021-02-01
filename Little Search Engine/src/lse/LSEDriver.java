package lse;
import java.io.*; 
import java.util.*;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Scanner;
import java.util.Set;
import java.util.StringTokenizer;

public class LSEDriver {
	static Scanner stdin = new Scanner(System.in);
	
	public static void main(String[] args) throws FileNotFoundException {
		LittleSearchEngine test = new LittleSearchEngine();
		test.makeIndex("docs.txt", "noisewords.txt");
		/*for (String hi: test.keywordsIndex.keySet()) {
			System.out.println(hi+" "+test.keywordsIndex.get(hi));
		}*/
		System.out.println(test.getKeyword("either:or"));
		//System.out.println(test.top5search("alice", "deep"));
		
		/*ArrayList<Occurrence> occs = new ArrayList<Occurrence>();
		occs.add(new Occurrence("doc1.txt", 20));
		occs.add(new Occurrence("doc2.txt", 15));
		occs.add(new Occurrence("doc3.txt", 14));
		occs.add(new Occurrence("doc4.txt", 12));
		occs.add(new Occurrence("doc5.txt", 12));
		occs.add(new Occurrence("doc6.txt", 10));
		occs.add(new Occurrence("doc7.txt", 8));
		occs.add(new Occurrence("doc8.txt", 10));
	
		ArrayList<Integer> seq = test.insertLastOccurrence(occs);
		System.out.println(seq.toString());*/
	
	}
}

