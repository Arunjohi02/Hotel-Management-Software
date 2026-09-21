package src.ui.customer;

import java.util.Scanner;
import java.util.List;
import java.time.format.DateTimeFormatter;

import src.model.Hotel;
import src.service.CustomerService;
import src.util.InputUtil;

public class SearchHotel
{
	public static void searchHotel()
	{
		Scanner io = InputUtil.getScanner();
		System.out.print("Enter Your Location : ");
		String location = io.nextLine().trim();

		List<Hotel> hotels = CustomerService.showHotels(location);

		if(hotels == null || hotels.isEmpty())
		{
			System.out.println("\nNo hotels found in location: " + location + "\n");
			return;
		}

		System.out.println("\n=========================== HOTELS IN " + location.toUpperCase() + " ===========================\n");
		System.out.printf(" %-4s | %-20s | %-15s | %-25s | %-30s | %-12s | %-12s %n%n",
					"ID", "Name", "Phone", "Email", "Description", "Check-In", "Check-Out");

		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("hh:mm a");
		for(Hotel hotel : hotels)
		{
			System.out.printf(" %-4s | %-20s | %-15s | %-25s | %-30s | %-12s | %-12s %n",
				hotel.getid(),
				hotel.getname(),
				hotel.getphone(),
				hotel.getemail(),
				hotel.getdescription(),										
				hotel.getcheckInTime().format(formatter),
				hotel.getcheckoutTime().format(formatter));
		}
		System.out.println();
	}
}