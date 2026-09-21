package src.enums;

public enum Role
{
	ADMIN("Admin Only"),
	CUSTOMER("Customers Only"),
	RECEPTIONIST("Receptionist Only" ),
	MANAGER("Manager only ");
	
	private String msg ;
	
	Role(String msg)
	{
		this.msg = msg ;
	}
	
	public String getmessage()
	{
		return msg ;
	}
}