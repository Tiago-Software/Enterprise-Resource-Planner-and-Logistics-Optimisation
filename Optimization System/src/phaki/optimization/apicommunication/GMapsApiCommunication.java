package phaki.optimization.apicommunication;

import java.io.IOException;
import java.util.ArrayList;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.maps.DirectionsApi;

import com.google.maps.DirectionsApiRequest;
import com.google.maps.GeoApiContext;
import com.google.maps.GeocodingApi;
import com.google.maps.ImageResult;
import com.google.maps.StaticMapsApi;
import com.google.maps.StaticMapsRequest;
import com.google.maps.StaticMapsRequest.Markers;
import com.google.maps.StaticMapsRequest.Markers.MarkersSize;
import com.google.maps.StaticMapsRequest.Path;
import com.google.maps.StaticMapsRequest.StaticMapType;
import com.google.maps.errors.ApiException;
import com.google.maps.model.DirectionsResult;
import com.google.maps.model.GeocodingResult;
import com.google.maps.model.LatLng;
import com.google.maps.model.Size;

import phaki.optimization.models.Location;
import phaki.optimization.models.OrderDto;
import phaki.optimization.models.Route;

/**
 * 
 * @author Maikel Achi
 *
 *	This class handles the communication with the Google Maps API using the Google Maps Java Client Library
 */
public class GMapsApiCommunication
{
	private static final String API_KEY = "AIzaSyBxG4vTY6qL6Y0H8pI7Msqs70j1hGNcALE";
	private static GeoApiContext context = new GeoApiContext.Builder().apiKey(API_KEY).build();
	private static final String[] colors = {"green", "orange", "blue", "gray", "purple", "yellow", "brown", "red", "white", "black"}; 
	
	
	/**
	 * This method geocodes a given string address using the Google Maps API
	 * 
	 * @param address - Address to be geocoded 
	 * 
	 * @return the coordinates of the given address
	 */
	public static LatLng geocodeAddress(String address)
	{ 
		try {
			GeocodingResult[] results = GeocodingApi.geocode(context, address).await();
			
		
			
			if(results != null)
			{
				Gson gson = new GsonBuilder().setPrettyPrinting().create();
				
				String strResult = gson.toJson(results[0].geometry);
				
				JsonObject jsonResult = JsonParser.parseString(strResult).getAsJsonObject();
				
				JsonObject location = jsonResult.get("location").getAsJsonObject();
				
				double lat = location.get("lat").getAsDouble();
				
				double lng = location.get("lng").getAsDouble();
				
				return new LatLng(lat, lng);
			}
			else
			{
				return null;
			}
			
		} catch (ApiException | InterruptedException | IOException e) {
			e.printStackTrace();
			
			return null;
		}
	}
	
	/**
	 * This method retrieves the most optimal route given the branch location and the route consisting of all the locations that need to be traversed
	 * 
	 * @param route - Route object containing the locations to be traversed
	 * @param branchLoc - Location of the branch which serves as the start and end point of the route
	 * 
	 * @return A Route object containing the locations to be traversed in the optimal sequence
	 */
	public static Route getOptimalRoute(Route route, Location branchLoc)
	{
		DirectionsApiRequest request = DirectionsApi.newRequest(context).origin(branchLoc.toString()).destination(branchLoc.toString()).optimizeWaypoints(true);
		
		LatLng[] waypoints = new LatLng[route.orders.size()];
		
		for(int x = 0; x < route.orders.size(); x++)
			waypoints[x] = route.orders.get(x).location.location_Coordinates;
		
		try {
			DirectionsResult result = request.waypoints(waypoints).await();
			
			
			if(result != null)
			{
				
				long totalDuration = 0;
				double totalDistance = 0;
				
				for(int x = 0; x < result.routes[0].legs.length; x++)
				{
					totalDuration += result.routes[0].legs[x].duration.inSeconds;
					totalDistance += result.routes[0].legs[x].distance.inMeters / 1000.0;
				}
				
				route.route_ETA_In_Seconds = totalDuration;
				route.route_Total_Distance = totalDistance;  
				
				ArrayList<OrderDto> ordersNewSeq = new ArrayList<OrderDto>();
				
				for(int x = 0; x < result.routes[0].waypointOrder.length; x++)
					ordersNewSeq.add(route.orders.get(result.routes[0].waypointOrder[x]));
				
				route.orders = ordersNewSeq;
				
				return route;
			}
			else
				return null;
				
		} catch (ApiException | InterruptedException | IOException e) 
		{
			e.printStackTrace();
			return null;
		}
	}
	
	public static byte[] getStaticMapInitialOrders(ArrayList<OrderDto> orders)
	{
		StaticMapsRequest request = StaticMapsApi.newRequest(context, new Size(1000, 400));
		
		Markers markers = new Markers();
		markers.size(MarkersSize.small);
		markers.color("Red");
		
		for(int x = 0; x < orders.size(); x++)
		{
			markers.addLocation(orders.get(x).location.location_Coordinates);
		}
		
		request.markers(markers);
		
		request.scale(2);
		
		request.maptype(StaticMapType.roadmap);
		
		request.zoom(9);
		
		try 
		{
			ImageResult mapImage = request.await();
			return mapImage.imageData;
		} 
		catch (ApiException e) 
		{
			e.printStackTrace();
			return null;
		} 
		catch (InterruptedException e) 
		{
			e.printStackTrace();
			return null;
		}
		catch (IOException e) 
		{
			e.printStackTrace();
			return null;
		}
	}
	
	public static byte[] getStaticMapClusteredOrders(ArrayList<Route> routes)
	{
		StaticMapsRequest request = StaticMapsApi.newRequest(context, new Size(1000, 400));
		
		for(int x = 0; x < routes.size(); x++)
		{
			Markers markers = new Markers();
			
			markers.size(MarkersSize.small);
			markers.color(colors[x]);
			
			for(int y = 0; y < routes.get(x).orders.size(); y++)
			{
				markers.addLocation(routes.get(x).orders.get(y).location.location_Coordinates);
			}
			
			request.markers(markers);
		}
		
		request.scale(2);
		
		request.maptype(StaticMapType.roadmap);
		
		
		try 
		{
			ImageResult mapImage = request.await();
			return mapImage.imageData;
		} 
		catch (ApiException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
		catch (InterruptedException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		} 
		catch (IOException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}
	
	public static byte[] getStaticMapWithPaths(ArrayList<Route> optimizedRoutes, Location branchLoc)
	{
		StaticMapsRequest request = StaticMapsApi.newRequest(context, new Size(1000, 400));
		
		for(int x = 0; x < optimizedRoutes.size(); x++)
		{
			Path path = new Path();
			path.color(colors[x]);
			path.geodesic(true);
			
			Markers markers = new Markers();
			markers.size(MarkersSize.tiny);
			markers.color(colors[x]);
			
			path.addPoint(branchLoc.location_Coordinates);
			
			for(int y = 0; y < optimizedRoutes.get(x).orders.size(); y++)
			{
				markers.addLocation(optimizedRoutes.get(x).orders.get(y).location.location_Coordinates);
				path.addPoint(optimizedRoutes.get(x).orders.get(y).location.location_Coordinates);
			}
			path.addPoint(branchLoc.location_Coordinates);
			
			request.markers(markers);
			request.path(path);
		}
		
		request.scale(2);
		
		request.maptype(StaticMapType.roadmap);
		
		Markers branch = new Markers();
		branch.addLocation(branchLoc.location_Coordinates);
		branch.color(colors[colors.length - 1]);
		
		request.markers(branch);
		
		try 
		{
			ImageResult mapImage = request.await();
			return mapImage.imageData;
		} 
		catch (ApiException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
		catch (InterruptedException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		} 
		catch (IOException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}
	
	public static byte[] getStaticMap()
	{
		StaticMapsRequest request = StaticMapsApi.newRequest(context, new Size(1000, 400));
		
		request.scale(2);
		
		request.maptype(StaticMapType.roadmap);
		
		request.zoom(9);
		
		request.center("Gauteng");
		
		try 
		{
			ImageResult mapImage = request.await();
			return mapImage.imageData;
		} 
		catch (ApiException e) 
		{
			e.printStackTrace();
			return null;
		} 
		catch (InterruptedException e) 
		{
			e.printStackTrace();
			return null;
		}
		catch (IOException e) 
		{
			e.printStackTrace();
			return null;
		}
	}
}
