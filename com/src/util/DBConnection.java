package com.src.util ;

import java.sql.DriverManager ;
import java.sql.Connection ;
import java.sql.SQLExeption ;

public class DBConnection
{
	private final static URL="jdbc:mysql://localhost:3306/hotel_Booking";
	private final static UserName="root";
	private final static pass="root";

	public static Connection getConnection()throws Exception
	{
		return DriverManager.getConnection(URL,UserName,pass);
	}
}