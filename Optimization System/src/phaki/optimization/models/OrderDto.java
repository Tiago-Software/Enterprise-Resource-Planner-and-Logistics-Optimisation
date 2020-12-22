package phaki.optimization.models;

import com.google.maps.model.LatLng;

public class OrderDto 
{
	public int order_Id;

    public Location location;
    
    public double order_Total_Volume;
    
    public OrderDto() {}
    
    public OrderDto(Order order)
    {
    	location = new Location();
    	
    	this.order_Id = order.order_Id;
    	this.order_Total_Volume = order.order_Total_Volume;
    	
    	this.location.location_Street = order.location.location_Street;
		this.location.location_Suburb = order.location.location_Suburb;
		this.location.location_City = order.location.location_City;
		this.location.location_Zip_Code = order.location.location_Zip_Code;
		this.location.location_Coordinates = new LatLng(order.location.location_Latitude, order.location.location_Longitude);
    }
    
    @Override
    public String toString()
    {
    	return String.valueOf(order_Id) + ": " + order_Total_Volume;
    }
}
