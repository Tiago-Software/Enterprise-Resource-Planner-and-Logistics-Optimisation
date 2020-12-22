package phaki.optimization.models;

public class OrderUpdateDto
{
	private int order_Id;
	private int order_Sequence_Number;
	
	
	
	public OrderUpdateDto(int order_Id, int sequence_Num) 
	{
		this.order_Id = order_Id;
		this.order_Sequence_Number = sequence_Num;
	}

	
	
	public int getOrder_Id() 
	{
		return order_Id;
	}
	
	public int getOrder_Sequence_Number() 
	{
		return order_Sequence_Number;
	}
	
	public void setOrder_Id(int order_Id)
	{
		this.order_Id = order_Id;
	}
	
	public void setOrder_Sequence_Number(int order_Sequence_Number) {
		this.order_Sequence_Number = order_Sequence_Number;
	}

}
