package src.service;

import java.util.List;

import src.model.User;
import src.dao.UserDAO;

public class UserService
{
	public static User logIn(String email, String password)
	{
		if(email == null || email.trim().isEmpty() || password == null || password.trim().isEmpty())
		{
			return null;
		}

		return UserDAO.login(email.trim(), password.trim());
	}

	public static List<User> allManager()
	{
		return UserDAO.allManager();
	}

	public static boolean addAdmin(User user)
	{
		if(user == null || user.getname() == null || user.getname().trim().isEmpty()
			|| user.getemail() == null || user.getemail().trim().isEmpty()
			|| user.getphone() == null || user.getphone().trim().isEmpty()
			|| user.getdob() == null
			|| user.getpassword() == null || user.getpassword().trim().isEmpty())
		{
			return false;
		}

		user.setrole("Admin");
		return UserDAO.addUser(user);
	}

	public static boolean addUser(User user)
	{
		if(user == null || user.getname() == null || user.getname().trim().isEmpty()
			|| user.getemail() == null || user.getemail().trim().isEmpty()
			|| user.getphone() == null || user.getphone().trim().isEmpty()
			|| user.getdob() == null
			|| user.getpassword() == null || user.getpassword().trim().isEmpty())
		{
			return false;
		}

		if(user.getrole() == null || user.getrole().trim().isEmpty())
		{
			user.setrole("Customer");
		}

		return UserDAO.addUser(user);
	}
}