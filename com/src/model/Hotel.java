package src.model ;

class Hotel
{
	private int id ;
	private String name ;
	private String location ;
	private String phone ;
	private String email ;
	private String description ;
	private String checkInTime ;
	private String checkoutTime ;
	private int totalRooms ;

	public Hotel(String name ,
				String location ,
				String Phone ,
				String email ,
				String description ,
				String checkInTime ,
				String checkoutTime
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
	
	public String getcheckInTime() { return checkInTime ; }
	
	public String getcheckoutTime() { return checkoutTime ; }
	
	public String gettotalRooms() { return totalRooms ; }
	
	
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
		   
	public void setcheckInTime(String checkInTime) 
	{
		this.checkInTime = checkInTime ; 
	}
		   
	public void setcheckoutTime(String checkoutTime) 
	{ 
		this.checkoutTime = checkoutTime ; 
	}
		   
	public void settotalRooms(int totalRooms)
	{ 
		this.totalRooms = totalRooms ; 
	}
	
	public String toString()
	{
		return name + " " +
			   location + " " +
		       phone + " " +
			   email + " " +
			   description + " " +
			   checkInTime +" " +
			   checkoutTime +" " +
			   totalRooms ;
	}
}