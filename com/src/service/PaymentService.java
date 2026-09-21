package src.service;

import java.util.List;
import java.util.ArrayList;

import src.model.Payment;
import src.model.Booking;
import src.model.User;

import src.enums.PaymentType;
import src.enums.PaymentStatus;

import src.dao.PaymentDAO;
import src.dao.CustomerDAO;
import src.dao.UserDAO;

public class PaymentService
{
	public static boolean payment(int customerId, int bookingId, double amount, PaymentType paymentType)
	{
		String result = processPayment(customerId, bookingId, amount, paymentType);
		return "SUCCESS".equals(result);
	}
	
	public static String processPayment(int customerId, int bookingId, double amount, PaymentType paymentType)
	{
		// 1. Validate Customer
		if(customerId <= 0)
		{
			return "Invalid Customer ID.";
		}
		User customer = UserDAO.findCustomerById(customerId);
		if(customer == null)
		{
			return "Customer #" + customerId + " not found.";
		}

		// 2. Validate Booking
		if(bookingId <= 0)
		{
			return "Invalid Booking ID.";
		}
		Booking booking = CustomerDAO.getBookingById(bookingId);
		if(booking == null)
		{
			return "Booking #" + bookingId + " not found.";
		}

		// 3. Validate Amount
		if(amount <= 0)
		{
			return "Payment amount must be greater than zero.";
		}

		// 4. Validate Payment Type
		if(paymentType == null)
		{
			return "Invalid payment type. Select CASH, UPI, or CARD.";
		}

		// 5. Create Payment record with SUCCESS status
		Payment payment = new Payment(bookingId, customerId, amount, paymentType, PaymentStatus.CONFIRMED);
		boolean ok = PaymentDAO.recordPayment(payment);

		if(ok && payment.getpaymentId() > 0)
		{
			return "SUCCESS";
		}
		return "Database error while processing payment.";
	}

	/**
	 * Calculates cumulative amount already successfully paid for a booking.
	 */
	public static double getAlreadyPaidAmount(int bookingId)
	{
		if(bookingId <= 0) return 0.0;
		return PaymentDAO.getTotalPaidAmountByBookingId(bookingId);
	}

	public static double getRemainingAmount(int bookingId)
	{
		if(bookingId <= 0) return 0.0;

		Booking booking = CustomerDAO.getBookingById(bookingId);
		if(booking == null) return 0.0;

		double total = booking.getamount();
		double paid = getAlreadyPaidAmount(bookingId);
		double remaining = total - paid;

		// Prevent negative precision errors
		return remaining > 0.01 ? remaining : 0.0;
	}

	public static boolean createBookingWithPayment(Booking booking, int roomId, double roomPrice, PaymentType paymentType)
	{
		if(booking == null || roomId <= 0 || roomPrice <= 0 || booking.getuserId() <= 0)
		{
			return false;
		}
		if(paymentType == null)
		{
			return false;
		}

		Payment payment = new Payment();
		payment.setamount(booking.getamount());
		payment.setpaymentType(paymentType);
		payment.setpaymentStatus(PaymentStatus.CONFIRMED);

		return CustomerDAO.createBooking(booking, roomId, roomPrice, payment);
	}

	public static List<Payment> getPaymentHistoryByCustomerId(int customerId)
	{
		if(customerId <= 0)
		{
			return new ArrayList<>();
		}
		return PaymentDAO.getPaymentsByCustomerId(customerId);
	}

	
	public static List<Payment> getPaymentHistoryByCustomerEmail(String email)
	{
		if(email == null || email.trim().isEmpty())
		{
			return new ArrayList<>();
		}
		return PaymentDAO.getPaymentsByCustomerEmail(email.trim());
	}

	
	public static boolean updatePaymentStatus(int paymentId, PaymentStatus status)
	{
		if(paymentId <= 0 || status == null)
		{
			return false;
		}
		return PaymentDAO.updatePaymentStatus(paymentId, status);
	}
}
