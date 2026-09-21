package src.ui.admin ;

import java.util.Scanner ;

import java.time.LocalDate ;

import java.util.Scanner ;

import src.model.User ;

import src.service.UserService ;

import src.enums.Role ;
import src.util.InputUtil ;

public class AddAdmin
{
	static Scanner io = InputUtil.getScanner();

	public static void addAdmin()
	{
										
		System.out.print("Name 		   : ") ;
		String name = io.nextLine() ;
		
		System.out.print("DOB  ( YYYY-MM-DD ): ") ;
		String Birth= io.nextLine() ;
		LocalDate dob = LocalDate.parse(Birth) ;

		System.out.print("Phone 	   : ") ;
		String phone = io.nextLine() ;
		
		System.out.print("Email 	   : ") ;
		String email = io.nextLine() ;

		System.out.print("Password 	   : ") ;
		String password = io.nextLine() ;

		Role role=Role.ADMIN;
		String roleS=role.toString();

		User user=
				new User(
					name,
					dob,
					email,
					phone,
					roleS,
					password
				);

		
		String result=(UserService.addAdmin(user))? "\n✅ Admin Added SuccessFully......\n" : "\n❌ Faild Register\n" ;

		System.out.println(result);
	}

}