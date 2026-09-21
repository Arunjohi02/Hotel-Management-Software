package src.ui.admin ;

import java.util.Scanner ;

import src.model.Hotel ;
import src.model.Room ;

import src.enums.AcType ;
import src.enums.RoomTypes ;
import src.enums.Status ;

import src.service.HotelService ;

import src.exception.InputException;

import java.time.LocalTime ;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException ;

import src.util.InputUtil;

public class AddHotel
{
	static Scanner io = InputUtil.getScanner();
	static Byte choice ;
	public static void addHotel()
	{
		System.out.print("Name 		   : ") ;

		String name = io.nextLine() ;

		System.out.print("Location 	   : ") ;
		String location = io.nextLine() ;

		System.out.print("Phone 	   : ") ;
		String phone = io.nextLine() ;
		
		System.out.print("Email 	   : ") ;
		String email = io.nextLine() ;
		
		System.out.print("Description  : ") ;
		String description = io.nextLine() ;
		
		LocalTime checkin = null;
		while(checkin == null)
		{
			try
			{
				System.out.print("CheckInTime (HH:mm, e.g. 09:00): ");
				String checkinInput = io.nextLine().trim();
				checkin = LocalTime.parse(checkinInput);
			}
			catch(DateTimeParseException e)
			{
				System.out.println("Invalid time format. Please use HH:mm (e.g. 09:00 or 14:30)");
			}
		}
		
		LocalTime checkout = null;
		while(checkout == null)
		{
			try
			{
				System.out.print("CheckoutTime (HH:mm, e.g. 11:00): ");
				String checkoutInput = io.nextLine().trim();
				checkout = LocalTime.parse(checkoutInput);
			}
			catch(DateTimeParseException e)
			{
				System.out.println("Invalid time format. Please use HH:mm (e.g. 11:00 or 18:00)");
			}
		}

		int totalrooms = 0;
		while(totalrooms <= 0)
		{
			try
			{
				System.out.print("TotalRooms   : ");
				totalrooms = io.nextInt();
				io.nextLine();
				if(totalrooms <= 0)
				{
					System.out.println("Total rooms must be at least 1.");
				}
			}
			catch(Exception e)
			{
				System.out.println("Invalid Input. Numeric values only.");
				io.nextLine();
			}
		}

		Hotel newhotel = new Hotel(
				name,
				location,
				phone,
				email,
				description,
				checkin,
				checkout,
				totalrooms
		);

// Add Room Information

		System.out.println(" \n ------ Room Info ------ \n ") ;

		Room[] roomsInfo = new Room[totalrooms];
		Room room ;

		int i = 1;

		while(i <= totalrooms)
		{
			int room_number = i ;

			System.out.print("Floor Number for Room " + room_number + " : ") ;
			int floor_number = 0;
			try
			{
				floor_number = io.nextInt();
				io.nextLine();
			}
			catch(Exception e)
			{
				io.nextLine();
			}

			String status = (Status.AVAILABLE).toString();

			String title = null ;
			while(title == null)
			{
				System.out.print("Title (SINGLE :1 / SUITE :2 / DELUXE : 3): ") ;
				try
				{
					choice = io.nextByte();
					io.nextLine();

					if(choice == 1)
					{
						title = (RoomTypes.SINGLE).toString();
					}
					else if(choice == 2)
					{
						title = (RoomTypes.SUITE).toString();
					}
					else if(choice == 3)
					{
						title = (RoomTypes.DELUXE).toString();
					}
					else
					{
						System.out.println("Invalid choice. Enter 1, 2, or 3.") ;
					}
				}
				catch(Exception e)
				{
					System.out.println("Enter numeric value only.");
					io.nextLine();
				}
			}

			String type = null;
			while(type == null)
			{
				System.out.print("AcType (Ac:1 / NON_Ac :2) : ") ;
				try
				{
					choice = io.nextByte();
					io.nextLine();

					if(choice == 1)
					{
						type = (AcType.AC).toString();
					}
					else if(choice == 2)
					{
						type = (AcType.NONAC).toString();
					}
					else
					{
						System.out.println("Invalid choice. Enter 1 or 2.") ;
					}
				}
				catch(Exception e)
				{
					System.out.println("Enter numeric value only.");
					io.nextLine();
				}
			}

			double price = 0;
			try
			{
				System.out.print("Price (/day) : ");
				price = io.nextDouble();
			}
			catch(Exception e)
			{
				io.nextLine();
			}

			int capacity = 1;
			try
			{
				System.out.print("Capacity :");
				capacity = io.nextInt();
				io.nextLine();
			}
			catch(Exception e)
			{
				io.nextLine();
			}
			
			System.out.println();
			
			room = new Room(room_number, floor_number, status, title, type, price, capacity);

			roomsInfo[i-1] = room ;
			i++;
		}

		String result=(HotelService.addHotel(newhotel,roomsInfo))? "\n✅ Register SuccessFully......\n" : "\n❌ Faild Register\n" ;

		System.out.println(result);
	}
}