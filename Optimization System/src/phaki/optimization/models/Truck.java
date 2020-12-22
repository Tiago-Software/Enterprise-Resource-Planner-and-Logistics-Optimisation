package phaki.optimization.models;

public class Truck 
{
	public int truck_Id;
	public double truck_Capacity;
	
	
	
	public int getTruck_Id() 
	{
		return truck_Id;
	}
	
	public void setTruck_Id(int truck_Id) 
	{
		this.truck_Id = truck_Id;
	}
	
	public double getTruck_Capacity() 
	{
		return truck_Capacity;
	}
	
	public void setTruck_Capacity(double truck_Capacity) 
	{
		this.truck_Capacity = truck_Capacity;
	}
	
	@Override
	public String toString()
	{
		return (truck_Id + ": " + truck_Capacity);
	}
	
	
}
