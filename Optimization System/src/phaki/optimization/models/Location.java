package phaki.optimization.models;

import com.google.maps.model.LatLng;

public class Location {

	public String location_Street;
	
    public String location_Suburb;
    
    public String location_City;
    
    public String location_Zip_Code;

	public LatLng location_Coordinates;
    
    

    public LatLng getLocation_Coordinates() {
		return location_Coordinates;
	}

	public void setLocation_Coordinates(LatLng location_Coordinates) {
		this.location_Coordinates = location_Coordinates;
	}

	public String getLocation_Street() {
		return location_Street;
	}

	public void setLocation_Street(String location_Street) {
		this.location_Street = location_Street;
	}

	public String getLocation_Suburb() {
		return location_Suburb;
	}

	public void setLocation_Suburb(String location_Suburb) {
		this.location_Suburb = location_Suburb;
	}

	public String getLocation_City() {
		return location_City;
	}

	public void setLocation_City(String location_City) {
		this.location_City = location_City;
	}

	public String getLocation_Zip_Code() {
		return location_Zip_Code;
	}

	public void setLocation_Zip_Code(String location_Zip_Code) {
		this.location_Zip_Code = location_Zip_Code;
	}

	@Override
	public String toString()
	{
		return location_Street + " " + location_Suburb + " " + location_City + " South Africa " + location_Zip_Code; 
	}
}
