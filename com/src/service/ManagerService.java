package src.service;

import java.util.List;
import java.util.Map;

import src.model.Room;
import src.model.Booking;
import src.model.User;
import src.dao.HotelDAO;
import src.dao.CustomerDAO;
import src.dao.UserDAO;

public class ManagerService
{
	// 1. Room Management
	public static List<Room> getAllRooms(int hotelId)
	{
		return HotelDAO.allRooms(hotelId);
	}

	public static boolean updateRoomStatus(int roomId, String newStatus)
	{
		if(roomId <= 0 || newStatus == null || newStatus.trim().isEmpty())
		{
			return false;
		}
		return HotelDAO.updateRoomStatus(roomId, newStatus.trim().toUpperCase());
	}

	// 2. Booking Management
	public static List<Booking> getAllBookings()
	{
		return CustomerDAO.getAllBookings();
	}

	public static Booking getBookingDetails(int bookingId)
	{
		if(bookingId <= 0) return null;
		return CustomerDAO.getBookingById(bookingId);
	}

	public static boolean confirmBooking(int bookingId)
	{
		if(bookingId <= 0) return false;
		return CustomerDAO.confirmBooking(bookingId);
	}

	public static String cancelBooking(int bookingId)
	{
		return ReceptionistService.cancelBooking(bookingId);
	}

	// 3 & 4. Check-in & Check-out
	public static String checkIn(int bookingId)
	{
		return ReceptionistService.checkIn(bookingId);
	}

	public static String checkOut(int bookingId)
	{
		return ReceptionistService.checkOut(bookingId);
	}

	// 5. Customer Details
	public static List<User> getAllCustomers()
	{
		return UserDAO.allCustomers();
	}

	public static User findCustomer(String query, int type)
	{
		return ReceptionistService.findCustomer(query, type);
	}

	// 6. Reports
	public static Map<String, Integer> getBookingReportCounts()
	{
		return CustomerDAO.getBookingStatusCounts();
	}

	public static Map<String, Integer> getRoomReportCounts()
	{
		return CustomerDAO.getRoomStatusCounts();
	}

	public static double getTotalRevenue()
	{
		return CustomerDAO.getTotalRevenue();
	}

	public static Map<String, Double> getRevenueByHotel()
	{
		return CustomerDAO.getRevenueByHotel();
	}
}
