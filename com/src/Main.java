package com.src ;

import java.util.Scanner ;

import java.time.LocalDate ;

import java.time.format.*;

import com.src.model.User ;

import com.src.service.UserService ;

import com.src.exception.InvalidDate ;

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
			choice = io.nextByte();
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
						System.out.println("\n✅ LogIn Succesess....\n");
					}
					else
					{
						System.out.println("\n❌ LogIn Err\n");
						break;
					}
					
					break;
					
				case 2 :
				
					System.out.println("\n============ Register ==========");
					
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
					
					break;
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