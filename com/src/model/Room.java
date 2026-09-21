package src.model ;

public class Room
{
	private int id ;
	private int  roomNumber ;
	private int floorNumber ;
	private String status ;
	private int hotelId ;
	
	private String title ;
	private String acType ;
	private double basePrice ;
	private int capacity ;
	private String hotelName ;
	
	public Room() {}

	public Room(int roomNumber,int floorNumber,String status,String title ,String acType , double basePrice , int capacity)
	{
		this.roomNumber = roomNumber ;
		this.floorNumber = floorNumber ;
		this.status = status ;
		this.title = title ;
		this.acType = acType ;
		this.basePrice = basePrice ;
		this.capacity = capacity ;
	}
	
	public int getid() { return id ; }
	
	public int getroomNumber() { return roomNumber ; }
	
	public int getfloorNumber() { return floorNumber ; }
	
	public String getstatus() { return status ; }
	
	public int gethotelId() { return hotelId ; }


	public String gettitle() { return title ; }
	
	public String getactype() { return acType ; }
	
	public double getbasePrice() { return basePrice ; }
	
	public int getcapacity() { return capacity ; }
	
	
	public void setid(int id)
	{ 
		this.id=id ;
	}
	
	public void setroomNumber(int roomNumber)
	{ 
		this.roomNumber=roomNumber ;
	}
	
	public void setfloorNumber(int floorNumber)
	{ 
		this.floorNumber=floorNumber ; 
	}
	
	public void setstatus(String status)
	{ 
		this.status=status ;
	}
	
	public void sethotelId(int hotelId)
	{ 
		this.hotelId = hotelId ;
	}

	public void settitle(String title )
	{
		this.title = title ;
	}
		   
	public void setactype(String acType)
	{
		this.acType = acType ;
	}
	
	public void setbasePrice(double basePrice)
	{ 
		this.basePrice = basePrice ;
	}
	
	public void setcapacity(int capacity)
	{ 
		this.capacity = capacity ;
	}

	public String gethotelName()
	{
		return hotelName;
	}

	public void sethotelName(String hotelName)
	{
		this.hotelName = hotelName;
	}
	
	public String toString()
	{
		return id + " " +
			   roomNumber + " " +
		       floorNumber + " " +
			   status + " " +
			   hotelId + " " +
			   title + " " +
			   acType + " " +
			   basePrice + " " +
			   capacity ;
	}
}	