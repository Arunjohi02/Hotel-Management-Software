enum Roll
{
	ADMIN("Admin Only"),
	CUSTOMER("Customers Only"),
	RECEPTIONIST("Receptionist Only" );
	
	private String msg ;
	
	Roll(String msg)
	{
		this.msg = msg ;
	}
	
	public String getmessage()
	{
		return msg ;
	}
}