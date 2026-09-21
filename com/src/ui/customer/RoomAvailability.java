package src.ui.customer;

import java.util.Scanner;
import java.util.List;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

import src.model.Hotel;
import src.model.Room;
import src.service.CustomerService;
import src.enums.RoomTypes;
import src.enums.AcType;
import src.util.InputUtil;

public class RoomAvailability
{
	public static void roomAvailability()
	{
		Scanner io = InputUtil.getScanner();

		System.out.print("Enter Your Location : ");
		String location = io.nextLine().trim();

		List<Hotel> hotels = CustomerService.availability(location);

		if(hotels == null || hotels.isEmpty())
		{
			System.out.println("\nNo hotels available in location: " + location + "\n");
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

		int hotelId = 0;
		while(true)
		{
			try
			{
				System.out.print("Enter Hotel ID to Check Rooms : ");
				hotelId = Integer.parseInt(io.nextLine().trim());
				boolean valid = false;
				for(Hotel h : hotels)
				{
					if(h.getid() == hotelId)
					{
						valid = true;
						break;
					}
				}
				if(valid) break;
				System.out.println("Invalid Hotel ID. Please pick an ID from the list above.");
			}
			catch(NumberFormatException e)
			{
				System.out.println("Please enter numeric Hotel ID.");
			}
		}

		LocalDate checkIn = null;
		while(checkIn == null)
		{
			try
			{
				System.out.print("Enter Check-In Date (yyyy-MM-dd) : ");
				checkIn = LocalDate.parse(io.nextLine().trim());
				if(checkIn.isBefore(LocalDate.now()))
				{
					System.out.println("Check-in date cannot be in the past. Today is " + LocalDate.now());
					checkIn = null;
				}
			}
			catch(DateTimeParseException e)
			{
				System.out.println("Invalid date format. Example: " + LocalDate.now());
			}
		}

		LocalDate checkOut = null;
		while(checkOut == null)
		{
			try
			{
				System.out.print("Enter Check-Out Date (yyyy-MM-dd) : ");
				checkOut = LocalDate.parse(io.nextLine().trim());
				if(!checkOut.isAfter(checkIn))
				{
					System.out.println("Check-out date must be after check-in date.");
					checkOut = null;
				}
			}
			catch(DateTimeParseException e)
			{
				System.out.println("Invalid date format. Example: " + checkIn.plusDays(1));
			}
		}

		String title = null;
		while(title == null)
		{
			System.out.println("\nSelect Room Type: 1. SINGLE  2. SUITE  3. DELUXE");
			System.out.print("Enter Choice (1 - 3): ");
			try
			{
				int ch = Integer.parseInt(io.nextLine().trim());
				if(ch == 1) title = RoomTypes.SINGLE.toString();
				else if(ch == 2) title = RoomTypes.SUITE.toString();
				else if(ch == 3) title = RoomTypes.DELUXE.toString();
				else System.out.println("Invalid choice. Enter 1, 2, or 3.");
			}
			catch(NumberFormatException e)
			{
				System.out.println("Numeric value only.");
			}
		}

		String acType = null;
		while(acType == null)
		{
			System.out.println("Select AC Type: 1. AC  2. NON-AC");
			System.out.print("Enter Choice (1 - 2): ");
			try
			{
				int ch = Integer.parseInt(io.nextLine().trim());
				if(ch == 1) acType = AcType.AC.toString();
				else if(ch == 2) acType = AcType.NONAC.toString();
				else System.out.println("Invalid choice. Enter 1 or 2.");
			}
			catch(NumberFormatException e)
			{
				System.out.println("Numeric value only.");
			}
		}

		List<Room> rooms = CustomerService.getAvailableRooms(hotelId, checkIn, checkOut, title, acType);

		if(rooms == null || rooms.isEmpty())
		{
			System.out.println("\n❌ No available rooms matching your criteria for the given dates.\n");
			return;
		}

		System.out.println("\n=========================== AVAILABLE ROOMS ===========================\n");
		System.out.printf(" %-8s | %-10s | %-8s | %-10s | %-8s | %-12s | %-8s%n",
			"Room ID", "Room No", "Floor", "Type", "AC", "Price/Night", "Capacity");

		for(Room r : rooms)
		{
			System.out.printf(" %-8d | %-10d | %-8d | %-10s | %-8s | %-12.2f | %-8d%n",
				r.getid(),
				r.getroomNumber(),
				r.getfloorNumber(),
				r.gettitle(),
				r.getactype(),
				r.getbasePrice(),
				r.getcapacity());
		}
		System.out.println();
	}
}