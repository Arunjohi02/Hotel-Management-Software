package src.model;

import java.time.LocalDate;

public class Booking
{
	private int id;
	private LocalDate checkIn;
	private LocalDate checkOut;
	private String status;
	private double amount;
	private int userId;
	private int roomNumber;

	// Display fields for My Bookings, Receptionist and Manager
	private int roomId;
	private String hotelName;
	private String roomTitle;
	private String acType;
	private String customerName;
	private String customerEmail;
	private String customerPhone;
	private double pricePerNight;

	public Booking() {}

	public Booking(LocalDate checkIn, LocalDate checkOut, String status, double amount, int userId, int roomNumber)
	{
		this.checkIn = checkIn;
		this.checkOut = checkOut;
		this.status = status;
		this.amount = amount;
		this.userId = userId;
		this.roomNumber = roomNumber;
	}

	public Booking(int id, LocalDate checkIn, LocalDate checkOut, String status, double amount, int userId, int roomNumber)
	{
		this.id = id;
		this.checkIn = checkIn;
		this.checkOut = checkOut;
		this.status = status;
		this.amount = amount;
		this.userId = userId;
		this.roomNumber = roomNumber;
	}

	public int getid() { return id; }
	public void setid(int id) { this.id = id; }

	public LocalDate getcheckIn() { return checkIn; }
	public void setcheckIn(LocalDate checkIn) { this.checkIn = checkIn; }

	public LocalDate getcheckOut() { return checkOut; }
	public void setcheckOut(LocalDate checkOut) { this.checkOut = checkOut; }

	public String getstatus() { return status; }
	public void setstatus(String status) { this.status = status; }

	public double getamount() { return amount; }
	public void setamount(double amount) { this.amount = amount; }

	public int getuserId() { return userId; }
	public void setuserId(int userId) { this.userId = userId; }

	public int getroomNumber() { return roomNumber; }
	public void setroomNumber(int roomNumber) { this.roomNumber = roomNumber; }

	public int getroomId() { return roomId; }
	public void setroomId(int roomId) { this.roomId = roomId; }

	public String gethotelName() { return hotelName; }
	public void sethotelName(String hotelName) { this.hotelName = hotelName; }

	public String getroomTitle() { return roomTitle; }
	public void setroomTitle(String roomTitle) { this.roomTitle = roomTitle; }

	public String getacType() { return acType; }
	public void setacType(String acType) { this.acType = acType; }

	public String getcustomerName() { return customerName; }
	public void setcustomerName(String customerName) { this.customerName = customerName; }

	public String getcustomerEmail() { return customerEmail; }
	public void setcustomerEmail(String customerEmail) { this.customerEmail = customerEmail; }

	public String getcustomerPhone() { return customerPhone; }
	public void setcustomerPhone(String customerPhone) { this.customerPhone = customerPhone; }

	public double getpricePerNight() { return pricePerNight; }
	public void setpricePerNight(double pricePerNight) { this.pricePerNight = pricePerNight; }

	@Override
	public String toString()
	{
		return id + " " + checkIn + " " + checkOut + " " + status + " " + amount + " " + userId + " " + roomNumber;
	}
}
