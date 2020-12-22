package phaki.optimization.kmeansclustering;

import java.util.ArrayList;

import com.google.maps.model.LatLng;

import phaki.optimization.apicommunication.PhakiAPICommunication;
import phaki.optimization.models.*;

/**
 * 
 * @author Maikel Achi
 * 
 * This class handles the KMeans Location Clustering
 *
 */
public class KMeansLocationClustering 
{
	private static ArrayList<OrderDto> allOrders;
	private static int numTrucks;
	private static ArrayList<Truck> allTrucks; 
	private static ArrayList<Truck> trucksToBeUsed;
	private static ArrayList<LatLng> centroids;
	private static ArrayList<Route> finalClusters;
	private static ArrayList<ArrayList<LatLng>> centroidsPrevLocs;
	
	private static double minLngBound;
	private static double minLatBound;
	private static double maxLngBound;
	private static double maxLatBound;
	
	/**
	 * Method which initiates the clustering algorithm and acts as the entry point to this class
	 * 
	 * @return Clustered Orders
	 */
	public static ArrayList<Route> initiateClustering()
	{
		generateDataFromAPI(); 		//Get all required data from the Phaki Web API
		setEnvironmentBoundries();	//Set the boundries for the creation of initial centroids
		determineNumTrucksNeeded();	//Determine the number of trucks required
		performClustering();		//Perform clustering iteration
		
		return finalClusters;		//return final clusters
	}

	/**
	 * Method which performs the clustering Iteration until valid clusters are created
	 */
	private static void performClustering() 
	{
		ArrayList<Cluster> clusters;
		
		do																//do-while loop which ensures that orders in each cluster can fit in the assigned truck
		{
			createInitialCentroids();									//Create the initial centroids
			clusters = cluster();										//Create clusters based on initial centroids
			
			while(!allClustersContainOrders(clusters))					//This loop ensures that each created cluster contains at least 1 order
			{
				createInitialCentroids();								//Create the initial centroids
				clusters = cluster();									//Create clusters based on initial centroids
			}
			
			ArrayList<LatLng> averages = calculateAverages(clusters);	//Calculate average location of each cluster i.e. the new centroid locations
	
			int iteration = 0;
			
			while((centroidsChanged(averages)) && (!centroidsRepeating(averages) && allClustersContainOrders(clusters)))	//while-loop which iterates while the centroid positions have changed and the centroid positions are not repeated(the latter is to avoid a possible infinite loop)
			{
				iteration++;
				moveCentroids(averages);								//Move the centroids to their new positions i.e. the average calculated for each cluster
				clusters = cluster();									//Cluster locations based on the new centroid locations
				printClusters(clusters, iteration);
				averages = calculateAverages(clusters);					//Recalculate average of each cluster
			}
			
		}while(!ordersFitInTrucks(clusters));							//do-while condition i.e. checks whether orders for each cluster can fit in each assigned truck
		
		
		//This just prints the resulting clusters to the console
		for(int x = 0; x < clusters.size(); x++)
		{
			System.out.println("Cluster " + x + ": \n	centroid Loc: " + clusters.get(x).centroid.toString() + "\n		truck: " + clusters.get(x).getTruck().truck_Id + "\n");
			
			for(int y = 0; y < clusters.get(x).orders.size(); y++)
			{
				System.out.print("		Order " + y + ": " + clusters.get(x).orders.get(y).order_Id + "\n");
			}
		}
		
		
		createFinalClusters(clusters);									//Convert each resulting cluster into a route object to be returned by initiateClustering() method   
	}								  		
	
	private static void printClusters(ArrayList<Cluster> clusters, int iteration) 
	{
		System.out.println("\n\nIteration: " + iteration + "\n	");
		
		for(int x = 0; x < clusters.size(); x++)
		{
			System.out.println("Truck: " + clusters.get(x).truck.truck_Capacity + "cm^3\n		");
			double totalOrderCapacity = 0.0;
			
			for(int y = 0; y < clusters.get(x).orders.size(); y++)
			{
				totalOrderCapacity += clusters.get(x).orders.get(y).order_Total_Volume;
			}
			
			System.out.println("Combined orders volume: " + totalOrderCapacity + "\n	");
		}
		
	}

	/**
	 * This method checks to see if each given cluster has at least 1 order assigned to it
	 * 
	 * @param clusters - clusters that need to be checked
	 * 
	 * @return true if all clusters contain at least 1 order, false otherwise
	 */
	private static boolean allClustersContainOrders(ArrayList<Cluster> clusters)
	{
		for(int x = 0; x < clusters.size(); x++)
			if(clusters.get(x).orders.size() == 0)
				return false;
		
		return true;
	}

	/**
	 * This method checks to see if a new centroid location has been previously used, this is done to avoid an infinite loop during the clustering iterations
	 * 
	 * @param averages - Represent the new centroid locations
	 * 
	 * @return false if at least 1 cluster has a new location which has not been repeated, true otherwise.
	 */
	private static boolean centroidsRepeating(ArrayList<LatLng> averages) 
	{
		for(int x = 0; x < averages.size(); x++)
		{
			LatLng currLoc = averages.get(x);
			
			boolean result = false;
			
			for(int y = 0; y < centroidsPrevLocs.get(x).size(); y++)
				if((currLoc.lat == centroidsPrevLocs.get(x).get(y).lat) && (currLoc.lng == centroidsPrevLocs.get(x).get(y).lng))
					result = true;
			
			if(result == false)
				return false;
		}
		
		return true;
	}

	/**
	 * This method verifies if all the orders in each cluster fit into the assigned truck
	 * 
	 * @param clusters - clusters to be examined
	 * 
	 * @return false if at least 1 cluster contains orders which do not fit into the assigned truck, true otherwise.
	 */
	private static boolean ordersFitInTrucks(ArrayList<Cluster> clusters) 
	{
		for(int x = 0; x < clusters.size(); x++)
		{
			double totalVolume = 0.0;
			
			for(int y = 0; y < clusters.get(x).getOrders().size(); y++)
				totalVolume += clusters.get(x).getOrders().get(y).order_Total_Volume;
			
			if(clusters.get(x).truck.truck_Capacity < totalVolume)
				return false;
		}
		
		return true;
	}

	/**
	 * This method converts the given clusters into Route objects which will serve as the final clusters
	 * @param clusters - final clusters to be converted
	 */
	private static void createFinalClusters(ArrayList<Cluster> clusters)
	{
		finalClusters = new ArrayList<Route>();
		
		for(int x = 0; x < clusters.size(); x++)
		{
			Route newRoute = new Route();
			
			newRoute.orders = clusters.get(x).getOrders();
			
			newRoute.route_Truck_Id = clusters.get(x).getTruck().truck_Id;
			
			finalClusters.add(newRoute);
		}
	}

	/**
	 * This method checks if a given location exists in the given array of locations
	 * 
	 * @param loc - Location to be checked
	 * @param newLocs - List of locations to be compared to the given location
	 * 
	 * @return true if loc exists in newLocs, false otherwise
	 */
	private static boolean locationExists(LatLng loc, ArrayList<LatLng> prevLocs)
	{
		for(int x = 0; x < prevLocs.size(); x++)
			if((loc.lat == prevLocs.get(x).lat) && (loc.lng == prevLocs.get(x).lng))
				return true;
		
		return false;
	}
	
	/**
	 * This method sets the new location of each centroid to the given locations
	 * 
	 * @param newLocs - New centroid locations
	 */
	private static void moveCentroids(ArrayList<LatLng> newLocs) 
	{
		for(int x = 0; x < centroids.size(); x++)
		{
			if(!locationExists(centroids.get(x), centroidsPrevLocs.get(x)))                     
				centroidsPrevLocs.get(x).add(centroids.get(x));
			
			centroids.set(x, newLocs.get(x));
		}
	}
  
	/**
	 * This method checks if the new centroid locations are different to the current centroid locations
	 * 
	 * @param averages - the new centroid locations
	 * 
	 * @return true if at least 1 centroid location has changed, false otherwise
	 */
	private static boolean centroidsChanged(ArrayList<LatLng> averages)
	{
		for(int x = 0; x < centroids.size(); x++)
			if((averages.get(x).lat != centroids.get(x).lat) || (averages.get(x).lng != centroids.get(x).lng))
				return true;
		
		return false;
	}

	/**
	 * This method performs the actual clustering by assigning each order to the closest centroid
	 * 
	 * @return a list of newly created clusters
	 */
	private static ArrayList<Cluster> cluster()
	{
		ArrayList<Cluster> clusters = new ArrayList<Cluster>();
		
		for(int x = 0; x < centroids.size(); x++)
			clusters.add(new Cluster(centroids.get(x), trucksToBeUsed.get(x)));
		
		
		int shortestDistIndex = 0;
		double shortestDist = 0.0;
		
		for(int x = 0; x < allOrders.size(); x++)
		{	
			OrderDto currOrder = allOrders.get(x);
			LatLng currLoc = currOrder.location.location_Coordinates;
			shortestDist = haversineDistance(centroids.get(0), currLoc);
			shortestDistIndex = 0;
			
			for(int y = 1; y < centroids.size(); y++)
			{
				double currDist = haversineDistance(centroids.get(y), currLoc);
				
				if(currDist < shortestDist)
				{
					shortestDist = currDist;
					shortestDistIndex = y;
				}	
			}
			
			clusters.get(shortestDistIndex).addOrder(currOrder);
		}
		
		
		
		return clusters;
	}

	/**
	 * This method creates the initial random centroid locations
	 */
	private static void createInitialCentroids() 
	{
		centroids = new ArrayList<LatLng>();
		centroidsPrevLocs = new ArrayList<ArrayList<LatLng>>();
		
		for(int x = 0; x < numTrucks; x++)
		{
			double randLat = (Math.random() * (maxLatBound - minLatBound)) + minLatBound;
			double randLng = (Math.random() * (maxLngBound - minLngBound)) + minLngBound;
			
			centroids.add(new LatLng(randLat, randLng));
			
			centroidsPrevLocs.add(new ArrayList<LatLng>());
		}
	}

	/**
	 * This method determines the minimum number of trucks required to facilitate all orders
	 */
	private static void determineNumTrucksNeeded()
	{
		double totalVolume = 0.0;
		
		for(int x = 0; x < allOrders.size(); x++)
			totalVolume += allOrders.get(x).order_Total_Volume;

		
		ArrayList<Truck> trucksNeeded = new ArrayList<Truck>();
		double totalTruckCapacity = 0;
		
		
		boolean reverse = false;
		int front = 0;
		int back = 1;
		
		for(int x = 0; x < allTrucks.size(); x++)
		{
			if(totalTruckCapacity <= totalVolume)
			{
				if(!reverse)
				{
					trucksNeeded.add(allTrucks.get(front));
					totalTruckCapacity += allTrucks.get(front).truck_Capacity;
					front++;
					reverse = true;
				}
				else
				{
					trucksNeeded.add(allTrucks.get(allTrucks.size() - back));
					totalTruckCapacity += allTrucks.get(allTrucks.size() - back).truck_Capacity;
					back++;
					reverse = false;
				}
			}
			else 
			{
				trucksNeeded.add(allTrucks.get(allTrucks.size() - back));
				break;
			}
				
		}
		
		trucksToBeUsed = new ArrayList<Truck>(trucksNeeded);	
		
		numTrucks = trucksToBeUsed.size();
		
		System.out.println("Trucks Chosen: " + numTrucks + "\n");
		
		for(int x = 0; x < trucksToBeUsed.size(); x++)
			System.out.println(trucksToBeUsed.get(x).toString() + "\n");
	}

	/**
	 * This sets the boundries in which the random initial centroids are generated
	 */
	private static void setEnvironmentBoundries() 
	{
		minLatBound = allOrders.get(0).location.location_Coordinates.lat;
		maxLatBound = allOrders.get(0).location.location_Coordinates.lat;
		minLngBound = allOrders.get(0).location.location_Coordinates.lng;
		maxLngBound = allOrders.get(0).location.location_Coordinates.lng;
		
		double currLat;
		double currLng;
		
		for(int x = 0; x < allOrders.size(); x++)
		{
			currLat = allOrders.get(x).location.location_Coordinates.lat;
			currLng = allOrders.get(x).location.location_Coordinates.lng;
			
			if(currLat < minLatBound)
				minLatBound = currLat;
			else if(currLat > maxLatBound)
				maxLatBound = currLat;
			
			if(currLng < minLngBound)
				minLngBound = currLng;
			else if(currLng > maxLngBound)
				maxLngBound = currLng;
		}
		
		System.out.println("\nMin Lat: " + minLatBound + "\nMax Lat: " + maxLatBound + "\nMin Lng: " + minLngBound + "\nMax Lng: " + maxLngBound + "\n'");
	}

	/**
	 * This method retrieves all the required data from the Phaki Web API
	 * 
	 * @return true if the data was successfully retrieved, false otherwise
	 */
	private static boolean generateDataFromAPI() 
	{
		allOrders = PhakiAPICommunication.GetPendingOrders();
			
		allTrucks = PhakiAPICommunication.GetAllAvailableTrucks();
		
		if((allOrders == null) | (allTrucks == null))
			return false;
		else
		{
			System.out.println("all Orders: \n");
			
			for(int x = 0; x < allOrders.size(); x++)
			{
				System.out.print(allOrders.get(x).toString() + "\n");
			}
			
			System.out.print("\nAll Trucks: \n");
			for(int x = 0; x < allTrucks.size(); x++)
			{
				System.out.print(allTrucks.get(x).toString() + "\n");
			}
			
			return true;
		}
	}



	/**
	 * @author Prakhar7 
	 * @see <a href="https://auth.geeksforgeeks.org/user/prakhar7/articles">Author of this method</a>
	 * @see <a href="file:../docs/References.txt">References</a>
	 * 
	 * @param point 1
	 * @param point 2 
	 * 
	 * @return distance between given points in kilometers
	 */
	private static double haversineDistance(LatLng point1, LatLng point2) 
	{ 
		// distance between latitudes and longitudes 
		double dLat = Math.toRadians(point2.lat - point1.lat); 
		double dLon = Math.toRadians(point2.lng - point1.lng); 
		
		
		// convert to radians 
		double point1Lat = Math.toRadians(point1.lat); 
		double point2Lat = Math.toRadians(point2.lat); 
		
		// apply formulae 
		double a = Math.pow(Math.sin(dLat / 2), 2) +  
		   Math.pow(Math.sin(dLon / 2), 2) *  
		   Math.cos(point1Lat) *  
		   Math.cos(point2Lat); 
		double rad = 6371; 
		double c = 2 * Math.asin(Math.sqrt(a)); 
		return rad * c; 
	} 
	
	/**
	 * Calculates the average location of each given cluster
	 * 
	 * @param clusters - clusters for which averages need to be calculated
	 * 
	 * @return list of average locations for each cluster
	 */
	private static ArrayList<LatLng> calculateAverages(ArrayList<Cluster> clusters)
	{
		ArrayList<LatLng> averages = new ArrayList<LatLng>();
		Cluster currCluster = null;
		
		for(int x = 0; x < clusters.size(); x++)
		{
			currCluster = clusters.get(x);
			
			double totalLat = 0;
			double totalLng = 0;
			
			int numOrders = currCluster.getOrders().size();
			
			for(int y = 0; y < numOrders; y++)
			{
				totalLat += currCluster.getOrders().get(y).location.location_Coordinates.lat;
				totalLng += currCluster.getOrders().get(y).location.location_Coordinates.lng;
			}
			
			averages.add(new LatLng(totalLat/numOrders, totalLng/numOrders));
		}
		
		return averages;
	}
	
	/**
	 * 
	 * @author Maikel Achi
	 *
	 * This is an inner static class which represents a cluster
	 */
	public static class Cluster 
	{
		private ArrayList<OrderDto> orders;
		private LatLng centroid;
		private Truck truck;
		

		public Cluster() 
		{
			orders = new ArrayList<OrderDto>();
			centroid = new LatLng();
			truck = new Truck();
		}
		
		public Cluster(LatLng centroid, Truck truck) 
		{
			orders = new ArrayList<OrderDto>();
			this.centroid = centroid;
			this.truck = truck;
		}
		
		public Cluster(ArrayList<OrderDto> orders, LatLng centroid) 
		{
			this.orders = orders;
			this.centroid = centroid;
			truck = new Truck();
		}

		public ArrayList<OrderDto> getOrders() 
		{
			return orders;
		}

		public LatLng getCentroid() {
			return centroid;
		}

		public void setOrders(ArrayList<OrderDto> orders)
		{
			this.orders = orders;
		}

		public void setCentroid(LatLng centroid) 
		{
			this.centroid = centroid;
		}
		
		public Truck getTruck()
		{
			return truck;
		}

		public void setTruck(Truck truck)
		{
			this.truck = truck;
		}
		
		public OrderDto addOrder(OrderDto newOrder)
		{
			if(this.orders.add(newOrder))
				return newOrder;
			
			return null;
		}
		
	}

}

