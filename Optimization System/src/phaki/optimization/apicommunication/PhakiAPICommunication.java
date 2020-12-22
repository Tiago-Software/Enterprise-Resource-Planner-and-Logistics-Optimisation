package phaki.optimization.apicommunication;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import javax.net.ssl.HttpsURLConnection;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import javafx.scene.control.TextArea;
import phaki.optimization.models.*;

/**
 * 
 * @author Maikel Achi
 * 
 * This class handles all communication with the Phaki Web API
 *
 */


public class PhakiAPICommunication 
{
	private static String PHAKI_API_URL = "";
	private static final String POST_AUTH_URL = "Authenticate";
	private static final String GET_PENDING_ORDERS_URL = "GetOrdersDue";
	private static final String POST_RESULTS = "UpdateRoutes";
	private static final String GET_TRUCKS = "GetAllAvailableTrucks";
	private static final String GET_BRANCH_LOC = "GetBranchLocation";
	
	private static final String AUTH_EMAIL = "optimization@phaki.com";
	private static final String AUTH_PASSWORD = "1234";
	
	private static final String CONTENT_TYPE = "application/json; charset=utf-8";
	private static final String USER_AGENT = "Mozilla/5.0";
	
	private static TextArea output;
	
	private static String token;
	
	private static HttpsURLConnection con;
	private static URL url;
	
	private static BufferedReader br;
	
	/**
	 * This method is used to authenticate this system with the Phaki Web API
	 * 
	 * @return true if authenticated successfully, false otherwise
	 */
	public static boolean Authenticate()
	{
		
		try {
			url = new URL(PHAKI_API_URL + POST_AUTH_URL);
			
			con = (HttpsURLConnection) url.openConnection();
			con.setRequestMethod("POST");
			con.setRequestProperty("User-agent", USER_AGENT);
			con.setRequestProperty("Content-type", CONTENT_TYPE);
			con.setRequestProperty("Accept", CONTENT_TYPE);
			
			JsonObject obj = new JsonObject();
			
			obj.addProperty("emp_Log_Email", AUTH_EMAIL);
			obj.addProperty("emp_Log_Password", AUTH_PASSWORD);
			
			con.setDoOutput(true);
			
			OutputStream os = con.getOutputStream();
			os.write(obj.toString().getBytes("UTF-8"));
			os.flush();
			os.close();
			
			int responseCode = con.getResponseCode();
			
			if(responseCode == HttpsURLConnection.HTTP_ACCEPTED)
			{
				br = new BufferedReader(new InputStreamReader(con.getInputStream()));
				
				StringBuffer responseMsg = new StringBuffer();
				String line = new String();
				
				while((line = br.readLine()) != null)
					responseMsg.append(line);
				
				Gson gson = new Gson();
				
				Employee emp = gson.fromJson(responseMsg.toString(), Employee.class);
				
				token = emp.token;
				
				return true;
			}
			else 
				return false;
			
		} catch (MalformedURLException e) {
			output.appendText("\nProvided url is not valid, please change it in the OPTIONS menu above and try again.");
			return false;
			
		} catch (IOException e) {
			e.printStackTrace();
			return false;
			
		}
		finally
		{
			con.disconnect();
			try {
				if(br != null)
					br.close();
			} catch (IOException e) {
				e.printStackTrace();
				
			}
		}
	}
	
	/**
	 * This method retrieves all the pending orders from the API
	 * 
	 * @return a list of all the retrieved orders
	 */
	public static ArrayList<OrderDto> GetPendingOrders()
	{
		try {
			url = new URL(PHAKI_API_URL + GET_PENDING_ORDERS_URL);
			con = (HttpsURLConnection) url.openConnection();
			con.setRequestMethod("GET");
			con.setRequestProperty("User-agent", USER_AGENT);
			con.setRequestProperty("Content-type", CONTENT_TYPE);
			con.setRequestProperty("Accept", CONTENT_TYPE);
			con.setRequestProperty("Authorization", "Bearer " + token);
			con.setDoInput(true);
			
			int responseCode = con.getResponseCode();
			
			if(responseCode == HttpsURLConnection.HTTP_OK)
			{
				br = new BufferedReader(new InputStreamReader(con.getInputStream()));
				
				StringBuffer responseMsg = new StringBuffer();
				String line = new String();
				
				while((line = br.readLine()) != null)
					responseMsg.append(line);
				
				Gson gson = new Gson();
				
				ArrayList<Order> orders = gson.fromJson(responseMsg.toString(), new TypeToken<ArrayList<Order>>(){}.getType());
				
				ArrayList<OrderDto> convertedOrders = new ArrayList<OrderDto>();
				
 				for(int x = 0; x < orders.size(); x++)
					convertedOrders.add(new OrderDto(orders.get(x)));
				
				return convertedOrders;
				
			}
			else
			{
				return null;
			}
			
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
			
		}
		finally
		{
			try {
				if(br != null)
					br.close();
				con.disconnect();
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
	}
		
	/**
	 * This method is used to post the results of the optimization back to the API
	 * 
	 * @param routes - the resulting routes of the optimization
	 * 
	 * @return true if results were posted successfully, false otherwise
	 */
	public static boolean PostOptimizationResults(ArrayList<RouteDto> routes)
 	{
		try {
			url = new URL(PHAKI_API_URL + POST_RESULTS);
			con = (HttpsURLConnection) url.openConnection();
			con.setRequestMethod("POST");
			con.setRequestProperty("User-agent", USER_AGENT);
			con.setRequestProperty("Content-type", CONTENT_TYPE);
			con.setRequestProperty("Accept", CONTENT_TYPE);
			con.setRequestProperty("Authorization", "Bearer " + token);
			con.setDoOutput(true);
			
			String json = new Gson().toJson(routes);
			
			System.out.println(json);
			
			try(OutputStream os = con.getOutputStream()) 
			{
			    byte[] input = json.getBytes("utf-8");
			    os.write(input, 0, input.length);			
			}
			
			int responseCode = con.getResponseCode();
			
			if(responseCode == HttpsURLConnection.HTTP_OK)
			{
				System.out.println("\n\n" + responseCode);
				return true;
			}
			else
			{
				System.out.println("\n\n" + responseCode);
				return false;
			}
			
		} catch (MalformedURLException e)
		{
			e.printStackTrace();
			return false;
			
		} catch (IOException e)
		{
			e.printStackTrace();
			return false;
			
		}
		finally
		{
			try {
				br.close();
				con.disconnect();
				
			} catch (IOException e) {
				e.printStackTrace();
			}
			
		}
	}
	
	/**
	 * This method retrieves all the available trucks from the API
	 * 
	 * @return a list of all the retrieved trucks
	 */
	public static ArrayList<Truck> GetAllAvailableTrucks()
	{
		try {
			url = new URL(PHAKI_API_URL + GET_TRUCKS);
			con = (HttpsURLConnection) url.openConnection();
			con.setRequestMethod("GET");
			con.setRequestProperty("User-agent", USER_AGENT);
			con.setRequestProperty("Content-type", CONTENT_TYPE);
			con.setRequestProperty("Accept", CONTENT_TYPE);
			con.setRequestProperty("Authorization", "Bearer " + token);
			con.setDoInput(true);
			
			int responseCode = con.getResponseCode();
			
			if(responseCode == HttpsURLConnection.HTTP_OK)
			{
				br = new BufferedReader(new InputStreamReader(con.getInputStream()));
				
				StringBuffer responseMsg = new StringBuffer();
				String line = new String();
				
				while((line = br.readLine()) != null)
					responseMsg.append(line);
				
				Gson gson = new Gson();
				
				ArrayList<Truck> trucks = gson.fromJson(responseMsg.toString(), new TypeToken<ArrayList<Truck>>(){}.getType());
				
				return trucks;
				
			}
			else
			{
				return null;
			}
			
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
			
		}
		finally
		{
			try {
				br.close();
				con.disconnect();
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
	}

	/**
	 * This method retrieves the location of the Phaki branch
	 * 
	 * @return Location of the branch
	 */
	public static Location GetBranchLocation()
	{
		try {
			url = new URL(PHAKI_API_URL + GET_BRANCH_LOC);
			con = (HttpsURLConnection) url.openConnection();
			con.setRequestMethod("GET");
			con.setRequestProperty("User-agent", USER_AGENT);
			con.setRequestProperty("Content-type", CONTENT_TYPE);
			con.setRequestProperty("Accept", CONTENT_TYPE);
			con.setRequestProperty("Authorization", "Bearer " + token);
			con.setDoInput(true);
			
			int responseCode = con.getResponseCode();
			
			if(responseCode == HttpsURLConnection.HTTP_OK)
			{
				br = new BufferedReader(new InputStreamReader(con.getInputStream()));
				
				StringBuffer responseMsg = new StringBuffer();
				String line = new String();
				
				while((line = br.readLine()) != null)
					responseMsg.append(line);
				
				Gson gson = new Gson();
				
				LocationInput loc = gson.fromJson(responseMsg.toString(), new TypeToken<LocationInput>(){}.getType());
				
				Location location = new Location();
				
				location.location_Street = loc.location_Street;
				location.location_Suburb = loc.location_Suburb;
				location.location_City = loc.location_City;
				location.location_Zip_Code = loc.location_Zip_Code;
				
				location.location_Coordinates = GMapsApiCommunication.geocodeAddress(location.toString());
				
				return location;
			}
			else
			{
				return null;
			}
			
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
			
		}
		finally
		{
			try {
				br.close();
				con.disconnect();
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
	}
	
	public static void setURL(String phakiApiUrl)
	{
		PHAKI_API_URL = phakiApiUrl;
	}
	
	public static void setOutputArea(TextArea outputArea)
	{
		output = outputArea;
	}
	
}




