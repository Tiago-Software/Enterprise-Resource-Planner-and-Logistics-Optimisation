package phaki.optimization.models;

import java.util.ArrayList;
import java.util.Date;


public class Route {

	 public int route_Truck_Id;

     public long route_ETA_In_Seconds;
     
     public double route_Total_Distance;

     public Date route_Date;

     public ArrayList<OrderDto> orders;
}
