package src.model ;

import java.time.LocalDate ;

public class Coupons
{
	private int id ;
	private String code ;
	private int discountPercentage ;
	private LocalDate validDate ;
	private int hotelId ;
	
	public Coupons() {}
	
	public Coupons(String code,
		int discountPercentage,
		LocalDate validDate,
		int hotelId
	)
	{
		this.code = code ;
		this.discountPercentage = discountPercentage ;
		this.validDate = validDate ;
		this.hotelId = hotelId ;
	}
	
	public int getid() { return id ;  }
	
	public String getcode() { return code ;  }
	
	public int getdiscountPercentage() { return discountPercentage ;  }
	
	public LocalDate getvalidDate() { return validDate ;  }
	
	public int gethotelId() { return hotelId ;  }
	
	public void setid(int id)
	{
		this.id = id ; 
	}
	
	public void setcode(String code)
	{
		this.code = code ;
	}
	
	public void setdiscountPercentage(int discountPercentage)
	{
		this.discountPercentage = discountPercentage ;
	}
	
	public void setvalidDate(LocalDate validDate) 
	{
		this.validDate = validDate ;
	}
	
	public void sethotelId(int hotelId) 
	{ 
		this.hotelId = hotelId ;
	}
	
	public String toString()
	{
		return id + " " +
			   code + " " +
			   discountPercentage + " " +
		       validDate + " " +
			   hotelId ;
	}
}