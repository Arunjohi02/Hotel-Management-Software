package src.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import src.exception.DataNotFountException;
import src.model.Booking;
import src.model.Hotel;
import src.model.Payment;
import src.model.Room;
import src.util.DBConnection;

public class CustomerDAO
{
	static String Query;
	static Connection con;

	static 
	{
		try
		{
			con = DBConnection.getConnection();
		}
		catch(Exception err)
		{
			System.err.print(err.getMessage());
		}
	}

	public static List<Hotel> showHotels(String location)
	{
		Query = """
			Select id,name,phone,email,description,checkin,checkout,totalcount from Hotel where location =? ;
		""";

		List<Hotel> hotels = new ArrayList<>();

		try(PreparedStatement ps = con.prepareStatement(Query))
		{
			ps.setString(1, location);

			try(ResultSet res = ps.executeQuery())
			{
				while(res.next())
				{
					Hotel hotel = new Hotel();

					hotel.setid(res.getInt(1));
					hotel.setname(res.getString(2));
					hotel.setphone(res.getString(3));
					hotel.setemail(res.getString(4));
					hotel.setdescription(res.getString(5));
					hotel.setcheckInTime(res.getTime(6).toLocalTime());
					hotel.setcheckoutTime(res.getTime(7).toLocalTime());
					hotel.settotalRooms(res.getInt(8));
					hotels.add(hotel);
				}
			}
		}
		catch(Exception e)
		{
			try
			{
				throw new DataNotFountException("Data Not Found");
			}
			catch(DataNotFountException err)
			{
				System.out.println(err.getMessage());
			}
		}
		return hotels;
	}

	public static List<Hotel> availableHotels(String location)
	{
		Query = """
			Select id,name,phone,email,description,checkin,checkout from Hotel where location = ? and totalcount >=1 ;
		""";

		List<Hotel> hotels = new ArrayList<>();

		try(PreparedStatement ps = con.prepareStatement(Query))
		{
			ps.setString(1, location);
			try(ResultSet result = ps.executeQuery())
			{
				while(result.next())
				{
					Hotel hotel = new Hotel();

					hotel.setid(result.getInt(1));
					hotel.setname(result.getString(2));
					hotel.setphone(result.getString(3));
					hotel.setemail(result.getString(4));
					hotel.setdescription(result.getString(5));
					hotel.setcheckInTime(result.getTime(6).toLocalTime());
					hotel.setcheckoutTime(result.getTime(7).toLocalTime());

					hotels.add(hotel);
				}
			}
		}
		catch(Exception err)
		{
			System.out.println(err.getMessage());
		}
		return hotels;
	}

	public static List<Room> getAvailableRooms(int hotelId, LocalDate checkIn, LocalDate checkOut, String title, String acType)
	{
		List<Room> availableRooms = new ArrayList<>();

		String query = """
			SELECT r.id, r.room_number, r.floor_no, r.status, r.hotel_id, r.title, r.ACType, r.price, r.capacity
			FROM rooms r
			WHERE r.hotel_id = ?
			  AND r.title = ?
			  AND r.ACType = ?
			  AND r.status = 'AVAILABLE'
			  AND r.id NOT IN (
				  SELECT bi.room_id
				  FROM booking_items bi
				  JOIN booking b ON bi.booking_id = b.id
				  WHERE b.status != 'CANCELLED'
				    AND b.chech_in < ?
				    AND b.check_out > ?
			  )
		""";

		try(PreparedStatement ps = con.prepareStatement(query))
		{
			ps.setInt(1, hotelId);
			ps.setString(2, title);
			ps.setString(3, acType);
			ps.setDate(4, java.sql.Date.valueOf(checkOut));
			ps.setDate(5, java.sql.Date.valueOf(checkIn));

			try(ResultSet rs = ps.executeQuery())
			{
				while(rs.next())
				{
					Room room = new Room();
					room.setid(rs.getInt("id"));
					room.setroomNumber(rs.getInt("room_number"));
					room.setfloorNumber(rs.getInt("floor_no"));
					room.setstatus(rs.getString("status"));
					room.sethotelId(rs.getInt("hotel_id"));
					room.settitle(rs.getString("title"));
					room.setactype(rs.getString("ACType"));
					room.setbasePrice(rs.getDouble("price"));
					room.setcapacity(rs.getInt("capacity"));

					availableRooms.add(room);
				}
			}
		}
		catch(Exception err)
		{
			System.err.println("Error fetching available rooms: " + err.getMessage());
		}

		return availableRooms;
	}

	public static boolean createBooking(Booking booking, int roomId, double pricePerNight)
	{
		return createBooking(booking, roomId, pricePerNight, null);
	}

	public static boolean createBooking(Booking booking, int roomId, double pricePerNight, Payment payment)
	{
		String insertBooking = "INSERT INTO booking (chech_in, check_out, status, amout, user_id, roomNumber) VALUES (?, ?, ?, ?, ?, ?)";
		String insertItem = "INSERT INTO booking_items (price_per_ngt, booking_id, room_id) VALUES (?, ?, ?)";

		try
		{
			con.setAutoCommit(false);

			int bookingId = 0;
			try(PreparedStatement psBooking = con.prepareStatement(insertBooking, Statement.RETURN_GENERATED_KEYS))
			{
				psBooking.setDate(1, java.sql.Date.valueOf(booking.getcheckIn()));
				psBooking.setDate(2, java.sql.Date.valueOf(booking.getcheckOut()));
				psBooking.setString(3, booking.getstatus());
				psBooking.setDouble(4, booking.getamount());
				psBooking.setInt(5, booking.getuserId());
				psBooking.setInt(6, booking.getroomNumber());

				int affected = psBooking.executeUpdate();
				if(affected == 0)
				{
					con.rollback();
					return false;
				}

				try(ResultSet generatedKeys = psBooking.getGeneratedKeys())
				{
					if(generatedKeys.next())
					{
						bookingId = generatedKeys.getInt(1);
						booking.setid(bookingId);
					}
					else
					{
						con.rollback();
						return false;
					}
				}
			}

			try(PreparedStatement psItem = con.prepareStatement(insertItem))
			{
				psItem.setDouble(1, pricePerNight);
				psItem.setInt(2, bookingId);
				psItem.setInt(3, roomId);

				psItem.executeUpdate();
			}

			if(payment != null)
			{
				payment.setbookingId(bookingId);
				payment.setcustomerId(booking.getuserId());
				boolean pSuccess = PaymentDAO.recordPayment(con, payment);
				if(!pSuccess)
				{
					con.rollback();
					return false;
				}
			}

			con.commit();
			return true;
		}
		catch(Exception err)
		{
			try
			{
				con.rollback();
			}
			catch(SQLException e)
			{
				System.err.println("Rollback failed: " + e.getMessage());
			}
			System.err.println("Booking creation error: " + err.getMessage());
			return false;
		}
		finally
		{
			try
			{
				con.setAutoCommit(true);
			}
			catch(SQLException e)
			{
				System.err.println("Failed to reset auto-commit: " + e.getMessage());
			}
		}
	}

	public static List<Booking> getMyBookings(int userId)
	{
		List<Booking> bookings = new ArrayList<>();

		String query = """
			SELECT b.id, b.chech_in, b.check_out, b.status, b.amout, b.user_id, b.roomNumber,
			       r.id as room_id, r.title, r.ACType, h.name as hotel_name
			FROM booking b
			JOIN booking_items bi ON b.id = bi.booking_id
			JOIN rooms r ON bi.room_id = r.id
			JOIN hotel h ON r.hotel_id = h.id
			WHERE b.user_id = ?
			ORDER BY b.id DESC
		""";

		try(PreparedStatement ps = con.prepareStatement(query))
		{
			ps.setInt(1, userId);
			try(ResultSet rs = ps.executeQuery())
			{
				while(rs.next())
				{
					Booking booking = new Booking();
					booking.setid(rs.getInt("id"));
					booking.setcheckIn(rs.getDate("chech_in").toLocalDate());
					booking.setcheckOut(rs.getDate("check_out").toLocalDate());
					booking.setstatus(rs.getString("status"));
					booking.setamount(rs.getDouble("amout"));
					booking.setuserId(rs.getInt("user_id"));
					booking.setroomNumber(rs.getInt("roomNumber"));
					booking.setroomId(rs.getInt("room_id"));
					booking.setroomTitle(rs.getString("title"));
					booking.setacType(rs.getString("ACType"));
					booking.sethotelName(rs.getString("hotel_name"));

					bookings.add(booking);
				}
			}
		}
		catch(Exception err)
		{
			System.err.println("Error fetching customer bookings: " + err.getMessage());
		}

		return bookings;
	}

	public static boolean cancelBooking(int bookingId, int userId)
	{
		String updateBooking = "UPDATE booking SET status = 'CANCELLED' WHERE id = ? AND user_id = ? AND status NOT IN ('CANCELLED', 'CHECKED_IN', 'CHECKED_OUT')";
		String updateRoom = "UPDATE rooms SET status = 'AVAILABLE' WHERE id = (SELECT room_id FROM booking_items WHERE booking_id = ?)";

		try
		{
			con.setAutoCommit(false);

			try(PreparedStatement ps1 = con.prepareStatement(updateBooking))
			{
				ps1.setInt(1, bookingId);
				ps1.setInt(2, userId);
				int rows = ps1.executeUpdate();
				if(rows == 0)
				{
					con.rollback();
					return false;
				}
			}

			try(PreparedStatement ps2 = con.prepareStatement(updateRoom))
			{
				ps2.setInt(1, bookingId);
				ps2.executeUpdate();
			}

			con.commit();
			return true;
		}
		catch(Exception err)
		{
			try { con.rollback(); } catch(Exception ignored) {}
			System.err.println("Error cancelling booking: " + err.getMessage());
			return false;
		}
		finally
		{
			try { con.setAutoCommit(true); } catch(Exception ignored) {}
		}
	}

	public static Booking getBookingById(int bookingId)
	{
		String query = """
			SELECT b.id, b.chech_in, b.check_out, b.status, b.amout, b.user_id, b.roomNumber,
			       r.id as room_id, r.title, r.ACType, bi.price_per_ngt, h.name as hotel_name,
			       u.name as customer_name, u.email as customer_email, u.phone as customer_phone
			FROM booking b
			JOIN booking_items bi ON b.id = bi.booking_id
			JOIN rooms r ON bi.room_id = r.id
			JOIN hotel h ON r.hotel_id = h.id
			JOIN users u ON b.user_id = u.id
			WHERE b.id = ?
		""";

		try(PreparedStatement ps = con.prepareStatement(query))
		{
			ps.setInt(1, bookingId);
			try(ResultSet rs = ps.executeQuery())
			{
				if(rs.next())
				{
					Booking booking = new Booking();
					booking.setid(rs.getInt("id"));
					booking.setcheckIn(rs.getDate("chech_in").toLocalDate());
					booking.setcheckOut(rs.getDate("check_out").toLocalDate());
					booking.setstatus(rs.getString("status"));
					booking.setamount(rs.getDouble("amout"));
					booking.setuserId(rs.getInt("user_id"));
					booking.setroomNumber(rs.getInt("roomNumber"));
					booking.setroomId(rs.getInt("room_id"));
					booking.setroomTitle(rs.getString("title"));
					booking.setacType(rs.getString("ACType"));
					booking.setpricePerNight(rs.getDouble("price_per_ngt"));
					booking.sethotelName(rs.getString("hotel_name"));
					booking.setcustomerName(rs.getString("customer_name"));
					booking.setcustomerEmail(rs.getString("customer_email"));
					booking.setcustomerPhone(rs.getString("customer_phone"));
					return booking;
				}
			}
		}
		catch(Exception err)
		{
			System.err.println("Error fetching booking by ID: " + err.getMessage());
		}
		return null;
	}

	public static List<Booking> getAllBookings()
	{
		List<Booking> bookings = new ArrayList<>();
		String query = """
			SELECT b.id, b.chech_in, b.check_out, b.status, b.amout, b.user_id, b.roomNumber,
			       r.id as room_id, r.title, r.ACType, bi.price_per_ngt, h.name as hotel_name,
			       u.name as customer_name, u.email as customer_email, u.phone as customer_phone
			FROM booking b
			JOIN booking_items bi ON b.id = bi.booking_id
			JOIN rooms r ON bi.room_id = r.id
			JOIN hotel h ON r.hotel_id = h.id
			JOIN users u ON b.user_id = u.id
			ORDER BY b.id DESC
		""";

		try(Statement stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery(query))
		{
			while(rs.next())
			{
				Booking booking = new Booking();
				booking.setid(rs.getInt("id"));
				booking.setcheckIn(rs.getDate("chech_in").toLocalDate());
				booking.setcheckOut(rs.getDate("check_out").toLocalDate());
				booking.setstatus(rs.getString("status"));
				booking.setamount(rs.getDouble("amout"));
				booking.setuserId(rs.getInt("user_id"));
				booking.setroomNumber(rs.getInt("roomNumber"));
				booking.setroomId(rs.getInt("room_id"));
				booking.setroomTitle(rs.getString("title"));
				booking.setacType(rs.getString("ACType"));
				booking.setpricePerNight(rs.getDouble("price_per_ngt"));
				booking.sethotelName(rs.getString("hotel_name"));
				booking.setcustomerName(rs.getString("customer_name"));
				booking.setcustomerEmail(rs.getString("customer_email"));
				booking.setcustomerPhone(rs.getString("customer_phone"));

				bookings.add(booking);
			}
		}
		catch(Exception err)
		{
			System.err.println("Error listing all bookings: " + err.getMessage());
		}
		return bookings;
	}

	public static List<Booking> getTodayBookings()
	{
		List<Booking> bookings = new ArrayList<>();
		String query = """
			SELECT b.id, b.chech_in, b.check_out, b.status, b.amout, b.user_id, b.roomNumber,
			       r.id as room_id, r.title, r.ACType, bi.price_per_ngt, h.name as hotel_name,
			       u.name as customer_name, u.email as customer_email, u.phone as customer_phone
			FROM booking b
			JOIN booking_items bi ON b.id = bi.booking_id
			JOIN rooms r ON bi.room_id = r.id
			JOIN hotel h ON r.hotel_id = h.id
			JOIN users u ON b.user_id = u.id
			WHERE CURDATE() BETWEEN b.chech_in AND b.check_out
			   OR b.chech_in = CURDATE()
			ORDER BY b.id DESC
		""";

		try(Statement stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery(query))
		{
			while(rs.next())
			{
				Booking booking = new Booking();
				booking.setid(rs.getInt("id"));
				booking.setcheckIn(rs.getDate("chech_in").toLocalDate());
				booking.setcheckOut(rs.getDate("check_out").toLocalDate());
				booking.setstatus(rs.getString("status"));
				booking.setamount(rs.getDouble("amout"));
				booking.setuserId(rs.getInt("user_id"));
				booking.setroomNumber(rs.getInt("roomNumber"));
				booking.setroomId(rs.getInt("room_id"));
				booking.setroomTitle(rs.getString("title"));
				booking.setacType(rs.getString("ACType"));
				booking.setpricePerNight(rs.getDouble("price_per_ngt"));
				booking.sethotelName(rs.getString("hotel_name"));
				booking.setcustomerName(rs.getString("customer_name"));
				booking.setcustomerEmail(rs.getString("customer_email"));
				booking.setcustomerPhone(rs.getString("customer_phone"));

				bookings.add(booking);
			}
		}
		catch(Exception err)
		{
			System.err.println("Error listing today's bookings: " + err.getMessage());
		}
		return bookings;
	}

	public static boolean checkInCustomer(int bookingId)
	{
		String updateBooking = "UPDATE booking SET status = 'CHECKED_IN' WHERE id = ? AND status = 'CONFIRMED'";
		String updateRoom = "UPDATE rooms SET status = 'OCCUPIED' WHERE id = (SELECT room_id FROM booking_items WHERE booking_id = ?)";

		try
		{
			con.setAutoCommit(false);

			try(PreparedStatement ps1 = con.prepareStatement(updateBooking))
			{
				ps1.setInt(1, bookingId);
				int rows = ps1.executeUpdate();
				if(rows == 0)
				{
					con.rollback();
					return false;
				}
			}

			try(PreparedStatement ps2 = con.prepareStatement(updateRoom))
			{
				ps2.setInt(1, bookingId);
				ps2.executeUpdate();
			}

			con.commit();
			return true;
		}
		catch(Exception err)
		{
			try { con.rollback(); } catch(Exception ignored) {}
			System.err.println("Check-in error: " + err.getMessage());
			return false;
		}
		finally
		{
			try { con.setAutoCommit(true); } catch(Exception ignored) {}
		}
	}

	public static boolean checkOutCustomer(int bookingId)
	{
		String updateBooking = "UPDATE booking SET status = 'CHECKED_OUT' WHERE id = ? AND status = 'CHECKED_IN'";
		String updateRoom = "UPDATE rooms SET status = 'DIRTY' WHERE id = (SELECT room_id FROM booking_items WHERE booking_id = ?)";

		try
		{
			con.setAutoCommit(false);

			try(PreparedStatement ps1 = con.prepareStatement(updateBooking))
			{
				ps1.setInt(1, bookingId);
				int rows = ps1.executeUpdate();
				if(rows == 0)
				{
					con.rollback();
					return false;
				}
			}

			try(PreparedStatement ps2 = con.prepareStatement(updateRoom))
			{
				ps2.setInt(1, bookingId);
				ps2.executeUpdate();
			}

			con.commit();
			return true;
		}
		catch(Exception err)
		{
			try { con.rollback(); } catch(Exception ignored) {}
			System.err.println("Check-out error: " + err.getMessage());
			return false;
		}
		finally
		{
			try { con.setAutoCommit(true); } catch(Exception ignored) {}
		}
	}

	public static boolean confirmBooking(int bookingId)
	{
		String sql = "UPDATE booking SET status = 'CONFIRMED' WHERE id = ?";
		try(PreparedStatement ps = con.prepareStatement(sql))
		{
			ps.setInt(1, bookingId);
			int rows = ps.executeUpdate();
			return rows > 0;
		}
		catch(Exception err)
		{
			System.err.println("Error confirming booking: " + err.getMessage());
			return false;
		}
	}

	public static boolean cancelBookingByStaff(int bookingId)
	{
		String updateBooking = "UPDATE booking SET status = 'CANCELLED' WHERE id = ? AND status NOT IN ('CANCELLED', 'CHECKED_OUT')";
		String updateRoom = "UPDATE rooms SET status = 'AVAILABLE' WHERE id = (SELECT room_id FROM booking_items WHERE booking_id = ?)";

		try
		{
			con.setAutoCommit(false);

			try(PreparedStatement ps1 = con.prepareStatement(updateBooking))
			{
				ps1.setInt(1, bookingId);
				int rows = ps1.executeUpdate();
				if(rows == 0)
				{
					con.rollback();
					return false;
				}
			}

			try(PreparedStatement ps2 = con.prepareStatement(updateRoom))
			{
				ps2.setInt(1, bookingId);
				ps2.executeUpdate();
			}

			con.commit();
			return true;
		}
		catch(Exception err)
		{
			try { con.rollback(); } catch(Exception ignored) {}
			System.err.println("Error cancelling booking by staff: " + err.getMessage());
			return false;
		}
		finally
		{
			try { con.setAutoCommit(true); } catch(Exception ignored) {}
		}
	}

	public static java.util.Map<String, Integer> getBookingStatusCounts()
	{
		java.util.Map<String, Integer> counts = new java.util.LinkedHashMap<>();
		counts.put("TOTAL", 0);
		counts.put("CONFIRMED", 0);
		counts.put("CHECKED_IN", 0);
		counts.put("CHECKED_OUT", 0);
		counts.put("CANCELLED", 0);

		String sql = "SELECT status, COUNT(*) as count FROM booking GROUP BY status";

		try(Statement stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery(sql))
		{
			int total = 0;
			while(rs.next())
			{
				String st = rs.getString("status");
				int count = rs.getInt("count");
				counts.put(st.toUpperCase(), count);
				total += count;
			}
			counts.put("TOTAL", total);
		}
		catch(Exception err)
		{
			System.err.println("Error fetching booking counts: " + err.getMessage());
		}
		return counts;
	}

	public static java.util.Map<String, Integer> getRoomStatusCounts()
	{
		java.util.Map<String, Integer> counts = new java.util.LinkedHashMap<>();
		counts.put("TOTAL", 0);
		counts.put("AVAILABLE", 0);
		counts.put("OCCUPIED", 0);
		counts.put("DIRTY", 0);
		counts.put("MAINTENANCE", 0);

		String sql = "SELECT status, COUNT(*) as count FROM rooms GROUP BY status";

		try(Statement stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery(sql))
		{
			int total = 0;
			while(rs.next())
			{
				String st = rs.getString("status");
				int count = rs.getInt("count");
				counts.put(st.toUpperCase(), count);
				total += count;
			}
			counts.put("TOTAL", total);
		}
		catch(Exception err)
		{
			System.err.println("Error fetching room counts: " + err.getMessage());
		}
		return counts;
	}

	public static double getTotalRevenue()
	{
		String sql = "SELECT SUM(amout) as total FROM booking WHERE status IN ('CONFIRMED', 'CHECKED_IN', 'CHECKED_OUT')";
		try(Statement stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery(sql))
		{
			if(rs.next())
			{
				return rs.getDouble("total");
			}
		}
		catch(Exception err)
		{
			System.err.println("Error calculating total revenue: " + err.getMessage());
		}
		return 0.0;
	}

	public static java.util.Map<String, Double> getRevenueByHotel()
	{
		java.util.Map<String, Double> revenueMap = new java.util.LinkedHashMap<>();
		String sql = """
			SELECT h.name, SUM(b.amout) as revenue
			FROM booking b
			JOIN booking_items bi ON b.id = bi.booking_id
			JOIN rooms r ON bi.room_id = r.id
			JOIN hotel h ON r.hotel_id = h.id
			WHERE b.status IN ('CONFIRMED', 'CHECKED_IN', 'CHECKED_OUT')
			GROUP BY h.name
			ORDER BY revenue DESC
		""";

		try(Statement stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery(sql))
		{
			while(rs.next())
			{
				revenueMap.put(rs.getString("name"), rs.getDouble("revenue"));
			}
		}
		catch(Exception err)
		{
			System.err.println("Error calculating hotel revenue: " + err.getMessage());
		}
		return revenueMap;
	}
}