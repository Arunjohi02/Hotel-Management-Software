package src.model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import src.enums.PaymentType;
import src.enums.PaymentStatus;

public class Payment
{
	private int paymentId;
	private int bookingId;
	private int customerId;
	private double amount;
	private PaymentType paymentType;
	private PaymentStatus paymentStatus;
	private LocalDateTime paymentDate;

	// Display metadata (joined from booking/user/hotel)
	private String customerName;
	private String customerEmail;
	private String hotelName;
	private int roomNumber;

	public Payment() {}

	public Payment(int bookingId, int customerId, double amount, PaymentType paymentType, PaymentStatus paymentStatus)
	{
		this.bookingId = bookingId;
		this.customerId = customerId;
		this.amount = amount;
		this.paymentType = paymentType;
		this.paymentStatus = paymentStatus;
		this.paymentDate = LocalDateTime.now();
	}

	public Payment(int paymentId, int bookingId, int customerId, double amount, PaymentType paymentType, PaymentStatus paymentStatus, LocalDateTime paymentDate)
	{
		this.paymentId = paymentId;
		this.bookingId = bookingId;
		this.customerId = customerId;
		this.amount = amount;
		this.paymentType = paymentType;
		this.paymentStatus = paymentStatus;
		this.paymentDate = paymentDate;
	}

	// Getters & Setters (project naming convention)
	public int getpaymentId() { return paymentId; }
	public void setpaymentId(int paymentId) { this.paymentId = paymentId; }

	public int getbookingId() { return bookingId; }
	public void setbookingId(int bookingId) { this.bookingId = bookingId; }

	public int getcustomerId() { return customerId; }
	public void setcustomerId(int customerId) { this.customerId = customerId; }

	public double getamount() { return amount; }
	public void setamount(double amount) { this.amount = amount; }

	public PaymentType getpaymentType() { return paymentType; }
	public void setpaymentType(PaymentType paymentType) { this.paymentType = paymentType; }

	public PaymentStatus getpaymentStatus() { return paymentStatus; }
	public void setpaymentStatus(PaymentStatus paymentStatus) { this.paymentStatus = paymentStatus; }

	public LocalDateTime getpaymentDate() { return paymentDate; }
	public void setpaymentDate(LocalDateTime paymentDate) { this.paymentDate = paymentDate; }

	// Display helpers
	public String getcustomerName() { return customerName; }
	public void setcustomerName(String customerName) { this.customerName = customerName; }

	public String getcustomerEmail() { return customerEmail; }
	public void setcustomerEmail(String customerEmail) { this.customerEmail = customerEmail; }

	public String gethotelName() { return hotelName; }
	public void sethotelName(String hotelName) { this.hotelName = hotelName; }

	public int getroomNumber() { return roomNumber; }
	public void setroomNumber(int roomNumber) { this.roomNumber = roomNumber; }

	// CamelCase aliases for convenience
	public int getPaymentId() { return paymentId; }
	public int getBookingId() { return bookingId; }
	public int getCustomerId() { return customerId; }
	public double getAmount() { return amount; }
	public PaymentType getPaymentType() { return paymentType; }
	public PaymentStatus getPaymentStatus() { return paymentStatus; }
	public LocalDateTime getPaymentDate() { return paymentDate; }

	public String getFormattedDate()
	{
		if(paymentDate == null) return "N/A";
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
		return paymentDate.format(formatter);
	}

	@Override
	public String toString()
	{
		return "Payment #" + paymentId + " [Booking #" + bookingId + ", Customer #" + customerId +
			   ", Amount: Rs. " + amount + ", Type: " + paymentType + ", Status: " + paymentStatus +
			   ", Date: " + getFormattedDate() + "]";
	}
}
