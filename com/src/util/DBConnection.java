package src.util ;

import src.properties.PropertyLoader ;

import java.sql.DriverManager ;
import java.sql.Connection ;
import java.sql.SQLException ;

public class DBConnection
{
	final static String URL="jdbc:mysql://localhost:3306/hotel_Booking";
	final static String UserName="root";
	final static String pass="root";

	

	public static Connection getConnection()throws Exception
	{
		return DriverManager.getConnection(PropertyLoader.getProperty("db.url"),PropertyLoader.getProperty("db.userName"),PropertyLoader.getProperty("db.password"));
	}
}