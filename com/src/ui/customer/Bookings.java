package src.ui.customer;

import java.util.Scanner;
import java.util.List;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoUnit;

import src.model.Hotel;
import src.model.Room;
import src.model.Booking;
import src.model.User;
import src.service.CustomerService;
import src.enums.RoomTypes;
import src.enums.AcType ; 
import src.enums.PaymentType ;
import src.enums.PaymentStatus ;

import src.service.PaymentService;

import src.util.InputUtil;

public class Bookings
{
    public static void booking(User currentUser)
    {
        Scanner io = InputUtil.getScanner();
        if (currentUser == null)
        {
            System.out.println("\n❌ Please login first to book a room.");
            return;
        }

        // 1. LOCATION
        System.out.print("Enter your Location : ");
        String location = io.nextLine();

        List<Hotel> hotels = CustomerService.availability(location);

        if (hotels == null || hotels.size() == 0)
        {
            System.out.println("\nNo hotels available in this location.");
            return;
        }

        // 2. DISPLAY HOTELS
        System.out.println("\n================ AVAILABLE HOTELS ================\n");

        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("hh:mm a");

        System.out.printf(
            " %-3s | %-20s | %-15s | %-25s | %-30s | %-12s | %-12s%n%n",
            "ID", "Name", "Phone", "Email", "Description", "Check-In", "Check-Out"
        );

        for (Hotel hotel : hotels)
        {
            System.out.printf(
                " %-3s | %-20s | %-15s | %-25s | %-30s | %-12s | %-12s%n",
                hotel.getid(),
                hotel.getname(),
                hotel.getphone(),
                hotel.getemail(),
                hotel.getdescription(),
                hotel.getcheckInTime().format(timeFormatter),
                hotel.getcheckoutTime().format(timeFormatter)
            );
        }

        // 3. SELECT HOTEL
        int hotelId = 0;
        String hotelName = "";

        while (true)
        {
            try
            {
                System.out.print("\nEnter Hotel ID : ");
                hotelId = io.nextInt();
                io.nextLine();

                boolean validHotel = false;

                for (Hotel hotel : hotels)
                {
                    if (hotel.getid() == hotelId)
                    {
                        validHotel = true;
                        hotelName = hotel.getname();
                        break;
                    }
                }

                if (validHotel)
                {
                    break;
                }

                System.out.println("Invalid Hotel ID. Please select from the list above.");
            }
            catch (Exception e)
            {
                System.out.println("Invalid input. Enter numeric Hotel ID.");
                io.nextLine();
            }
        }

        // 4. CHECK-IN DATE
        LocalDate checkIn = null;

        while (checkIn == null)
        {
            try
            {
                System.out.print("Enter Check-In Date (yyyy-MM-dd) : ");
                checkIn = LocalDate.parse(
                    io.nextLine().trim(),
                    DateTimeFormatter.ofPattern("yyyy-MM-dd")
                );

                if (checkIn.isBefore(LocalDate.now()))
                {
                    System.out.println("Check-In date cannot be in the past. Today is " + LocalDate.now());
                    checkIn = null;
                }
            }
            catch (DateTimeParseException e)
            {
                System.out.println("Invalid date format. Example: " + LocalDate.now());
            }
        }

        // 5. CHECK-OUT DATE
        LocalDate checkOut = null;

        while (checkOut == null)
        {
            try
            {
                System.out.print("Enter Check-Out Date (yyyy-MM-dd) : ");
                checkOut = LocalDate.parse(
                    io.nextLine().trim(),
                    DateTimeFormatter.ofPattern("yyyy-MM-dd")
                );

                if (!checkOut.isAfter(checkIn))
                {
                    System.out.println("Check-Out date must be after Check-In date.");
                    checkOut = null;
                }
            }
            catch (DateTimeParseException e)
            {
                System.out.println("Invalid date format. Example: " + checkIn.plusDays(1));
            }
        }

        // 6. ROOM TYPE
        String title = null;

        while (title == null)
        {
            System.out.println("\n------ ROOM TYPE ------");
            System.out.println("1. SINGLE");
            System.out.println("2. SUITE");
            System.out.println("3. DELUXE");

            System.out.print("Enter Room Type : ");

            try
            {
                byte choice = io.nextByte();
                io.nextLine();

                switch (choice)
                {
                    case 1:
                        title = RoomTypes.SINGLE.toString();
                        break;
                    case 2:
                        title = RoomTypes.SUITE.toString();
                        break;
                    case 3:
                        title = RoomTypes.DELUXE.toString();
                        break;
                    default:
                        System.out.println("Wrong choice. Enter 1, 2, or 3.");
                }
            }
            catch (Exception e)
            {
                System.out.println("Enter numeric value only.");
                io.nextLine();
            }
        }

        // 7. AC TYPE
        String type = null;

        while (type == null)
        {
            System.out.println("\n------ AC TYPE ------");
            System.out.println("1. AC");
            System.out.println("2. NON-AC");

            System.out.print("Enter AC Type : ");

            try
            {
                byte choice = io.nextByte();
                io.nextLine();

                switch (choice)
                {
                    case 1:
                        type = AcType.AC.toString();
                        break;
                    case 2:
                        type = AcType.NONAC.toString();
                        break;
                    default:
                        System.out.println("Wrong choice. Enter 1 or 2.");
                }
            }
            catch (Exception e)
            {
                System.out.println("Enter numeric value only.");
                io.nextLine();
            }
        }

        // 8. SEARCH AVAILABLE ROOMS

        System.out.println("\n================================================");
        System.out.println("             SEARCHING ROOMS");
        System.out.println("================================================");
        System.out.println("Hotel          : " + hotelName);
        System.out.println("Check-In       : " + checkIn);
        System.out.println("Check-Out      : " + checkOut);
        System.out.println("Room Type      : " + title);
        System.out.println("AC Type        : " + type);

        List<Room> availableRooms = CustomerService.getAvailableRooms(
            hotelId,
            checkIn,
            checkOut,
            title,
            type
        );

        if (availableRooms == null || availableRooms.isEmpty())
        {
            System.out.println("\n❌ No rooms available matching your criteria for the selected dates.");
            return;
        }

        // 9. DISPLAY AVAILABLE ROOMS

        System.out.println("\n================ AVAILABLE ROOMS ================\n");
        System.out.printf(
            " %-8s | %-10s | %-8s | %-10s | %-8s | %-12s | %-8s%n%n",
            "Room ID", "Room No", "Floor", "Type", "AC", "Price/Night", "Capacity"
        );
        

        for (Room result : availableRooms)
        {
            System.out.printf(
                " %-8d | %-10d | %-8d | %-10s | %-8s | %-12.2f | %-8d%n",
                result.getid(),
                result.getroomNumber(),
                result.getfloorNumber(),
                result.gettitle(),
                result.getactype(),
                result.getbasePrice(),
                result.getcapacity()
            );
        }

        // 10. SELECT ROOM
        Room selectedRoom = null;
        while (selectedRoom == null)
        {
            try
            {
                System.out.print("\nEnter Room ID to Book (or 0 to Cancel) : ");
                int selectedId = io.nextInt();
                io.nextLine();

                if (selectedId == 0)
                {
                    System.out.println("Booking cancelled.");
                    return;
                }

                for (Room result : availableRooms)
                {
                    if (result.getid() == selectedId)
                    {
                        selectedRoom = result;
                        break;
                    }
                }

                if (selectedRoom == null)
                {
                    System.out.println("Invalid Room ID. Please select a Room ID from the table above.");
                }
            }
            catch (Exception e)
            {
                System.out.println("Invalid input. Enter numeric Room ID.");
                io.nextLine();
            }
        }

        // 11. PRICE CALCULATION

        long nights = ChronoUnit.DAYS.between(checkIn, checkOut);
        double totalAmount = nights * selectedRoom.getbasePrice();

        // 12. SUMMARY & CONFIRMATION
        System.out.println("\n================ BOOKING SUMMARY ================");
        System.out.println("Guest Name     : " + currentUser.getname());
        System.out.println("Hotel          : " + hotelName);
        System.out.println("Room Number    : " + selectedRoom.getroomNumber());
        System.out.println("Room Type      : " + selectedRoom.gettitle() + " (" + selectedRoom.getactype() + ")");
        System.out.println("Check-In Date  : " + checkIn);
        System.out.println("Check-Out Date : " + checkOut);
        System.out.println("Total Nights   : " + nights);
        System.out.printf("Price / Night  : Rs. %.2f%n", selectedRoom.getbasePrice());
        System.out.printf("Total Amount   : Rs. %.2f%n", totalAmount);
        System.out.println("=================================================");

        System.out.print("Proceed to Payment & Confirm Booking? (1. Yes / 2. No) : ");
        byte confirm = 0;
        try
        {
            String cStr = io.nextLine().trim();
            if(!cStr.isEmpty()) confirm = Byte.parseByte(cStr);
        }
        catch (Exception e)
        {
            confirm = 0;
        }

        if (confirm == 1)
        {
            PaymentType pType = PaymentUI.selectPaymentType(io);
            if(pType == null)
            {
                System.out.println("\n❌ Payment cancelled. Booking was not created.");
                return;
            }

            PaymentStatus status=PaymentStatus.CONFIRMED;
            Booking newBooking = new Booking(
                checkIn,
                checkOut,
                status.toString(),
                totalAmount,
                currentUser.getid(),
                selectedRoom.getroomNumber()
            );

            System.out.println("\nProcessing " + pType + " payment of Rs. " + String.format("%.2f", totalAmount) + "...");
            boolean success = PaymentService.createBookingWithPayment(
                newBooking,
                selectedRoom.getid(),
                selectedRoom.getbasePrice(),
                pType
            );

            if (success)
            {
                System.out.println("\n✅ Payment SUCCESS via " + pType + "!");
                System.out.println("✅ Booking Confirmed Successfully!");
                System.out.println("Your Booking ID is: " + newBooking.getid());
                System.out.printf("Total Amount Paid: Rs. %.2f%n", totalAmount);
            }
            else
            {
                System.out.println("\n❌ Failed to process payment or save booking. Please try again.");
            }
        }
        else
        {
            System.out.println("\nBooking was cancelled.");
        }
    }
}