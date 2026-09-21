package src.service;

import java.util.List;
import java.util.ArrayList;

import src.model.Hotel;
import src.model.Room;
import src.model.Booking;
import src.dao.CustomerDAO;

import java.time.LocalDate;

public class CustomerService
{
	public static List<Hotel> showHotels(String location)
	{
		if(location == null || location.trim().isEmpty())
		{
			return new ArrayList<>();
		}
		return CustomerDAO.showHotels(location.trim());
	}

	public static List<Hotel> availability(String location)
	{
		if(location == null || location.trim().isEmpty())
		{
			return new ArrayList<>();
		}
		return CustomerDAO.availableHotels(location.trim());
	}

	public static List<Room> getAvailableRooms(
        int hotelId,
        LocalDate checkIn,
        LocalDate checkOut,
        String title,
        String type)
	{
		if(hotelId <= 0 || checkIn == null || checkOut == null || !checkOut.isAfter(checkIn) || title == null || type == null)
		{
			return new ArrayList<>();
		}

		return CustomerDAO.getAvailableRooms(hotelId, checkIn, checkOut, title, type);
	}

	public static boolean createBooking(Booking booking, int roomId, double pricePerNight)
	{
		return createBooking(booking, roomId, pricePerNight, null);
	}

	public static boolean createBooking(Booking booking, int roomId, double pricePerNight, src.model.Payment payment)
	{
		if(booking == null || roomId <= 0 || pricePerNight <= 0 || booking.getuserId() <= 0)
		{
			return false;
		}

		return CustomerDAO.createBooking(booking, roomId, pricePerNight, payment);
	}

	public static List<Booking> getMyBookings(int userId)
	{
		if(userId <= 0)
		{
			return new ArrayList<>();
		}
		return CustomerDAO.getMyBookings(userId);
	}

	public static boolean cancelBooking(int bookingId, int userId)
	{
		if(bookingId <= 0 || userId <= 0)
		{
			return false;
		}
		return CustomerDAO.cancelBooking(bookingId, userId);
	}
}