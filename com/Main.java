import java.util.Scanner ;
import java.util.InputMismatchException ;

import java.time.LocalDate ;
import java.time.format.DateTimeParseException ;

import src.model.User ;

import src.service.UserService ;

import src.exception.InvalidDate ;
import src.exception.InputException ;

import src.enums.Role ;

class Main
{
	public static void main(String args[])
	{
		Scanner io = new Scanner(System.in);
		byte choice=0;
		
		System.out.println(" ================== Hotel Booking & Room Booking ================== ");

		System.out.println("1.Login");
		System.out.println("2.Register");
		System.out.println("3.Exit");
		
		while(true)
		{
			System.out.print("Enter your's Choice :");

			try
			{
				choice = io.nextByte();
			}
			catch(InputMismatchException e)
			{
				try
				{
					throw new InputException("\n❌Invalid Input Accepting only Numaric value Max:3");
				}
				catch(InputException err)
				{
					System.out.println(err.getMessage());
				}
			}

			io.nextLine();
			
			switch(choice)
			{
				case 1 :
				
					System.out.print("\nEnter Your's Email :");
					String email = io.nextLine() ;
					
					System.out.print("Enter Your's password  :");
					String password = io.nextLine() ;

					if(UserService.logIn(email,password))
					{
						if((UserService.role()).equalsIgnoreCase("ADMIN"))
						{
							System.out.println("This is Admin");
						}
						else if(UserService.role().equalsIgnoreCase("RECEPTIONIST"))
						{
							System.out.println("This is RECEPTIONIST");
						}
						else if(UserService.role().equalsIgnoreCase("MANAGER"))
						{
							System.out.println("This is Manager");
						}
						else if(UserService.role().equalsIgnoreCase("CUSTOMER"))
						{
							System.out.println("This is CUSTOMER");
						}
						
					}
					else
					{
						System.out.println("\n❌ LogIn Err\n");
						break;
					}
					
					break;
					
				case 2 :
				
					System.out.println("\n============ Register ============");
					
					System.out.print("Enter Your Name :");
					String Name = io.nextLine();
					
					LocalDate DOB=null;
					
					try
					{
						System.out.print("Enter Your DOB ( YYYY-MM-DD ) :");
						DOB = LocalDate.parse(io.nextLine());
					}
					catch(DateTimeParseException e)
					{
						try
						{
							throw new InvalidDate( "Incorrect Date Formate" );
						}
						catch(InvalidDate err)
						{
							System.out.println(err.getMessage());
						}
						
						break;
					}
					
					
					System.out.print("Enter Your Email ( User@gmail.com ):");
					email = io.nextLine() ;
					
					System.out.print("Enter Your Phone :");
					String phone = io.nextLine();
					
					User User = 
						new User(
							Name,
							DOB,
							email,
							phone
						);
						
					//service.addUser(User)
					if(true)
					{
						System.out.println("\n✅ Register SuccessFully......\n");
					}
					else
					{
						System.out.println("\n❌ Faild Register\n");
					}
					
					break ;
				case 3 :
				
						System.out.println("☺️ Thank For Your's Attenting");
						
					return ;
					
				default :
					
						System.out.println("\n❌ Invalid Input....");
							
						System.out.println("1.Login");
						System.out.println("2.Register");
						System.out.println("3.Exit\n");
		
					break;
			}
		}
	}
}