package src.ui.admin ;

import java.util.List ;

import src.model.User ;

import src.service.UserService ;

public class ViewManager
{
	public static void viewManager()
	{
		List<User> managers = UserService.allManager();

		System.out.printf(" %-2s | %-20s | %-25s | %-15s | %-15s | %-15s %n%n" ,
			"ID","Name","Email","Phone","Join At","DOB");
		for(User manager : managers)
		{
			System.out.printf(" %-2s | %-20s | %-25s | %-15s | %-15s | %-15s %n%n" ,

				manager.getid(),
				manager.getname(),
				manager.getemail(),
				manager.getphone(),
				manager.getcreateDate(),
				manager.getdob());
		}
	}
}