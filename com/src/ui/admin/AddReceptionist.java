package src.ui.admin ;

import java.util.Scanner ;
import src.util.InputUtil;

import java.time.LocalDate ;
import java.time.format.DateTimeParseException ;

import  src.model.User ;
import  src.service.UserService ;

import  src.enums.Role ;

public class AddReceptionist
{
	static Scanner io = InputUtil.getScanner();

	public static void addReceptionist()
	{
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

		Role role = Role.RECEPTIONIST;

		User newUser = new User(
			name,
			dob,
			regEmail,
			regPhone,
			role.toString(),
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
	}
}