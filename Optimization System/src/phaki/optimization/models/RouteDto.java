package phaki.optimization.models;

import java.util.ArrayList;

public class RouteDto 
{
	public int route_Truck_Id;

    public long route_ETA_In_Seconds;
    
    public double route_Total_Distance;

    public ArrayList<OrderUpdateDto> orders;
}
