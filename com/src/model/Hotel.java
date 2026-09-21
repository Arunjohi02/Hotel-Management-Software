package src.model ;

import java.time.LocalTime ;
import java.sql.Date;

public class Hotel
{
	private int id ;
	private String name ;
	private String location ;
	private String phone ;
	private String email ;
	private String description ;
	private LocalTime checkInTime ;
	private LocalTime checkoutTime ;
	private int totalRooms ;

	public Hotel(){}
	
	public Hotel(String name ,
				String location ,
				String phone ,
				String email ,
				String description ,
				LocalTime checkInTime ,
				LocalTime checkoutTime,
				int totalRooms
			)
	{
		
		this.name = name ;
		this.location = location ;
		this.phone = phone ;
		this.email = email;
		this.description = description ;
		this.checkInTime = checkInTime ;
		this.checkoutTime = checkoutTime ;
		this.totalRooms = totalRooms ;
		
	}
	
	public int getid() { return id ; }
	
	public String getname() { return name ; }
	
	public String getlocation() { return location ; }
	
	public String getphone() { return phone ; }
	
	public String getemail() { return email ; }
	
	public String getdescription() { return description ; }
	
	public LocalTime getcheckInTime() { return checkInTime ; }
	
	public LocalTime getcheckoutTime() { return checkoutTime ; }
	
	public int gettotalRooms() { return totalRooms ; }
	
	
	public void setid(int id)
	{ 
		this.id = id ; 
	}
	
	public void setname(String name)
	{ 
		this.name = name ; 
	}
		   
	public void setlocation(String location)
	{ 
		this.location = location ; 
	}
		   
	public void setphone(String phone) 
	{ 
		this.phone = phone ; 
	}
		   
	public void setemail(String email) 
	{ 
		this.email = email;
	}
		   
	public void setdescription(String description) 
	{
		this.description = description ; 
	}
		   
	public void setcheckInTime(LocalTime checkInTime) 
	{
		this.checkInTime = checkInTime ; 
	}
		   
	public void setcheckoutTime(LocalTime checkoutTime) 
	{ 
		this.checkoutTime = checkoutTime ; 
	}
		   
	public void settotalRooms(int totalRooms)
	{ 
		this.totalRooms = totalRooms ; 
	}
	
	public String toString()
	{
		return id + " " +
			   name + " " +
			   location + " " +
		       phone + " " +
			   email + " " +
			   description + " " +
			   checkInTime + " " +
			   checkoutTime + " " +
			   totalRooms ;
	}
}