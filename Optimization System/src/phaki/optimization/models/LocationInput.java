package phaki.optimization.models;

public class LocationInput {

	public String location_Street;
	
    public String location_Suburb;
    
    public String location_City;
    
    public String location_Zip_Code;

    public double location_Longitude;

    public double location_Latitude;
    
    public String location_Place_Id;
    
    @Override
    public String toString()
    {
    	return location_Street + " " + location_Suburb + " " + location_City + " South Africa " + location_Zip_Code;  
    }
}
