package src.ui.manager;

import java.util.Scanner;
import java.util.List;
import java.util.Map;

import src.model.User;
import src.model.Hotel;
import src.model.Room;
import src.model.Booking;
import src.service.ManagerService;
import src.service.HotelService;
import src.service.PaymentService;
import src.enums.PaymentType;
import src.ui.customer.PaymentUI;
import src.ui.customer.RoomAvailability;
import src.util.InputUtil;

public class ManagerUI
{
	public static void displayMenu(User managerUser)
	{
		Scanner io = InputUtil.getScanner();
		byte choice = 0;

		do
		{
			System.out.println("\n ==================== MANAGER DASHBOARD ==================== \n");
			System.out.println("Logged in as: " + managerUser.getname() + " (" + managerUser.getrole() + ")\n");
			System.out.println("1. Room Management");
			System.out.println("2. Booking Management");
			System.out.println("3. Check-in");
			System.out.println("4. Check-out");
			System.out.println("5. Customer Details");
			System.out.println("6. Reports");
			System.out.println("7. Logout");
			System.out.print("Enter Your Choice : ");

			try
			{
				String input = io.nextLine().trim();
				if(input.isEmpty()) continue;
				choice = Byte.parseByte(input);
			}
			catch(NumberFormatException e)
			{
				System.out.println("\n❌ Invalid Input. Please enter a number (1 - 7).");
				continue;
			}

			switch(choice)
			{
				case 1:
					roomManagementMenu(io);
					break;

				case 2:
					bookingManagementMenu(io);
					break;

				case 3:
					checkInFlow(io);
					break;

				case 4:
					checkOutFlow(io);
					break;

				case 5:
					customerDetailsMenu(io);
					break;

				case 6:
					reportsMenu(io);
					break;

				case 7:
					System.out.println("\nLogging out from Manager Dashboard...");
					break;

				default:
					System.out.println("\n❌ Invalid Choice. Select 1 to 7.");
					break;
			}
		} while(choice != 7);
	}

	// 1. ROOM MANAGEMENT

	private static void roomManagementMenu(Scanner io)
	{
		byte choice = 0;
		do
		{
			System.out.println("\n----------------- ROOM MANAGEMENT -----------------");
			System.out.println("1. View All Rooms");
			System.out.println("2. View Rooms by Hotel");
			System.out.println("3. Check Room Availability");
			System.out.println("4. Update Room Status");
			System.out.println("5. Back to Manager Menu");
			System.out.print("Enter Your Choice : ");

			try
			{
				String input = io.nextLine().trim();
				if(input.isEmpty()) continue;
				choice = Byte.parseByte(input);
			}
			catch(NumberFormatException e)
			{
				System.out.println("\n❌ Invalid Input. Enter 1 to 5.");
				continue;
			}

			switch(choice)
			{
				case 1:
					displayRooms(ManagerService.getAllRooms(0), "ALL ROOMS ACROSS HOTELS");
					break;

				case 2:
					viewRoomsByHotelFlow(io);
					break;

				case 3:
					RoomAvailability.roomAvailability();
					break;

				case 4:
					updateRoomStatusFlow(io);
					break;

				case 5:
					break;

				default:
					System.out.println("\n❌ Invalid Choice. Select 1 to 5.");
					break;
			}
		} while(choice != 5);
	}

	private static void viewRoomsByHotelFlow(Scanner io)
	{
		List<Hotel> hotels = HotelService.allhotels();
		if(hotels == null || hotels.isEmpty())
		{
			System.out.println("\n❌ No hotels found.");
			return;
		}

		System.out.println("\nAvailable Hotels:");
		System.out.printf(" %-4s | %-25s | %-20s%n", "ID", "Hotel Name", "Location");

		for(Hotel h : hotels)
		{
			System.out.printf(" %-4d | %-25s | %-20s%n", h.getid(), h.getname(), h.getlocation());
		}

		System.out.print("\nEnter Hotel ID to view rooms: ");
		try
		{
			int hotelId = Integer.parseInt(io.nextLine().trim());
			String hotelName = "Hotel #" + hotelId;
			for(Hotel h : hotels)
			{
				if(h.getid() == hotelId)
				{
					hotelName = h.getname();
					break;
				}
			}
			List<Room> rooms = ManagerService.getAllRooms(hotelId);
			displayRooms(rooms, "ROOMS FOR: " + hotelName);
		}
		catch(NumberFormatException e)
		{
			System.out.println("❌ Invalid Hotel ID. Must be numeric.");
		}
	}

	private static void displayRooms(List<Room> rooms, String header)
	{
		System.out.println("\n================ " + header + " ================\n");
		if(rooms == null || rooms.isEmpty())
		{
			System.out.println("No rooms found.\n");
			return;
		}

		System.out.printf(" %-8s | %-22s | %-8s | %-6s | %-12s | %-8s | %-12s | %-12s%n",
			"Room ID", "Hotel Name", "Room No", "Floor", "Type", "AC", "Price/Night", "Status");

		for(Room r : rooms)
		{
			System.out.printf(" %-8d | %-22s | %-8d | %-6d | %-12s | %-8s | Rs. %-8.2f | %-12s%n",
				r.getid(),
				r.gethotelName(),
				r.getroomNumber(),
				r.getfloorNumber(),
				r.gettitle(),
				r.getactype(),
				r.getbasePrice(),
				r.getstatus());
		}
		System.out.println();
	}

	private static void updateRoomStatusFlow(Scanner io)
	{
		System.out.println("\n---------------- UPDATE ROOM STATUS ----------------");
		System.out.print("Enter Room ID: ");
		int roomId = 0;
		try
		{
			roomId = Integer.parseInt(io.nextLine().trim());
		}
		catch(NumberFormatException e)
		{
			System.out.println("❌ Invalid Room ID.");
			return;
		}

		System.out.println("\nSelect New Status:");
		System.out.println("1. AVAILABLE");
		System.out.println("2. OCCUPIED");
		System.out.println("3. DIRTY");
		System.out.println("4. MAINTENANCE");
		System.out.print("Enter Choice (1 - 4): ");

		String newStatus = null;
		try
		{
			int statusChoice = Integer.parseInt(io.nextLine().trim());
			switch(statusChoice)
			{
				case 1: newStatus = "AVAILABLE"; break;
				case 2: newStatus = "OCCUPIED"; break;
				case 3: newStatus = "DIRTY"; break;
				case 4: newStatus = "MAINTENANCE"; break;
				default:
					System.out.println("❌ Invalid status choice.");
					return;
			}
		}
		catch(NumberFormatException e)
		{
			System.out.println("❌ Invalid input.");
			return;
		}

		boolean ok = ManagerService.updateRoomStatus(roomId, newStatus);
		if(ok)
		{
			System.out.println("\n✅ Room ID " + roomId + " status successfully updated to " + newStatus + ".");
		}
		else
		{
			System.out.println("\n❌ Failed to update room status. Please verify Room ID.");
		}
	}

	// ==========================================
	// 2. BOOKING MANAGEMENT
	// ==========================================
	private static void bookingManagementMenu(Scanner io)
	{
		byte choice = 0;
		do
		{
			System.out.println("\n----------------- BOOKING MANAGEMENT -----------------");
			System.out.println("1. View All Bookings");
			System.out.println("2. View Booking Details");
			System.out.println("3. Confirm Booking");
			System.out.println("4. Cancel Booking");
			System.out.println("5. Back to Manager Menu");
			System.out.print("Enter Your Choice : ");

			try
			{
				String input = io.nextLine().trim();
				if(input.isEmpty()) continue;
				choice = Byte.parseByte(input);
			}
			catch(NumberFormatException e)
			{
				System.out.println("\n❌ Invalid Input. Enter 1 to 5.");
				continue;
			}

			switch(choice)
			{
				case 1:
					displayAllBookings();
					break;

				case 2:
					viewBookingDetailsFlow(io);
					break;

				case 3:
					confirmBookingFlow(io);
					break;

				case 4:
					cancelBookingFlow(io);
					break;

				case 5:
					break;

				default:
					System.out.println("\n❌ Invalid Choice. Select 1 to 5.");
					break;
			}
		} while(choice != 5);
	}

	private static void displayAllBookings()
	{
		System.out.println("\n================ ALL BOOKINGS ACROSS HOTELS ================\n");
		List<Booking> bookings = ManagerService.getAllBookings();

		if(bookings == null || bookings.isEmpty())
		{
			System.out.println("No bookings recorded in the system.\n");
			return;
		}

		System.out.printf(" %-10s | %-16s | %-18s | %-8s | %-12s | %-12s | %-12s | %-12s%n",
			"Booking ID", "Customer Name", "Hotel Name", "Room No", "Check-In", "Check-Out", "Amount (Rs)", "Status");
		System.out.println("--------------------------------------------------------------------------------------------------------------------");

		for(Booking b : bookings)
		{
			System.out.printf(" %-10d | %-16s | %-18s | %-8d | %-12s | %-12s | %-12.2f | %-12s%n",
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

	private static void viewBookingDetailsFlow(Scanner io)
	{
		System.out.print("\nEnter Booking ID: ");
		try
		{
			int bookingId = Integer.parseInt(io.nextLine().trim());
			Booking booking = ManagerService.getBookingDetails(bookingId);
			if(booking == null)
			{
				System.out.println("❌ Booking #" + bookingId + " not found.");
				return;
			}

			System.out.println("\n================ BOOKING FULL DETAILS ================");
			System.out.println("Booking ID     : " + booking.getid());
			System.out.println("Customer Name  : " + booking.getcustomerName());
			System.out.println("Customer Email : " + booking.getcustomerEmail());
			System.out.println("Customer Phone : " + booking.getcustomerPhone());
			System.out.println("Hotel Name     : " + booking.gethotelName());
			System.out.println("Room Number    : " + booking.getroomNumber());
			System.out.println("Room Category  : " + booking.getroomTitle() + " (" + booking.getacType() + ")");
			System.out.println("Check-in Date  : " + booking.getcheckIn());
			System.out.println("Check-out Date : " + booking.getcheckOut());
			System.out.printf("Price Per Night: Rs. %.2f%n", booking.getpricePerNight());
			System.out.printf("Total Amount   : Rs. %.2f%n", booking.getamount());
			System.out.println("Booking Status : " + booking.getstatus());
			System.out.println("======================================================\n");
		}
		catch(NumberFormatException e)
		{
			System.out.println("❌ Invalid Booking ID format.");
		}
	}

	private static void confirmBookingFlow(Scanner io)
	{
		System.out.print("\nEnter Booking ID to Confirm: ");
		try
		{
			int bookingId = Integer.parseInt(io.nextLine().trim());
			Booking booking = ManagerService.getBookingDetails(bookingId);
			if(booking == null)
			{
				System.out.println("❌ Booking #" + bookingId + " not found.");
				return;
			}

			System.out.println("\nBooking ID    : " + booking.getid());
			System.out.println("Customer Name : " + booking.getcustomerName());
			System.out.println("Hotel         : " + booking.gethotelName());
			System.out.println("Room Number   : " + booking.getroomNumber());
			System.out.println("Current Status: " + booking.getstatus());

			if("CONFIRMED".equalsIgnoreCase(booking.getstatus()))
			{
				System.out.println("\nℹ️ Booking is already CONFIRMED.");
				return;
			}
			if("CANCELLED".equalsIgnoreCase(booking.getstatus()))
			{
				System.out.println("\n❌ Cannot confirm a CANCELLED booking.");
				return;
			}

			System.out.print("\nConfirm this booking? (1. Yes / 2. No): ");
			int confirm = Integer.parseInt(io.nextLine().trim());
			if(confirm == 1)
			{
				boolean ok = ManagerService.confirmBooking(bookingId);
				if(ok)
				{
					System.out.println("\n✅ Booking #" + bookingId + " successfully confirmed!");
				}
				else
				{
					System.out.println("\n❌ Failed to confirm booking.");
				}
			}
			else
			{
				System.out.println("Confirmation cancelled.");
			}
		}
		catch(NumberFormatException e)
		{
			System.out.println("❌ Numeric Booking ID required.");
		}
	}

	private static void cancelBookingFlow(Scanner io)
	{
		System.out.print("\nEnter Booking ID to Cancel: ");
		try
		{
			int bookingId = Integer.parseInt(io.nextLine().trim());
			Booking booking = ManagerService.getBookingDetails(bookingId);
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

			if("CANCELLED".equalsIgnoreCase(booking.getstatus()))
			{
				System.out.println("\nℹ️ This booking is already CANCELLED.");
				return;
			}
			if("CHECKED_OUT".equalsIgnoreCase(booking.getstatus()))
			{
				System.out.println("\n❌ Cannot cancel a completed stay (already CHECKED_OUT).");
				return;
			}

			System.out.print("\nAre you sure you want to cancel this booking? (1. Yes / 2. No): ");
			int confirm = Integer.parseInt(io.nextLine().trim());
			if(confirm == 1)
			{
				String result = ManagerService.cancelBooking(bookingId);
				if("SUCCESS".equals(result))
				{
					System.out.println("\n✅ Booking #" + bookingId + " has been successfully cancelled.");
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
		catch(NumberFormatException e)
		{
			System.out.println("❌ Numeric input required.");
		}
	}

	// ==========================================
	// 3. CHECK-IN
	// ==========================================
	private static void checkInFlow(Scanner io)
	{
		System.out.println("\n================ CHECK-IN ================\n");
		System.out.print("Enter Booking ID: ");
		try
		{
			int bookingId = Integer.parseInt(io.nextLine().trim());
			Booking booking = ManagerService.getBookingDetails(bookingId);
			if(booking == null)
			{
				System.out.println("❌ Booking #" + bookingId + " not found.");
				return;
			}

			System.out.println("\nBooking Details:");
			System.out.println("Booking ID    : " + booking.getid());
			System.out.println("Customer Name : " + booking.getcustomerName());
			System.out.println("Hotel         : " + booking.gethotelName());
			System.out.println("Room Number   : " + booking.getroomNumber() + " (" + booking.getroomTitle() + " " + booking.getacType() + ")");
			System.out.println("Check-in Date : " + booking.getcheckIn());
			System.out.println("Check-out Date: " + booking.getcheckOut());
			System.out.println("Current Status: " + booking.getstatus());
			System.out.printf("Total Amount  : Rs. %.2f%n", booking.getamount());

			System.out.print("\nConfirm Check-In? (1. Yes / 2. No): ");
			int confirm = Integer.parseInt(io.nextLine().trim());
			if(confirm == 1)
			{
				String result = ManagerService.checkIn(bookingId);
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
		catch(NumberFormatException e)
		{
			System.out.println("❌ Numeric input required.");
		}
	}

	// ==========================================
	// 4. CHECK-OUT
	// ==========================================
	private static void checkOutFlow(Scanner io)
	{
		System.out.println("\n================ CHECK-OUT ================\n");
		System.out.print("Enter Booking ID: ");
		try
		{
			int bookingId = Integer.parseInt(io.nextLine().trim());
			Booking booking = ManagerService.getBookingDetails(bookingId);
			if(booking == null)
			{
				System.out.println("❌ Booking #" + bookingId + " not found.");
				return;
			}

			System.out.println("\nBooking Details:");
			System.out.println("Booking ID    : " + booking.getid());
			System.out.println("Customer Name : " + booking.getcustomerName());
			System.out.println("Hotel         : " + booking.gethotelName());
			System.out.println("Room Number   : " + booking.getroomNumber());
			System.out.println("Stay Dates    : " + booking.getcheckIn() + " to " + booking.getcheckOut());
			System.out.println("Current Status: " + booking.getstatus());
			System.out.printf("Total Amount  : Rs. %.2f%n", booking.getamount());

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
			int confirm = Integer.parseInt(io.nextLine().trim());
			if(confirm == 1)
			{
				String result = ManagerService.checkOut(bookingId);
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
		catch(NumberFormatException e)
		{
			System.out.println("❌ Numeric input required.");
		}
	}

	// ==========================================
	// 5. CUSTOMER DETAILS
	// ==========================================
	private static void customerDetailsMenu(Scanner io)
	{
		byte choice = 0;
		do
		{
			System.out.println("\n----------------- CUSTOMER DETAILS -----------------");
			System.out.println("1. View All Registered Customers");
			System.out.println("2. Search Customer");
			System.out.println("3. View Customer Payment History");
			System.out.println("4. Back to Manager Menu");
			System.out.print("Enter Your Choice : ");

			try
			{
				String input = io.nextLine().trim();
				if(input.isEmpty()) continue;
				choice = Byte.parseByte(input);
			}
			catch(NumberFormatException e)
			{
				System.out.println("\n❌ Invalid Input. Enter 1 to 4.");
				continue;
			}

			switch(choice)
			{
				case 1:
					displayAllCustomers();
					break;

				case 2:
					searchCustomerFlow(io);
					break;

				case 3:
					PaymentUI.searchPaymentHistory(io);
					break;

				case 4:
					break;

				default:
					System.out.println("\n❌ Invalid Choice. Select 1 to 4.");
					break;
			}
		} while(choice != 4);
	}

	private static void displayAllCustomers()
	{
		System.out.println("\n================ REGISTERED CUSTOMERS ================\n");
		List<User> customers = ManagerService.getAllCustomers();

		if(customers == null || customers.isEmpty())
		{
			System.out.println("No registered customers found.\n");
			return;
		}

		System.out.printf(" %-6s | %-20s | %-28s | %-15s | %-12s%n",
			"ID", "Name", "Email", "Phone", "Registered");
		System.out.println("---------------------------------------------------------------------------------------------");

		for(User c : customers)
		{
			System.out.printf(" %-6d | %-20s | %-28s | %-15s | %-12s%n",
				c.getid(),
				c.getname(),
				c.getemail(),
				c.getphone(),
				c.getcreateDate());
		}
		System.out.println();
	}

	private static void searchCustomerFlow(Scanner io)
	{
		System.out.println("\nSearch Customer by:");
		System.out.println("1. Customer ID");
		System.out.println("2. Email");
		System.out.println("3. Phone");
		System.out.print("Enter Choice (1 - 3): ");

		int type = 0;
		try
		{
			type = Integer.parseInt(io.nextLine().trim());
		}
		catch(NumberFormatException e)
		{
			System.out.println("❌ Invalid choice.");
			return;
		}

		if(type < 1 || type > 3)
		{
			System.out.println("❌ Choice must be 1, 2, or 3.");
			return;
		}

		System.out.print("Enter Search Term: ");
		String term = io.nextLine().trim();

		User customer = ManagerService.findCustomer(term, type);
		if(customer != null)
		{
			System.out.println("\n---------------- CUSTOMER PROFILE ----------------");
			System.out.println("Customer ID  : " + customer.getid());
			System.out.println("Full Name    : " + customer.getname());
			System.out.println("Email        : " + customer.getemail());
			System.out.println("Phone        : " + customer.getphone());
			System.out.println("DOB          : " + customer.getdob());
			System.out.println("Member Since : " + customer.getcreateDate());
			System.out.println("Role         : " + customer.getrole());
			System.out.println("--------------------------------------------------\n");
		}
		else
		{
			System.out.println("\n❌ No customer found matching: " + term + "\n");
		}
	}

	// ==========================================
	// 6. REPORTS
	// ==========================================
	private static void reportsMenu(Scanner io)
	{
		byte choice = 0;
		do
		{
			System.out.println("\n----------------- MANAGEMENT REPORTS -----------------");
			System.out.println("1. Booking Report");
			System.out.println("2. Room Report");
			System.out.println("3. Revenue Report");
			System.out.println("4. Back to Manager Menu");
			System.out.print("Enter Your Choice : ");

			try
			{
				String input = io.nextLine().trim();
				if(input.isEmpty()) continue;
				choice = Byte.parseByte(input);
			}
			catch(NumberFormatException e)
			{
				System.out.println("\n❌ Invalid Input. Enter 1 to 4.");
				continue;
			}

			switch(choice)
			{
				case 1:
					displayBookingReport();
					break;

				case 2:
					displayRoomReport();
					break;

				case 3:
					displayRevenueReport();
					break;

				case 4:
					break;

				default:
					System.out.println("\n❌ Invalid Choice. Select 1 to 4.");
					break;
			}
		} while(choice != 4);
	}

	private static void displayBookingReport()
	{
		System.out.println("\n======================= BOOKING REPORT =======================");
		Map<String, Integer> counts = ManagerService.getBookingReportCounts();

		int total = counts.getOrDefault("TOTAL", 0);
		int confirmed = counts.getOrDefault("CONFIRMED", 0);
		int checkedIn = counts.getOrDefault("CHECKED_IN", 0);
		int checkedOut = counts.getOrDefault("CHECKED_OUT", 0);
		int cancelled = counts.getOrDefault("CANCELLED", 0);

		System.out.println("Total Bookings in System : " + total);
		System.out.println("--------------------------------------------------------------");
		System.out.printf(" %-15s : %-6d (%.1f%%)%n", "CONFIRMED", confirmed, total > 0 ? (confirmed * 100.0 / total) : 0.0);
		System.out.printf(" %-15s : %-6d (%.1f%%)%n", "CHECKED_IN", checkedIn, total > 0 ? (checkedIn * 100.0 / total) : 0.0);
		System.out.printf(" %-15s : %-6d (%.1f%%)%n", "CHECKED_OUT", checkedOut, total > 0 ? (checkedOut * 100.0 / total) : 0.0);
		System.out.printf(" %-15s : %-6d (%.1f%%)%n", "CANCELLED", cancelled, total > 0 ? (cancelled * 100.0 / total) : 0.0);
		System.out.println("==============================================================\n");
	}

	private static void displayRoomReport()
	{
		System.out.println("\n======================== ROOM REPORT ========================");
		Map<String, Integer> counts = ManagerService.getRoomReportCounts();

		int total = counts.getOrDefault("TOTAL", 0);
		int available = counts.getOrDefault("AVAILABLE", 0);
		int occupied = counts.getOrDefault("OCCUPIED", 0);
		int dirty = counts.getOrDefault("DIRTY", 0);
		int maintenance = counts.getOrDefault("MAINTENANCE", 0);

		System.out.println("Total Rooms in System    : " + total);
		System.out.println("--------------------------------------------------------------");
		System.out.printf(" %-15s : %-6d (%.1f%%)%n", "AVAILABLE", available, total > 0 ? (available * 100.0 / total) : 0.0);
		System.out.printf(" %-15s : %-6d (%.1f%%)%n", "OCCUPIED", occupied, total > 0 ? (occupied * 100.0 / total) : 0.0);
		System.out.printf(" %-15s : %-6d (%.1f%%)%n", "DIRTY", dirty, total > 0 ? (dirty * 100.0 / total) : 0.0);
		System.out.printf(" %-15s : %-6d (%.1f%%)%n", "MAINTENANCE", maintenance, total > 0 ? (maintenance * 100.0 / total) : 0.0);
		System.out.println("==============================================================\n");
	}

	private static void displayRevenueReport()
	{
		System.out.println("\n======================= REVENUE REPORT =======================");
		double totalRevenue = ManagerService.getTotalRevenue();
		Map<String, Double> revenueByHotel = ManagerService.getRevenueByHotel();

		System.out.printf("Total Realized/Active Revenue : Rs. %.2f%n", totalRevenue);
		System.out.println("--------------------------------------------------------------");
		System.out.println("Revenue Breakdown by Hotel:");
		System.out.printf(" %-30s | %-15s | %-10s%n", "Hotel Name", "Revenue (Rs)", "Share (%)");
		System.out.println("--------------------------------------------------------------");

		if(revenueByHotel == null || revenueByHotel.isEmpty())
		{
			System.out.println("No hotel revenue data available.");
		}
		else
		{
			for(Map.Entry<String, Double> entry : revenueByHotel.entrySet())
			{
				double rev = entry.getValue();
				double share = totalRevenue > 0 ? (rev * 100.0 / totalRevenue) : 0.0;
				System.out.printf(" %-30s | Rs. %-11.2f | %-9.1f%%%n", entry.getKey(), rev, share);
			}
		}
		System.out.println("==============================================================\n");
	}
}
