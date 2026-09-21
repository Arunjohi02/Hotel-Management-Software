package src.ui.receptionist;

import java.util.Scanner;
import java.util.List;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoUnit;

import src.model.User;
import src.model.Hotel;
import src.model.Room;
import src.model.Booking;
import src.service.ReceptionistService;
import src.service.CustomerService;
import src.service.UserService;
import src.enums.RoomTypes;
import src.enums.AcType;
import src.enums.PaymentType;
import src.service.PaymentService;
import src.ui.customer.PaymentUI;
import src.ui.customer.RoomAvailability;
import src.util.InputUtil;

public class ReceptionistUI
{
	public static void displayMenu(User staffUser)
	{
		Scanner io = InputUtil.getScanner();
		byte choice = 0;

		do
		{
			System.out.println("\n ================= RECEPTIONIST DASHBOARD ================= \n");
			System.out.println("Logged in as: " + staffUser.getname() + " (" + staffUser.getrole() + ")\n");
			System.out.println("1. Check Room Availability");
			System.out.println("2. Create Booking");
			System.out.println("3. Check-in Customer");
			System.out.println("4. Check-out Customer");
			System.out.println("5. View Today's Bookings");
			System.out.println("6. View Customer Details");
			System.out.println("7. Cancel Booking");
			System.out.println("8. Logout");
			System.out.print("Enter Your Choice : ");

			try
			{
				String input = io.nextLine().trim();
				if(input.isEmpty()) continue;
				choice = Byte.parseByte(input);
			}
			catch(NumberFormatException e)
			{
				System.out.println("\n❌ Invalid Input. Please enter a number (1 - 8).");
				continue;
			}

			switch(choice)
			{
				case 1:
					RoomAvailability.roomAvailability();
					break;

				case 2:
					createBookingFlow(io);
					break;

				case 3:
					checkInFlow(io);
					break;

				case 4:
					checkOutFlow(io);
					break;

				case 5:
					viewTodayBookings();
					break;

				case 6:
					viewCustomerDetailsFlow(io);
					break;

				case 7:
					cancelBookingFlow(io);
					break;

				case 8:
					System.out.println("\nLogging out from Receptionist Dashboard...");
					break;

				default:
					System.out.println("\n❌ Invalid Choice. Select 1 to 8.");
					break;
			}
		} while(choice != 8);
	}

	// 2. CREATE BOOKING
	private static void createBookingFlow(Scanner io)
	{
		System.out.println("\n================ CREATE BOOKING (RECEPTIONIST) ================\n");

		// Step 1: Customer Details
		User customer = null;
		while(customer == null)
		{
			System.out.println("Search Customer by:");
			System.out.println("1. Email");
			System.out.println("2. Phone");
			System.out.println("3. Customer ID");
			System.out.println("4. Cancel");
			System.out.print("Enter Choice (1 - 4): ");

			int type = 0;
			try
			{
				type = Integer.parseInt(io.nextLine().trim());
			}
			catch(Exception e)
			{
				System.out.println("Invalid numeric input.");
				continue;
			}

			if(type == 4)
			{
				System.out.println("Booking creation cancelled.");
				return;
			}

			if(type < 1 || type > 3)
			{
				System.out.println("Invalid choice. Enter 1 to 4.");
				continue;
			}

			// Map choice to searchType: 1->Email, 2->Phone, 3->ID
			int searchType = (type == 1) ? 2 : (type == 2 ? 3 : 1);
			System.out.print("Enter search term: ");
			String query = io.nextLine().trim();

			customer = ReceptionistService.findCustomer(query, searchType);

			if(customer != null)
			{
				System.out.println("\n----- Customer Found -----");
				System.out.println("Customer ID : " + customer.getid());
				System.out.println("Name        : " + customer.getname());
				System.out.println("Email       : " + customer.getemail());
				System.out.println("Phone       : " + customer.getphone());
				System.out.println("DOB         : " + customer.getdob());
				System.out.println("--------------------------");

				System.out.print("Proceed with this customer? (1. Yes / 2. Search Again): ");
				try
				{
					int ok = Integer.parseInt(io.nextLine().trim());
					if(ok != 1)
					{
						customer = null;
					}
				}
				catch(Exception e)
				{
					customer = null;
				}
			}
			else
			{
				System.out.println("\n❌ Customer not found.");
				System.out.print("Would you like to register this customer now? (1. Yes / 2. Search Again / 3. Cancel): ");
				try
				{
					int regChoice = Integer.parseInt(io.nextLine().trim());
					if(regChoice == 1)
					{
						customer = registerNewCustomerInline(io);
					}
					else if(regChoice == 3)
					{
						System.out.println("Booking creation cancelled.");
						return;
					}
				}
				catch(Exception e)
				{
					// loop again
				}
			}
		}

		// Step 2: Location & Hotel Selection
		System.out.print("\nEnter Location : ");
		String location = io.nextLine().trim();

		List<Hotel> hotels = CustomerService.availability(location);
		if(hotels == null || hotels.isEmpty())
		{
			System.out.println("No hotels found in location: " + location);
			return;
		}

		DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("hh:mm a");
		System.out.println("\nAvailable Hotels:");
		System.out.printf(" %-4s | %-20s | %-15s | %-12s | %-12s%n", "ID", "Name", "Phone", "Check-In", "Check-Out");
		for(Hotel h : hotels)
		{
			System.out.printf(" %-4d | %-20s | %-15s | %-12s | %-12s%n",
				h.getid(), h.getname(), h.getphone(),
				h.getcheckInTime().format(timeFormatter),
				h.getcheckoutTime().format(timeFormatter));
		}

		int hotelId = 0;
		String hotelName = "";
		while(true)
		{
			try
			{
				System.out.print("\nEnter Hotel ID: ");
				hotelId = Integer.parseInt(io.nextLine().trim());
				boolean ok = false;
				for(Hotel h : hotels)
				{
					if(h.getid() == hotelId)
					{
						ok = true;
						hotelName = h.getname();
						break;
					}
				}
				if(ok) break;
				System.out.println("Invalid Hotel ID from list.");
			}
			catch(Exception e)
			{
				System.out.println("Numeric ID only.");
			}
		}

		// Step 3: Check-in / Check-out Dates
		LocalDate checkIn = null;
		while(checkIn == null)
		{
			try
			{
				System.out.print("Enter Check-In Date (yyyy-MM-dd): ");
				checkIn = LocalDate.parse(io.nextLine().trim());
				if(checkIn.isBefore(LocalDate.now()))
				{
					System.out.println("Check-in date cannot be in the past. Today is " + LocalDate.now());
					checkIn = null;
				}
			}
			catch(DateTimeParseException e)
			{
				System.out.println("Invalid date format (yyyy-MM-dd).");
			}
		}

		LocalDate checkOut = null;
		while(checkOut == null)
		{
			try
			{
				System.out.print("Enter Check-Out Date (yyyy-MM-dd): ");
				checkOut = LocalDate.parse(io.nextLine().trim());
				if(!checkOut.isAfter(checkIn))
				{
					System.out.println("Check-out date must be after check-in date.");
					checkOut = null;
				}
			}
			catch(DateTimeParseException e)
			{
				System.out.println("Invalid date format (yyyy-MM-dd).");
			}
		}

		// Room Type & AC
		String title = null;
		while(title == null)
		{
			System.out.println("Select Room Type: 1. SINGLE  2. SUITE  3. DELUXE");
			System.out.print("Choice: ");
			try
			{
				int c = Integer.parseInt(io.nextLine().trim());
				if(c == 1) title = RoomTypes.SINGLE.toString();
				else if(c == 2) title = RoomTypes.SUITE.toString();
				else if(c == 3) title = RoomTypes.DELUXE.toString();
				else System.out.println("Enter 1, 2, or 3.");
			}
			catch(Exception e)
			{
				System.out.println("Numeric only.");
			}
		}

		String ac = null;
		while(ac == null)
		{
			System.out.println("Select AC Type: 1. AC  2. NON-AC");
			System.out.print("Choice: ");
			try
			{
				int c = Integer.parseInt(io.nextLine().trim());
				if(c == 1) ac = AcType.AC.toString();
				else if(c == 2) ac = AcType.NONAC.toString();
				else System.out.println("Enter 1 or 2.");
			}
			catch(Exception e)
			{
				System.out.println("Numeric only.");
			}
		}

		List<Room> rooms = CustomerService.getAvailableRooms(hotelId, checkIn, checkOut, title, ac);
		if(rooms == null || rooms.isEmpty())
		{
			System.out.println("\n❌ No available rooms found for those dates and type.");
			return;
		}

		System.out.println("\nAvailable Rooms:");
		System.out.printf(" %-8s | %-8s | %-10s | %-8s | %-12s | %-8s%n",
			"Room ID", "Room No", "Type", "AC", "Price/Night", "Capacity");
		
		for(Room r : rooms)
		{
			System.out.printf(" %-8d | %-8d | %-10s | %-8s | %-12.2f | %-8d%n",
				r.getid(), r.getroomNumber(), r.gettitle(), r.getactype(), r.getbasePrice(), r.getcapacity());
		}

		Room selectedRoom = null;
		while(selectedRoom == null)
		{
			try
			{
				System.out.print("\nEnter Room ID to Book (or 0 to Cancel): ");
				int selId = Integer.parseInt(io.nextLine().trim());
				if(selId == 0)
				{
					System.out.println("Booking cancelled.");
					return;
				}
				for(Room r : rooms)
				{
					if(r.getid() == selId)
					{
						selectedRoom = r;
						break;
					}
				}
				if(selectedRoom == null)
				{
					System.out.println("Room ID not found in available rooms table.");
				}
			}
			catch(Exception e)
			{
				System.out.println("Numeric Room ID only.");
			}
		}

		// Step 4: Calculate Amount
		long nights = ChronoUnit.DAYS.between(checkIn, checkOut);
		double totalAmount = nights * selectedRoom.getbasePrice();

		// Step 5: Confirm Booking
		System.out.println("\n----------------- BOOKING SUMMARY -----------------");
		System.out.println("Customer    : " + customer.getname() + " (ID: " + customer.getid() + ")");
		System.out.println("Hotel       : " + hotelName);
		System.out.println("Room Number : " + selectedRoom.getroomNumber());
		System.out.println("Room Type   : " + selectedRoom.gettitle());
		System.out.println("AC Type     : " + selectedRoom.getactype());
		System.out.println("Check-in    : " + checkIn);
		System.out.println("Check-out   : " + checkOut);
		System.out.println("Nights      : " + nights);
		System.out.printf("Price/Night : Rs. %.2f%n", selectedRoom.getbasePrice());
		System.out.printf("Total Amount: Rs. %.2f%n", totalAmount);
		System.out.println("---------------------------------------------------");

		System.out.print("Proceed to Payment & Confirm Booking? (1. Yes / 2. No): ");
		try
		{
			int confirm = Integer.parseInt(io.nextLine().trim());
			if(confirm == 1)
			{
				PaymentType pType = PaymentUI.selectPaymentType(io);
				if(pType == null)
				{
					System.out.println("\n❌ Payment cancelled. Booking aborted.");
					return;
				}

				Booking booking = new Booking(checkIn, checkOut, "CONFIRMED", totalAmount, customer.getid(), selectedRoom.getroomNumber());
				System.out.println("\nProcessing " + pType + " payment of Rs. " + String.format("%.2f", totalAmount) + "...");
				boolean ok = PaymentService.createBookingWithPayment(booking, selectedRoom.getid(), selectedRoom.getbasePrice(), pType);
				if(ok)
				{
					System.out.println("\n✅ Payment SUCCESS via " + pType + "!");
					System.out.println("✅ Booking created successfully! Booking ID: " + booking.getid());
					System.out.printf("Total Amount Paid: Rs. %.2f%n", totalAmount);
				}
				else
				{
					System.out.println("\n❌ Failed to process payment or save booking.");
				}
			}
			else
			{
				System.out.println("\nBooking cancelled by receptionist.");
			}
		}
		catch(Exception e)
		{
			System.out.println("\nBooking creation aborted.");
		}
	}

	private static User registerNewCustomerInline(Scanner io)
	{
		System.out.println("\n--- Quick Customer Registration ---");
		System.out.print("Customer Name : ");
		String name = io.nextLine().trim();

		LocalDate dob = null;
		while(dob == null)
		{
			try
			{
				System.out.print("DOB (yyyy-MM-dd): ");
				dob = LocalDate.parse(io.nextLine().trim());
			}
			catch(Exception e)
			{
				System.out.println("Format: yyyy-MM-dd");
			}
		}

		System.out.print("Email : ");
		String email = io.nextLine().trim();

		System.out.print("Phone : ");
		String phone = io.nextLine().trim();

		System.out.print("Password (default: pass123) : ");
		String pass = io.nextLine().trim();
		if(pass.isEmpty()) pass = "pass123";

		User user = new User(name, dob, email, phone, "Customer", pass);
		boolean ok = UserService.addUser(user);
		if(ok)
		{
			System.out.println("✅ Customer registered successfully!");
			return ReceptionistService.findCustomer(email, 2);
		}
		else
		{
			System.out.println("❌ Registration failed. Duplicate email or phone.");
			return null;
		}
	}

	// 3. CHECK-IN CUSTOMER
	private static void checkInFlow(Scanner io)
	{
		System.out.println("\n================ CHECK-IN CUSTOMER ================\n");
		System.out.print("Enter Booking ID: ");
		int bookingId = 0;
		try
		{
			bookingId = Integer.parseInt(io.nextLine().trim());
		}
		catch(Exception e)
		{
			System.out.println("Invalid numeric input.");
			return;
		}

		Booking booking = ReceptionistService.getBookingDetails(bookingId);
		if(booking == null)
		{
			System.out.println("❌ Booking #" + bookingId + " not found.");
			return;
		}

		System.out.println("\nBooking Details:");
		System.out.println("Booking ID   : " + booking.getid());
		System.out.println("Customer     : " + booking.getcustomerName());
		System.out.println("Hotel        : " + booking.gethotelName());
		System.out.println("Room Number  : " + booking.getroomNumber() + " (" + booking.getroomTitle() + " " + booking.getacType() + ")");
		System.out.println("Check-in     : " + booking.getcheckIn());
		System.out.println("Check-out    : " + booking.getcheckOut());
		System.out.println("Current Status: " + booking.getstatus());
		System.out.printf("Total Amount : Rs. %.2f%n", booking.getamount());

		System.out.print("\nConfirm Check-In for this customer? (1. Yes / 2. No): ");
		try
		{
			int confirm = Integer.parseInt(io.nextLine().trim());
			if(confirm == 1)
			{
				String result = ReceptionistService.checkIn(bookingId);
				if("SUCCESS".equals(result))
				{
					System.out.println("\n✅ Customer checked in successfully! Room status updated to OCCUPIED.");
				}
				else
				{
					System.out.println("\n❌ Check-in failed: " + result);
				}
			}
			else
			{
				System.out.println("Check-in cancelled.");
			}
		}
		catch(Exception e)
		{
			System.out.println("Operation aborted.");
		}
	}

	// 4. CHECK-OUT CUSTOMER
	private static void checkOutFlow(Scanner io)
	{
		System.out.println("\n================ CHECK-OUT CUSTOMER ================\n");

		System.out.print("Enter Booking ID: ");
		int bookingId = 0;
		try
		{
			bookingId = Integer.parseInt(io.nextLine().trim());
		}
		catch(Exception e)
		{
			System.out.println("Invalid numeric input.");
			return;
		}

		Booking booking = ReceptionistService.getBookingDetails(bookingId);
		if(booking == null)
		{
			System.out.println("❌ Booking #" + bookingId + " not found.");
			return;
		}

		System.out.println("\nBooking Details:");
		System.out.println("Booking ID   : " + booking.getid());
		System.out.println("Customer     : " + booking.getcustomerName());
		System.out.println("Hotel        : " + booking.gethotelName());
		System.out.println("Room Number  : " + booking.getroomNumber());
		System.out.println("Stay Dates   : " + booking.getcheckIn() + " to " + booking.getcheckOut());
		System.out.println("Current Status: " + booking.getstatus());
		System.out.printf("Total Amount : Rs. %.2f%n", booking.getamount());

		if(!"CHECKED_IN".equalsIgnoreCase(booking.getstatus()))
		{
			System.out.println("\n❌ Cannot check out. Booking status is " + booking.getstatus() + " (Must be CHECKED_IN).");
			return;
		}

		double total = booking.getamount();
		double alreadyPaid = PaymentService.getAlreadyPaidAmount(bookingId);
		double remaining = PaymentService.getRemainingAmount(bookingId);

		System.out.println("\n---------------- CHECK-OUT BILL ----------------");
		System.out.printf("Booking Total       : Rs. %.2f%n", total);
		System.out.printf("Already Paid        : Rs. %.2f%n", alreadyPaid);
		System.out.printf("Remaining Amount    : Rs. %.2f%n", remaining);
		System.out.println("------------------------------------------------");

		if(remaining > 0.01)
		{
			System.out.print("\nRemaining balance of Rs. " + String.format("%.2f", remaining) + " must be settled. Collect payment now? (1. Yes / 2. No): ");
			int payChoice = 0;
			try { payChoice = Integer.parseInt(io.nextLine().trim()); } catch(Exception e) {}
			if(payChoice != 1)
			{
				System.out.println("Check-out payment deferred. Check-out not completed.");
				return;
			}

			PaymentType pType = PaymentUI.selectPaymentType(io);
			if(pType == null)
			{
				System.out.println("Payment cancelled. Check-out aborted.");
				return;
			}

			String pResult = PaymentService.processPayment(booking.getuserId(), bookingId, remaining, pType);
			if(!"SUCCESS".equals(pResult))
			{
				System.out.println("❌ Payment failed: " + pResult);
				return;
			}
			System.out.println("✅ Remaining balance paid successfully via " + pType + "!");
		}
		else
		{
			System.out.println("ℹ️ Booking is already fully paid. No additional payment required.");
		}

		System.out.print("\nComplete Check-Out? (1. Yes / 2. No): ");
		try
		{
			int confirm = Integer.parseInt(io.nextLine().trim());
			if(confirm == 1)
			{
				String result = ReceptionistService.checkOut(bookingId);
				if("SUCCESS".equals(result))
				{
					System.out.println("\n✅ Customer checked out successfully! Room status updated to DIRTY.");
				}
				else
				{
					System.out.println("\n❌ Check-out failed: " + result);
				}
			}
			else
			{
				System.out.println("Check-out cancelled.");
			}
		}
		catch(Exception e)
		{
			System.out.println("Operation aborted.");
		}
	}

	// 5. VIEW TODAY'S BOOKINGS
	private static void viewTodayBookings()
	{
		System.out.println("\n================ TODAY'S BOOKINGS (" + LocalDate.now() + ") ================\n");

		List<Booking> bookings = ReceptionistService.getTodayBookings();

		if(bookings == null || bookings.isEmpty())
		{
			System.out.println("No active bookings scheduled for today.\n");
			return;
		}

		System.out.printf(" %-10s | %-16s | %-16s | %-8s | %-12s | %-12s | %-12s | %-12s%n",
			"Booking ID", "Customer", "Hotel", "Room No", "Check-In", "Check-Out", "Amount (Rs)", "Status");

		for(Booking b : bookings)
		{
			System.out.printf(" %-10d | %-16s | %-16s | %-8d | %-12s | %-12s | %-12.2f | %-12s%n",
				b.getid(),
				b.getcustomerName(),
				b.gethotelName(),
				b.getroomNumber(),
				b.getcheckIn(),
				b.getcheckOut(),
				b.getamount(),
				b.getstatus());
		}
		System.out.println();
	}

	// 6. VIEW CUSTOMER DETAILS
	private static void viewCustomerDetailsFlow(Scanner io)
	{
		System.out.println("\n================ VIEW CUSTOMER DETAILS ================\n");
		System.out.println("Search Customer by:");
		System.out.println("1. Customer ID");
		System.out.println("2. Email");
		System.out.println("3. Phone");
		System.out.print("Enter Choice (1 - 3): ");

		int type = 0;
		try
		{
			type = Integer.parseInt(io.nextLine().trim());
		}
		catch(Exception e)
		{
			System.out.println("Invalid numeric input.");
			return;
		}

		if(type < 1 || type > 3)
		{
			System.out.println("Invalid choice.");
			return;
		}

		System.out.print("Enter search Info : ");
		String term = io.nextLine().trim();

		User customer = ReceptionistService.findCustomer(term, type);

		if(customer != null)
		{
			System.out.println("\n---------------- CUSTOMER PROFILE ----------------");
			System.out.println("Customer ID : " + customer.getid());
			System.out.println("Full Name   : " + customer.getname());
			System.out.println("Email       : " + customer.getemail());
			System.out.println("Phone       : " + customer.getphone());
			System.out.println("DOB         : " + customer.getdob());
			System.out.println("Member Since: " + customer.getcreateDate());
			System.out.println("--------------------------------------------------");

			System.out.print("\nView payment history for this customer? (1. Yes / 2. No): ");
			try
			{
				int pChoice = Integer.parseInt(io.nextLine().trim());
				if(pChoice == 1)
				{
					PaymentUI.viewPaymentHistory(customer);
				}
			}
			catch(Exception ignored) {}
		}
		else
		{
			System.out.println("\n❌ No customer found matching: " + term);
		}
		System.out.println();
	}

	// 7. CANCEL BOOKING
	private static void cancelBookingFlow(Scanner io)
	{
		System.out.println("\n================ CANCEL BOOKING ================\n");

		System.out.print("Enter Booking ID to Cancel: ");
		int bookingId = 0;
		
		try
		{
			bookingId = Integer.parseInt(io.nextLine().trim());
		}
		catch(Exception e)
		{
			System.out.println("Invalid numeric input.");
			return;
		}

		Booking booking = ReceptionistService.getBookingDetails(bookingId);
		if(booking == null)
		{
			System.out.println("❌ Booking #" + bookingId + " not found.");
			return;
		}

		System.out.println("\nBooking Details:");
		System.out.println("Booking ID   : " + booking.getid());
		System.out.println("Customer     : " + booking.getcustomerName());
		System.out.println("Hotel        : " + booking.gethotelName());
		System.out.println("Room Number  : " + booking.getroomNumber());
		System.out.println("Dates        : " + booking.getcheckIn() + " to " + booking.getcheckOut());
		System.out.println("Status       : " + booking.getstatus());
		System.out.printf("Total Amount : Rs. %.2f%n", booking.getamount());

		System.out.print("\nAre you sure you want to cancel this booking? (1. Yes / 2. No): ");
		try
		{
			int confirm = Integer.parseInt(io.nextLine().trim());
			if(confirm == 1)
			{
				String result = ReceptionistService.cancelBooking(bookingId);
				if("SUCCESS".equals(result))
				{
					System.out.println("\n✅ Booking #" + bookingId + " has been cancelled successfully.");
				}
				else
				{
					System.out.println("\n❌ Cancellation failed: " + result);
				}
			}
			else
			{
				System.out.println("Cancellation aborted.");
			}
		}
		catch(Exception e)
		{
			System.out.println("Operation aborted.");
		}
	}
}
