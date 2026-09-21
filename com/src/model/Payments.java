package src.model ;

public class Payments
{
	private int id ;
	private int bookingId ;
	private double amount ;
	private String paymentType ;
	private String status ;
	
	public Payments() {}
	
	public Payments(
			int bookingId,
			double amount,
			String paymentType,
			String status
		)
	{
		this.bookingId = bookingId ;
		this.amount = amount ;
		this.paymentType = paymentType ;
		this.status = status ;
	}
	
	public int getid() { return id ; }
	
	public int getbookingId() { return bookingId ; }
	
	public double getamount() { return amount ; }
	
	public String getpaymentType() { return paymentType ; }
	
	public String getstatus() { return status ; }
	
	
	
	public void setid(int id) 
	{ 
		this.id = id; 
	}
	
	public void setbookingId(int bookingId) 
	{ 
		this.bookingId = bookingId; 
	}
	
	public void setamount(double amount) 
	{ 
		this.amount = amount ; 
	}
	
	public void setpaymentType(String paymentType) 
	{ 
		this.paymentType = paymentType ; 
	}
	
	public void setstatus(String status) 
	{ 
		this.status = status ; 
	}
	
	public String toString()
	{
		return id + " " +
			   bookingId + " " +
		       amount + " " +
			   paymentType + " " +
			   status ;
	}
}