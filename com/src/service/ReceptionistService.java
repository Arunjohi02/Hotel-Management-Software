package src.service;

import java.util.List;
import src.model.User;
import src.model.Booking;
import src.dao.UserDAO;
import src.dao.CustomerDAO;

public class ReceptionistService
{
	public static User findCustomer(String query, int searchType)
	{
		if(query == null || query.trim().isEmpty())
		{
			return null;
		}

		query = query.trim();

		switch(searchType)
		{
			case 1: // By Customer ID
				try
				{
					int id = Integer.parseInt(query);
					return UserDAO.findCustomerById(id);
				}
				catch(NumberFormatException e)
				{
					return null;
				}

			case 2: // By Email
				return UserDAO.findCustomerByEmail(query);

			case 3: // By Phone
				return UserDAO.findCustomerByPhone(query);

			default:
				return null;
		}
	}

	public static boolean registerCustomer(User customer)
	{
		return UserService.addUser(customer);
	}

	public static String checkIn(int bookingId)
	{
		if(bookingId <= 0)
		{
			return "Invalid Booking ID.";
		}

		Booking booking = CustomerDAO.getBookingById(bookingId);
		if(booking == null)
		{
			return "Booking #" + bookingId + " not found.";
		}

		String status = booking.getstatus();
		if("CHECKED_IN".equalsIgnoreCase(status))
		{
			return "Customer is already checked in for this booking.";
		}
		if("CHECKED_OUT".equalsIgnoreCase(status))
		{
			return "Booking is already completed and checked out.";
		}
		if("CANCELLED".equalsIgnoreCase(status))
		{
			return "Cannot check in. This booking is CANCELLED.";
		}

		boolean success = CustomerDAO.checkInCustomer(bookingId);
		return success ? "SUCCESS" : "Database error while processing check-in.";
	}

	public static String checkOut(int bookingId)
	{
		if(bookingId <= 0)
		{
			return "Invalid Booking ID.";
		}

		Booking booking = CustomerDAO.getBookingById(bookingId);
		if(booking == null)
		{
			return "Booking #" + bookingId + " not found.";
		}

		String status = booking.getstatus();
		if(!"CHECKED_IN".equalsIgnoreCase(status))
		{
			return "Cannot check out. Customer is not currently checked in (Status: " + status + ").";
		}

		boolean success = CustomerDAO.checkOutCustomer(bookingId);
		return success ? "SUCCESS" : "Database error while processing check-out.";
	}

	public static List<Booking> getTodayBookings()
	{
		return CustomerDAO.getTodayBookings();
	}

	public static String cancelBooking(int bookingId)
	{
		if(bookingId <= 0)
		{
			return "Invalid Booking ID.";
		}

		Booking booking = CustomerDAO.getBookingById(bookingId);
		if(booking == null)
		{
			return "Booking #" + bookingId + " not found.";
		}

		String status = booking.getstatus();
		if("CANCELLED".equalsIgnoreCase(status))
		{
			return "Booking is already cancelled.";
		}
		if("CHECKED_OUT".equalsIgnoreCase(status))
		{
			return "Cannot cancel a completed (checked out) booking.";
		}

		boolean success = CustomerDAO.cancelBookingByStaff(bookingId);
		return success ? "SUCCESS" : "Database error while cancelling booking.";
	}

	public static Booking getBookingDetails(int bookingId)
	{
		return CustomerDAO.getBookingById(bookingId);
	}
}
