package com.src.model ;

public class Room
{
	private int id ;
	private int  roomNumber ;
	private int floorNumber ;
	private String status ;
	private int hotelId ;
	private int typeId ;
	
	public Room() {}

	public Room(int roomNumber,int floorNumber,String status,int hotelId , int typeId )
	{
		this.roomNumber = roomNumber ;
		this.floorNumber = floorNumber ;
		this.status = status ;
		this.hotelId = hotelId ;
		this.typeId = typeId ;
	}
	
	public int getid() { return id ; }
	
	public int getroomNumber() { return roomNumber ; }
	
	public int getfloorNumber() { return floorNumber ; }
	
	public String getstatus() { return status ; }
	
	public int gethotelId() { return hotelId ; }
	
	public int gettypeId() { return typeId ; }
	
	public void setid(int id)
	{ 
		this.id=id 
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
	
	public void settypeId(int typeId)
	{ 
		this.typeId = typeId;
	}
	
	public String toString()
	{
		return id + " " +
			   roomNumber + " " +
		       floorNumber + " " +
			   status + " " +
			   hotelId + " " +
			   typeId ;
	}
}	