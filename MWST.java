/* MWST.java
   CSC 226 - Spring 2015
   Assignment 1 - Minimum Weight Spanning Tree Template
   
   This template includes some testing code to help verify the implementation.
   To interactively provide test inputs, run the program with
	java MWST
	
   To conveniently test the algorithm with a large input, create a text file
   containing one or more test graphs (in the format described below) and run
   the program with
	java MWST file.txt
   where file.txt is replaced by the name of the text file.
   
   The input consists of a series of graphs in the following format:
   
    <number of vertices>
	<adjacency matrix row 1>
	...
	<adjacency matrix row n>
	
   Entry A[i][j] of the adjacency matrix gives the weight of the edge from 
   vertex i to vertex j (if A[i][j] is 0, then the edge does not exist).
   Note that since the graph is undirected, it is assumed that A[i][j]
   is always equal to A[j][i].
	
   An input file can contain an unlimited number of graphs; each will be 
   processed separately.

   Starter code: B. Bird

   @Chenkai Hao
*/

import java.util.Arrays;
import java.util.Scanner;
import java.util.Vector;
import java.io.File;
import java.util.*;

//Do not change the name of the MWST class
public class MWST{
//    private List<Edge> edges;
//    public static final int MAX_VALUE = 999;
//    
//    private int[] visited;
//    private int spanning_tree[][];
    
    
    /* mwst(G)
		Given an adjacency matrix for graph G, return the total weight
		of all edges in a minimum weight spanning tree.
		
		If G[i][j] == 0, there is no edge between vertex i and vertex j
		If G[i][j] > 0, there is an edge between vertices i and j, and the
		value of G[i][j] gives the weight of the edge.
		No entries of G will be negative.
	*/
    
	static int MWST(int[][] G){
        
        int numVerts = G.length;
        List<Edge> edges = new LinkedList<Edge>();
        int[][] spanning_tree = new int[numVerts][numVerts];
        //List<Edge> spanning_tree = new LinkedList<Edge>();
 
		/* Find a minimum weight spanning tree by Kruskal's algorithm */
		/* (You may add extra functions if necessary) */
		
		/* ... Your code here ... */

      
        for(int i = 0; i < numVerts; i++){
            for(int j = 0; j < numVerts; j++){
                if(G[i][j] != 0 && i != j){
                    Edge edge = new Edge();
                    edge.sourceVer = i;
                    edge.destVer = j;
                    edge.weight = G[i][j];
                    edges.add(edge);
                    G[j][i] = 0;

                }
            }
        }
        
        Collections.sort(edges, new EdgeComparator());
        UnionFind uf = new UnionFind(numVerts);
    

        
        
        int index = 0;
        for(Edge edge : edges){
            //System.out.println(edge.weight);
            //check if source and dest are in the same components
            //if yes ignore the edge
            //if no add the edge to the spanning tree
            

            if(uf.find(edge.sourceVer) != uf.find(edge.destVer)){
                spanning_tree[edge.sourceVer][edge.destVer] = edge.weight;
                spanning_tree[edge.destVer][edge.sourceVer] = edge.weight;
                
                index++;
                
                if(index == numVerts-1){
                    break;
                }
                
                uf.union(edge.sourceVer, edge.destVer);
            
            }
            
            
            
        }
        
        
		
		/* Add the weight of each edge in the minimum weight spanning tree
		   to totalWeight, which will store the total weight of the tree.
		*/
        int totalWeight = 0;

		/* ... Your code here ... */
        for(int i=0; i<numVerts; i++){
            for(int j=i; j<numVerts; j++){
                totalWeight = totalWeight + spanning_tree[i][j];
            }
            
        }
		return totalWeight;
		
	}

public static class Edge{
    
    public int sourceVer;
    public int destVer;
    public int weight;
    
}
    
public static class EdgeComparator implements Comparator<Edge>{
        
    public int compare(Edge edge1, Edge edge2){
        
        if(edge1.weight < edge2.weight){
            return -1;
        }
        if(edge1.weight > edge2.weight){
            return 1;
        }
        return 0;
    }
}
    
public static class UnionFind{
    
    private int[] parent;
    
    public UnionFind(int numVerts){
        parent = new int[numVerts];
        for(int i=0; i<numVerts; i++){
            parent[i]=i;
        }
    }
    
    public int find(int i){
        int p = parent[i];
        if(i==p){
            return i;
        }
        return parent[i] = find(p);
    }
    
    public void union(int i, int j){
        int root1 = find(i);
        int root2 = find(j);
        parent[root1] = root2;
        
    }
}

    
	/* main()
	   Contains code to test the MWST function. You may modify the
	   testing code if needed, but nothing in this function will be considered
	   during marking, and the testing process used for marking will not
	   execute any of the code below.
	*/
	public static void main(String[] args){
		Scanner s;
		if (args.length > 0){
			try{
				s = new Scanner(new File(args[0]));
			} catch(java.io.FileNotFoundException e){
				System.out.printf("Unable to open %s\n",args[0]);
				return;
			}
			System.out.printf("Reading input values from %s.\n",args[0]);
		}else{
			s = new Scanner(System.in);
			System.out.printf("Reading input values from stdin.\n");
		}
		
		int graphNum = 0;
		double totalTimeSeconds = 0;
		
		//Read graphs until EOF is encountered (or an error occurs)
		while(true){
			graphNum++;
			if(graphNum != 1 && !s.hasNextInt())
				break;
			System.out.printf("Reading graph %d\n",graphNum);
			int n = s.nextInt();
			int[][] G = new int[n][n];
			int valuesRead = 0;
			for (int i = 0; i < n && s.hasNextInt(); i++){
				for (int j = 0; j < n && s.hasNextInt(); j++){
					G[i][j] = s.nextInt();
					valuesRead++;
				}
			}
			if (valuesRead < n*n){
				System.out.printf("Adjacency matrix for graph %d contains too few values.\n",graphNum);
				break;
			}
			long startTime = System.currentTimeMillis();
			
			int totalWeight = MWST(G);
			long endTime = System.currentTimeMillis();
			totalTimeSeconds += (endTime-startTime)/1000.0;
			
			System.out.printf("Graph %d: Total weight is %d\n",graphNum,totalWeight);
		}
		graphNum--;
		System.out.printf("Processed %d graph%s.\nAverage Time (seconds): %.2f\n",graphNum,(graphNum != 1)?"s":"",(graphNum>0)?totalTimeSeconds/graphNum:0);
	}
}
