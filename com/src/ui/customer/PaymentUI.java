package src.ui.customer;

import java.util.Scanner;
import java.util.List;

import src.model.User;
import src.model.Payment;
import src.enums.PaymentType;
import src.service.PaymentService;

public class PaymentUI
{

	public static PaymentType selectPaymentType(Scanner io)
	{
		System.out.println("\nSelect Payment Type:");
		System.out.println("1. CASH");
		System.out.println("2. UPI");
		System.out.println("3. CARD");
		System.out.print("Enter Choice (1 - 3): ");

		try
		{
			String input = io.nextLine().trim();
			if(input.isEmpty()) return null;
			int choice = Integer.parseInt(input);
			switch(choice)
			{
				case 1: return PaymentType.CASH;
				case 2: return PaymentType.UPI;
				case 3: return PaymentType.CARD;
				default:
					System.out.println("❌ Invalid choice. Please enter 1, 2, or 3.");
					return null;
			}
		}
		catch(Exception e)
		{
			System.out.println("❌ Numeric choice required.");
			return null;
		}
	}

	public static void viewPaymentHistory(User customer)
	{
		if(customer == null)
		{
			System.out.println("\n❌ Please login to view payment history.");
			return;
		}

		System.out.println("\n================== MY PAYMENT HISTORY ==================");
		System.out.println("Customer: " + customer.getname() + " (ID: " + customer.getid() + ")\n");

		List<Payment> payments = PaymentService.getPaymentHistoryByCustomerId(customer.getid());
		displayPaymentTable(payments);
	}

	
	public static void searchPaymentHistory(Scanner io)
	{
		System.out.println("\n================ SEARCH PAYMENT HISTORY ================");
		System.out.println("1. Search by Customer ID");
		System.out.println("2. Search by Customer Email");
		System.out.print("Enter Choice (1 - 2): ");

		int choice = 0;
		try
		{
			choice = Integer.parseInt(io.nextLine().trim());
		}
		catch(Exception e)
		{
			System.out.println("❌ Numeric choice required.");
			return;
		}

		List<Payment> payments = null;
		if(choice == 1)
		{
			System.out.print("Enter Customer ID: ");
			try
			{
				int custId = Integer.parseInt(io.nextLine().trim());
				payments = PaymentService.getPaymentHistoryByCustomerId(custId);
			}
			catch(Exception e)
			{
				System.out.println("❌ Numeric ID required.");
				return;
			}
		}
		else if(choice == 2)
		{
			System.out.print("Enter Customer Email: ");
			String email = io.nextLine().trim();
			payments = PaymentService.getPaymentHistoryByCustomerEmail(email);
		}
		else
		{
			System.out.println("❌ Invalid option.");
			return;
		}

		displayPaymentTable(payments);
	}

	private static void displayPaymentTable(List<Payment> payments)
	{
		if(payments == null || payments.isEmpty())
		{
			System.out.println("No payment transactions found.\n");
			return;
		}

		System.out.printf(" %-11s | %-11s | %-18s | %-8s | %-12s | %-12s | %-10s | %-18s%n",
			"Payment ID", "Booking ID", "Hotel", "Room No", "Amount (Rs)", "Type", "Status", "Payment Date");

		for(Payment p : payments)
		{
			System.out.printf(" %-11d | %-11d | %-18s | %-8d | %-12.2f | %-12s | %-10s | %-18s%n",
				p.getpaymentId(),
				p.getbookingId(),
				p.gethotelName() != null ? p.gethotelName() : "-",
				p.getroomNumber(),
				p.getamount(),
				p.getpaymentType(),
				p.getpaymentStatus(),
				p.getFormattedDate());
		}
		System.out.println();
	}
}
