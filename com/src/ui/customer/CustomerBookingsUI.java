package src.ui.customer;

import java.util.Scanner;
import java.util.List;

import src.model.Booking;
import src.model.User;
import src.service.CustomerService;
import src.util.InputUtil;

public class CustomerBookingsUI
{
    private static Scanner io = InputUtil.getScanner();

    public static void viewMyBookings(User currentUser)
    {
        if (currentUser == null)
        {
            System.out.println("\n❌ Please login first.");
            return;
        }

        List<Booking> bookings = CustomerService.getMyBookings(currentUser.getid());

        if (bookings == null || bookings.isEmpty())
        {
            System.out.println("\nNo bookings found for your account.");
            return;
        }

        System.out.println("\n=========================== MY BOOKINGS ===========================\n");
        System.out.printf(
            " %-10s | %-18s | %-8s | %-10s | %-8s | %-12s | %-12s | %-12s | %-10s%n",
            "Booking ID", "Hotel", "Room No", "Type", "AC", "Check-In", "Check-Out", "Amount (Rs)", "Status"
        );
        
        for (Booking b : bookings)
        {
            System.out.printf(
                " %-10d | %-18s | %-8d | %-10s | %-8s | %-12s | %-12s | %-12.2f | %-10s%n",
                b.getid(),
                b.gethotelName(),
                b.getroomNumber(),
                b.getroomTitle(),
                b.getacType(),
                b.getcheckIn(),
                b.getcheckOut(),
                b.getamount(),
                b.getstatus()
            );
        }
        System.out.println();
    }

    public static void cancelBooking(User currentUser)
    {
        if (currentUser == null)
        {
            System.out.println("\n❌ Please login first.");
            return;
        }

        List<Booking> bookings = CustomerService.getMyBookings(currentUser.getid());

        if (bookings == null || bookings.isEmpty())
        {
            System.out.println("\nYou have no bookings to cancel.");
            return;
        }

        System.out.println("\n=========================== YOUR ACTIVE BOOKINGS ===========================\n");
        int activeCount = 0;
        System.out.printf(
            " %-10s | %-18s | %-8s | %-12s | %-12s | %-12s | %-10s%n",
            "Booking ID", "Hotel", "Room No", "Check-In", "Check-Out", "Amount (Rs)", "Status"
        );
        

        for (Booking b : bookings)
        {
            if (!"CANCELLED".equalsIgnoreCase(b.getstatus()))
            {
                activeCount++;
                System.out.printf(
                    " %-10d | %-18s | %-8d | %-12s | %-12s | %-12.2f | %-10s%n",
                    b.getid(),
                    b.gethotelName(),
                    b.getroomNumber(),
                    b.getcheckIn(),
                    b.getcheckOut(),
                    b.getamount(),
                    b.getstatus()
                );
            }
        }

        if (activeCount == 0)
        {
            System.out.println("No active bookings available for cancellation.");
            return;
        }

        System.out.print("\nEnter Booking ID to Cancel (or 0 to return) : ");
        int bookingId = 0;
        try
        {
            bookingId = io.nextInt();
            io.nextLine();
        }
        catch (Exception e)
        {
            System.out.println("Invalid input. Enter numeric Booking ID.");
            io.nextLine();
            return;
        }

        if (bookingId == 0)
        {
            System.out.println("Operation cancelled.");
            return;
        }

        boolean found = false;
        for (Booking b : bookings)
        {
            if (b.getid() == bookingId)
            {
                found = true;
                if ("CANCELLED".equalsIgnoreCase(b.getstatus()))
                {
                    System.out.println("\n❌ This booking is already cancelled.");
                    return;
                }
                break;
            }
        }

        if (!found)
        {
            System.out.println("\n❌ Booking ID not found or does not belong to your account.");
            return;
        }

        System.out.print("Are you sure you want to cancel booking #" + bookingId + "? (1. Yes / 2. No) : ");
        byte choice = 0;
        try
        {
            choice = io.nextByte();
            io.nextLine();
        }
        catch (Exception e)
        {
            io.nextLine();
        }

        if (choice == 1)
        {
            boolean cancelled = CustomerService.cancelBooking(bookingId, currentUser.getid());
            if (cancelled)
            {
                System.out.println("\n✅ Booking #" + bookingId + " has been cancelled successfully.");
            }
            else
            {
                System.out.println("\n❌ Failed to cancel booking. Please try again.");
            }
        }
        else
        {
            System.out.println("\nCancellation aborted.");
        }
    }
}
