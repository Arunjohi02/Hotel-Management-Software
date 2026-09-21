package src.ui.admin ;

import java.util.List ;

import java.time.LocalTime ;
import java.time.format.DateTimeFormatter;

import src.service.HotelService ;

import src.model.Hotel ;

public class VieweHotel
{
	public static void vieweHotel()
	{
		List<Hotel> hotels = HotelService.allhotels();

		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("hh:mm a");

		System.out.printf(" %-2s | %-20s | %-20s | %-10s | %-28s | %-45s | %-20s | %-20s | %-2s %n%n" ,
			"ID","Name","Location","Phone","Email","Description","Check_In","Check_out","Total Rooms");
		for(Hotel hResult : hotels)
		{
			System.out.printf(" %-2s | %-20s | %-20s | %-10s | %-28s | %-45s | %-20s | %-20s | %-2s %n%n" ,

				hResult.getid(),
				hResult.getname(),
				hResult.getlocation(),
				hResult.getphone(),
				hResult.getemail(),
				hResult.getdescription(),
				hResult.getcheckInTime().format(formatter),
				hResult.getcheckoutTime().format(formatter),
				hResult.gettotalRooms());
		}
	}
}