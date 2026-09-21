import java.util.Scanner;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;

import src.model.User;

import src.service.UserService;

import src.ui.admin.AddHotel;
import src.ui.admin.AddAdmin;
import src.ui.admin.UpdateHotel;
import src.ui.admin.VieweHotel;
import src.ui.admin.ViewManager;
import src.ui.admin.AddManager ;
import src.ui.admin.AddReceptionist ;

import src.ui.customer.SearchHotel;
import src.ui.customer.RoomAvailability;
import src.ui.customer.Bookings;
import src.ui.customer.CustomerBookingsUI;
import src.ui.customer.PaymentUI;

import src.ui.receptionist.ReceptionistUI;
import src.ui.manager.ManagerUI;

import src.util.InputUtil;

class Main
{
	public static void main(String args[])
	{
		Scanner io = InputUtil.getScanner();
		byte choice = 0;

		do
		{
			System.out.println("\n ================== Hotel Booking & Room Booking ================== \n");
			System.out.println("1. Login");
			System.out.println("2. Register");
			System.out.println("3. Exit");
			System.out.print("Enter your Choice : ");

			try
			{
				String input = io.nextLine().trim();
				if(input.isEmpty())
				{
					continue;
				}
				choice = Byte.parseByte(input);
			}
			catch(NumberFormatException e)
			{
				choice = 0;
				System.out.println("\n❌ Invalid Input. Enter numeric value (1 - 3).");
				continue;
			}

			switch(choice)
			{
				case 1:
					System.out.print("\nEnter Your Email : ");
					String email = io.nextLine().trim();

					System.out.print("Enter Your Password : ");
					String password = io.nextLine().trim();

					User currentUser = UserService.logIn(email, password);

					if(currentUser != null)
					{
						String userRole = currentUser.getrole();
						System.out.println("\n✅ Login Successful! Welcome, " + currentUser.getname() + " (" + userRole + ")");

						if("ADMIN".equalsIgnoreCase(userRole))
						{
							byte adminChoice = 0;
							do
							{
								System.out.println("\n =============== Welcome To ADMIN Page =============== \n");
								System.out.println("1. Add Hotel");
								System.out.println("2. Add Admin");
								System.out.println("3. Update Hotel");
								System.out.println("4. View Hotels");
								System.out.println("5. View Managers");
								System.out.println("6. add Managers");
								System.out.println("7. add receptionist");
								System.out.println("8. Logout");
								System.out.print("Enter Your Choice : ");

								try
								{
									String adminInput = io.nextLine().trim();
									if(adminInput.isEmpty()) continue;
									adminChoice = Byte.parseByte(adminInput);
								}
								catch(NumberFormatException e)
								{
									adminChoice = 0;
									System.out.println("\n❌ Invalid Input. Enter numeric choice.");
									continue;
								}

								switch(adminChoice)
								{
									case 1:
										System.out.println("\n ==================== Hotel Info ==================== \n");
										AddHotel.addHotel();
										break;

									case 2:
										System.out.println("\n ==================== Add Admin ==================== \n");
										AddAdmin.addAdmin();
										break;

									case 3:
										System.out.println("\n ==================== Update Hotel ==================== \n");
										UpdateHotel.updateHotel();
										break;

									case 4:
										System.out.println("\n ==================== View Hotels ==================== \n");
										VieweHotel.vieweHotel();
										break;

									case 5:
										System.out.println("\n ==================== View Managers ==================== \n");
										ViewManager.viewManager();
										break;

									case 6:
										System.out.println("\n ==================== Add Managers ==================== \n");
										AddManager.addManager();
										break;

									case 7:
										System.out.println("\n ==================== Add Receptionist ==================== \n");
										AddReceptionist.addReceptionist();
										break;
											
									case 8:
										System.out.println("\nLogging out from Admin Account...");
										break;

									default:
										System.out.println("\n❌ Invalid Choice. Select 1 to 6.");
										break;
								}
							} while(adminChoice !=8);
						}
						else if("CUSTOMER".equalsIgnoreCase(userRole))
						{
							byte custChoice = 0;
							do
							{
								System.out.println("\n =============== Welcome To CUSTOMER Page =============== \n");
								System.out.println("1. Search Hotel");
								System.out.println("2. Room Availability");
								System.out.println("3. Book Room");
								System.out.println("4. My Bookings");
								System.out.println("5. Cancel Booking");
								System.out.println("6. Payment History");
								System.out.println("7. Logout");
								System.out.print("Enter Your Choice : ");

								try
								{
									String custInput = io.nextLine().trim();
									if(custInput.isEmpty()) continue;
									custChoice = Byte.parseByte(custInput);
								}
								catch(NumberFormatException e)
								{
									custChoice = 0;
									System.out.println("\n❌ Invalid Input. Enter numeric choice.");
									continue;
								}

								switch(custChoice)
								{
									case 1:
										System.out.println("\n============ Search Hotel ============\n");
										SearchHotel.searchHotel();
										break;

									case 2:
										System.out.println("\n============ Room Availability ============\n");
										RoomAvailability.roomAvailability();
										break;

									case 3:
										Bookings.booking(currentUser);
										break;

									case 4:
										CustomerBookingsUI.viewMyBookings(currentUser);
										break;

									case 5:
										CustomerBookingsUI.cancelBooking(currentUser);
										break;

									case 6:
										PaymentUI.viewPaymentHistory(currentUser);
										break;

									case 7:
										System.out.println("\nLogging out from Customer Account...");
										break;

									default:
										System.out.println("\n❌ Invalid Choice. Select 1 to 7.");
										break;
								}
							} while(custChoice != 7);
						}
						else if("RECEPTIONIST".equalsIgnoreCase(userRole))
						{
							ReceptionistUI.displayMenu(currentUser);
						}
						else if("MANAGER".equalsIgnoreCase(userRole))
						{
							ManagerUI.displayMenu(currentUser);
						}
						else
						{
							System.out.println("\nUnknown role: " + userRole);
						}
					}
					else
					{
						System.out.println("\n❌ Invalid Email or Password!\n");
					}
					break;

				case 2:
					System.out.println("\n============ Customer Registration ============ \n");

					System.out.print("Enter Your Name : ");
					String name = io.nextLine().trim();

					LocalDate dob = null;
					while(dob == null)
					{
						try
						{
							System.out.print("Enter Your DOB (YYYY-MM-DD) : ");
							dob = LocalDate.parse(io.nextLine().trim());
						}
						catch(DateTimeParseException e)
						{
							System.out.println("❌ Incorrect Date Format. Example: 2000-01-15");
						}
					}

					System.out.print("Enter Your Email : ");
					String regEmail = io.nextLine().trim();

					System.out.print("Enter Your Phone : ");
					String regPhone = io.nextLine().trim();

					System.out.print("Enter Your Password : ");
					String regPass = io.nextLine().trim();

					User newUser = new User(
						name,
						dob,
						regEmail,
						regPhone,
						"Customer",
						regPass
					);

					boolean regResult = UserService.addUser(newUser);

					if(regResult)
					{
						System.out.println("\n✅ Registered Successfully! You can now login with your credentials.\n");
					}
					else
					{
						System.out.println("\n❌ Registration Failed! Email or phone might already be in use.\n");
					}
					break;

				case 3:
					System.out.println("\n☺️ Thank you for using Hotel Management System. Goodbye!");
					break;

				default:
					System.out.println("\n❌ Invalid Input. Please enter 1, 2, or 3.\n");
					break;
			}
		} while(choice != 3);
	}
}