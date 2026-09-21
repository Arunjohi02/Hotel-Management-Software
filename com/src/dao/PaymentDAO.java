package src.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import src.model.Payment;
import src.model.User;
import src.enums.PaymentType;
import src.enums.PaymentStatus;
import src.util.DBConnection;

public class PaymentDAO
{
	private static Connection con;

	static
	{
		try
		{
			con = DBConnection.getConnection();
		}
		catch(Exception e)
		{
			System.err.println("Database connection error in PaymentDAO: " + e.getMessage());
		}
	}

	public static boolean recordPayment(Payment payment)
	{
		return recordPayment(con, payment);
	}

	public static boolean recordPayment(Connection connection, Payment payment)
	{
		if(payment == null) return false;

		String sql = """
			INSERT INTO payments (booking_id, customer_id, amount, payment_type, payment_status, payment_date)
			VALUES (?, ?, ?, ?, ?, ?)
		""";

		try(PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS))
		{
			ps.setInt(1, payment.getbookingId());
			ps.setInt(2, payment.getcustomerId());
			ps.setDouble(3, payment.getamount());
			ps.setString(4, payment.getpaymentType().name());
			ps.setString(5, payment.getpaymentStatus().name());

			LocalDateTime dt = payment.getpaymentDate() != null ? payment.getpaymentDate() : LocalDateTime.now();
			ps.setTimestamp(6, Timestamp.valueOf(dt));

			int affected = ps.executeUpdate();
			if(affected > 0)
			{
				try(ResultSet rs = ps.getGeneratedKeys())
				{
					if(rs.next())
					{
						payment.setpaymentId(rs.getInt(1));
					}
				}
				return true;
			}
		}
		catch(Exception err)
		{
			System.err.println("Error recording payment: " + err.getMessage());
		}
		return false;
	}

	public static Payment getPaymentById(int paymentId)
	{
		String sql = """
			SELECT p.payment_id, p.booking_id, p.customer_id, p.amount, p.payment_type, p.payment_status, p.payment_date,
			       u.name as customer_name, u.email as customer_email, h.name as hotel_name, b.roomNumber
			FROM payments p
			JOIN users u ON p.customer_id = u.id
			JOIN booking b ON p.booking_id = b.id
			LEFT JOIN booking_items bi ON b.id = bi.booking_id
			LEFT JOIN rooms r ON bi.room_id = r.id
			LEFT JOIN hotel h ON r.hotel_id = h.id
			WHERE p.payment_id = ?
		""";

		try(PreparedStatement ps = con.prepareStatement(sql))
		{
			ps.setInt(1, paymentId);
			try(ResultSet rs = ps.executeQuery())
			{
				if(rs.next())
				{
					return mapRow(rs);
				}
			}
		}
		catch(Exception err)
		{
			System.err.println("Error fetching payment #" + paymentId + ": " + err.getMessage());
		}
		return null;
	}

	public static List<Payment> getPaymentsByBookingId(int bookingId)
	{
		List<Payment> list = new ArrayList<>();
		String sql = """
			SELECT p.payment_id, p.booking_id, p.customer_id, p.amount, p.payment_type, p.payment_status, p.payment_date,
			       u.name as customer_name, u.email as customer_email, h.name as hotel_name, b.roomNumber
			FROM payments p
			JOIN users u ON p.customer_id = u.id
			JOIN booking b ON p.booking_id = b.id
			LEFT JOIN booking_items bi ON b.id = bi.booking_id
			LEFT JOIN rooms r ON bi.room_id = r.id
			LEFT JOIN hotel h ON r.hotel_id = h.id
			WHERE p.booking_id = ?
			ORDER BY p.payment_id ASC
		""";

		try(PreparedStatement ps = con.prepareStatement(sql))
		{
			ps.setInt(1, bookingId);
			try(ResultSet rs = ps.executeQuery())
			{
				while(rs.next())
				{
					list.add(mapRow(rs));
				}
			}
		}
		catch(Exception err)
		{
			System.err.println("Error fetching payments for booking #" + bookingId + ": " + err.getMessage());
		}
		return list;
	}

	public static double getTotalPaidAmountByBookingId(int bookingId)
	{
		String sql = "SELECT COALESCE(SUM(amount), 0.0) FROM payments WHERE booking_id = ? AND payment_status = 'SUCCESS'";
		try(PreparedStatement ps = con.prepareStatement(sql))
		{
			ps.setInt(1, bookingId);
			try(ResultSet rs = ps.executeQuery())
			{
				if(rs.next())
				{
					return rs.getDouble(1);
				}
			}
		}
		catch(Exception err)
		{
			System.err.println("Error calculating total paid amount: " + err.getMessage());
		}
		return 0.0;
	}

	public static List<Payment> getPaymentsByCustomerId(int customerId)
	{
		List<Payment> list = new ArrayList<>();
		String sql = """
			SELECT p.payment_id, p.booking_id, p.customer_id, p.amount, p.payment_type, p.payment_status, p.payment_date,
			       u.name as customer_name, u.email as customer_email, h.name as hotel_name, b.roomNumber
			FROM payments p
			JOIN users u ON p.customer_id = u.id
			JOIN booking b ON p.booking_id = b.id
			LEFT JOIN booking_items bi ON b.id = bi.booking_id
			LEFT JOIN rooms r ON bi.room_id = r.id
			LEFT JOIN hotel h ON r.hotel_id = h.id
			WHERE p.customer_id = ?
			ORDER BY p.payment_id DESC
		""";

		try(PreparedStatement ps = con.prepareStatement(sql))
		{
			ps.setInt(1, customerId);
			try(ResultSet rs = ps.executeQuery())
			{
				while(rs.next())
				{
					list.add(mapRow(rs));
				}
			}
		}
		catch(Exception err)
		{
			System.err.println("Error fetching payments for customer #" + customerId + ": " + err.getMessage());
		}
		return list;
	}

	public static List<Payment> getPaymentsByCustomerEmail(String email)
	{
		if(email == null || email.trim().isEmpty()) return new ArrayList<>();

		User user = UserDAO.findCustomerByEmail(email.trim());
		if(user == null)
		{
			return new ArrayList<>();
		}
		return getPaymentsByCustomerId(user.getid());
	}

	public static boolean updatePaymentStatus(int paymentId, PaymentStatus status)
	{
		String sql = "UPDATE payments SET payment_status = ? WHERE payment_id = ?";
		try(PreparedStatement ps = con.prepareStatement(sql))
		{
			ps.setString(1, status.name());
			ps.setInt(2, paymentId);
			int rows = ps.executeUpdate();
			return rows > 0;
		}
		catch(Exception err)
		{
			System.err.println("Error updating payment status: " + err.getMessage());
			return false;
		}
	}

	private static Payment mapRow(ResultSet rs) throws Exception
	{
		Payment p = new Payment();
		p.setpaymentId(rs.getInt("payment_id"));
		p.setbookingId(rs.getInt("booking_id"));
		p.setcustomerId(rs.getInt("customer_id"));
		p.setamount(rs.getDouble("amount"));

		try
		{
			p.setpaymentType(PaymentType.valueOf(rs.getString("payment_type").toUpperCase()));
		}
		catch(Exception e)
		{
			p.setpaymentType(PaymentType.CASH);
		}

		try
		{
			p.setpaymentStatus(PaymentStatus.valueOf(rs.getString("payment_status").toUpperCase()));
		}
		catch(Exception e)
		{
			p.setpaymentStatus(PaymentStatus.PENDING);
		}

		Timestamp ts = rs.getTimestamp("payment_date");
		if(ts != null)
		{
			p.setpaymentDate(ts.toLocalDateTime());
		}

		p.setcustomerName(rs.getString("customer_name"));
		p.setcustomerEmail(rs.getString("customer_email"));
		p.sethotelName(rs.getString("hotel_name"));
		p.setroomNumber(rs.getInt("roomNumber"));

		return p;
	}
}
