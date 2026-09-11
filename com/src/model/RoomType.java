package com.src.model ;

public class RoomType
{
	private int id ;
	private String title ;
	private String acType ;
	private double basePrice ;
	private int capacity ;
	private int count ;
	private int hotelId ;
	
	public RoomType() {}
	
	public RoomType(String title ,
			String acType ,
			double basePrice ,
			int capacity ,
			int count ,
			int hotelId 
				)
	{
		this.title = title ;
		this.acType = acType ;
		this.basePrice = basePrice ;
		this.capacity = capacity ;
		this.count = count ;
		this.hotelId = hotelId ;
	}
	
	public int getid() { return id ; }
	
	public String gettitle() { return title ; }
	
	public String getactype() { return actype ; }
	
	public double getbasePrice() { return basePrice ; }
	
	public int getcapacity() { return capacity ; }
	
	public int getcount() { return count ; }
	
	public int gethotelId() { return hotelId ; }
	
	
	public void setid(int id) 
	{
		this.id = id ;
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
		    
	public void setcount(int count)
	{ 
		this.count = count ;
	}
	
	public void sethotelId(int hotelId)
	{ 
		this.hotelId = hotelId ; 
	}
	
	public String toString()
	{
		return title + " " +
			   acType + " " +
		       basePrice + " " +
			   capacity + " " +
			   count + " " +
			   hotelId ;
	}
}