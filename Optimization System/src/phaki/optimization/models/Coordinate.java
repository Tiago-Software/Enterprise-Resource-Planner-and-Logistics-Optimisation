package phaki.optimization.models;

public class Coordinate {
	
	private Double longitude;
	private Double latitude;
	
	public Coordinate(double latitude, double longitude)
	{
		this.longitude = longitude;
		this.latitude = latitude;
	}
	
	public double getLongitude() 
	{
		return longitude;
	}
	
	public void setLongitude(double longitude) 
	{
		this.longitude = longitude;
	}
	
	public double getLatitude() 
	{
		return latitude;
	}
	
	public void setLatitude(double latitude) 
	{
		this.latitude = latitude;
	}
	
	@Override
	public String toString()
	{
		return ("lng: " + longitude.toString() + "\nlat: " + latitude.toString());
	}
}
