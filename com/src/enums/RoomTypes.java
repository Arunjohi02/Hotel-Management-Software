package src.enums ;

public enum RoomTypes
{
	SINGLE(" Single Member Only "),
	SUITE("two Member Only "),
	DELUXE("4 to 5 Member only ");
	
	private String abbrivation ;
	
	RoomTypes(String abbrivation)
	{
		this.abbrivation = abbrivation ;
	}
	
	public String getMessage()
	{
		return abbrivation;
	}
}