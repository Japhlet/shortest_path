/**
 * Projec05 - Displays shortest paths from one node to anther given a list of nodes and path cost.
 * @author Jesse Franks
 */
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Scanner;
import java.util.Set;
import java.util.TreeMap;


public class Project05 {

	public static void main(String[] args) {
		
		
		Scanner keyboard = new Scanner(System.in);
		System.out.print("Enter a filename with paths: ");
		String fname = keyboard.nextLine();
		System.out.println();
		
		try {
			File file = new File(fname);
			Scanner inFile = new Scanner(file);
			
			Map<String, List<Path>> adj_list = readPathsFromFile(inFile); // Sorts file into adjacency list.
			displayAdjacencyList(adj_list); // Displays adjacency list.
			
			System.out.println();
			System.out.print("Enter a start city (empty line to quit): ");
			String startingLocation = keyboard.nextLine();
			System.out.println();
			
			while(!startingLocation.equals("")){ // Ask user if the want to calculate a route for another city.
				Map<String, Double> foundDistances =findDistances(startingLocation, adj_list);
				
				displayShortes(startingLocation, foundDistances);
				System.out.print("Enter a start city (empty line to quit): ");
				startingLocation = keyboard.nextLine();
			}			
			inFile.close();
			keyboard.close();
			}
			catch (IOException e) {
			System.out.println("Error opening file for reading");
			}	
	}
	
	public static Map<String, List<Path>> readPathsFromFile(Scanner inFile) {
		  Map<String, List<Path>> adj_list =  new TreeMap<String, List<Path>>();

		  while (inFile.hasNext()){ 
		    String input = inFile.nextLine();
		    String[] token = input.split(",");

		    // Get list.
		    List<Path> token0List = adj_list.get(token[0]); 
		    if(token0List == null){ // If there was no list.

		        // Create new list.
		        token0List = new ArrayList<Path>();
		        adj_list.put(token[0], token0List );
		    }
		    
		     Path path = new Path(token[1],Double.parseDouble(token[2])); // Create path.
		     token0List.add(path); 

		    List<Path> token1List = adj_list.get(token[1]);
		    if(token1List == null){ // If there was no list.
		    	
		    	// Create new list.
		        token1List = new ArrayList<Path>();
		        adj_list.put(token[1], token1List );

		    }
		     Path path2 = new Path(token[0],Double.parseDouble(token[2]));// Create path
		    token1List .add(path2); 
		  }
		  return adj_list;
		}
	public static void displayAdjacencyList(Map< String,List<Path> > map){
		
		Set<String> keySet = map.keySet();
		Iterator<String> it = keySet.iterator();
		
	
		System.out.format("%-21s","Start City");
		System.out.format("%-20s","Paths");
		System.out.println();
		System.out.println("--------------       ------------------------------------------------------------------------------------");
		while(it.hasNext()){	
			String key = it.next();
			
			System.out.format("%-20s",key + ":");
			List<Path> list = map.get(key);
			
			for(int i= 0; i < list.size(); i++){
				
				if (i < list.size() - 1){
					System.out.format("%-15s"," ("+list.get(i).getEndpoint() +":"+ list.get(i).getCost() +"), ");
				}else{
					System.out.format("%-15s"," ("+list.get(i).getEndpoint() +":"+ list.get(i).getCost() +") ");
				}
				
			}
			System.out.println();
			
		}
	}
	public static Map<String, Double> findDistances(String start, Map<String,List<Path>> adj_list){
		
		Map<String,Double> shortestDistances = new TreeMap<String,Double>();// This will map names to final path costs.
		PriorityQueue<Path> paths = new PriorityQueue<Path>();// The PriorityQueue will prioritize Paths in order of path cost.
		
		Path startPath = new Path(start, 0.0);
		paths.add(startPath); // Add the Path (start:0.0) to the PriorityQueue
		
		while(!paths.isEmpty()){
			Path current = paths.remove();
			
			if(!shortestDistances.containsKey(current.getEndpoint())){
				double d = current.getCost();
				String dest = current.getEndpoint();
				shortestDistances.put(dest, d);
			
			
			// add more to PQ
				List<Path> next = adj_list.get(dest);
				for(int i = 0; i < next.size();i++){
					double newCost = next.get(i).getCost() + d;
					Path newPath = new Path(next.get(i).getEndpoint(), newCost);
					paths.add(newPath);	
				}
			}	
		}

		return shortestDistances;
		
	}
	public static void displayShortes(String startingLocation, Map<String, Double> foundDistances) {
		Set<String> keySet = foundDistances.keySet();
		Iterator<String> it = keySet.iterator();
		
		System.out.format("Distances from %10s to each city:", startingLocation);
		System.out.println();
		System.out.format("%-15s","Dest. City");
		System.out.format("%-15s","Distance");
		System.out.println();
		System.out.println("-------------- --------------");
		
		while(it.hasNext()){
			String key = it.next();
			System.out.format("%-15s",key);
			System.out.format("%-15s",foundDistances.get(key));
			System.out.println();
			
		}
}
}


