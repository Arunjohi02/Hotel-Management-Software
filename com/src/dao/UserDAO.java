package src.dao;

import java.util.List;
import java.util.ArrayList;

import src.model.User;
import src.util.DBConnection;

import java.sql.Connection;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.PreparedStatement;
import java.sql.Date;
import java.time.LocalDate;

public class UserDAO
{
	static String Query;
	static Connection con;

	static 
	{
		try
		{
			con = DBConnection.getConnection();
		}
		catch(Exception err)
		{
			System.err.print(err.getMessage());
		}
	}

	public static List<User> allManager()
	{
		Query = "Select id,name,email,phone,create_At,dob from Users where roll='MANAGER'";

		List<User> users = new ArrayList<>();

		try
		(
			Statement stmt = con.createStatement();
			ResultSet res = stmt.executeQuery(Query);
		)
		{
			while(res.next())
			{
				User user = new User();

				user.setid(res.getInt(1));
				user.setname(res.getString(2));
				user.setemail(res.getString(3));
				user.setphone(res.getString(4));
				user.setcreateDate(res.getDate(5).toLocalDate());
				user.setdob(res.getDate(6).toLocalDate());

				users.add(user);
			}
		}
		catch(Exception err)
		{
			System.err.println(err.getMessage());
		}

		return users;
	}

	public static int userid(String email)
	{
		Query = "SELECT id FROM Users WHERE email = ?";

		try(PreparedStatement ps = con.prepareStatement(Query))
		{
			ps.setString(1, email);
			try(ResultSet rs = ps.executeQuery())
			{
				if(rs.next())
				{
					return rs.getInt(1);
				}
			}
		}
		catch(Exception err)
		{
			System.out.println(err.getMessage());
		}
		return 0;
	}

	public static boolean addUser(User user)
	{
		Query = "INSERT INTO Users (name, DOB, email, phone, roll, create_At, password) VALUES (?, ?, ?, ?, ?, ?, ?)";

		try(PreparedStatement ps = con.prepareStatement(Query))
		{
			ps.setString(1, user.getname());
			ps.setDate(2, Date.valueOf(user.getdob()));
			ps.setString(3, user.getemail());
			ps.setString(4, user.getphone());
			ps.setString(5, user.getrole());
			ps.setDate(6, Date.valueOf(LocalDate.now()));
			ps.setString(7, user.getpassword());

			int rows = ps.executeUpdate();
			return rows > 0;
		}
		catch(Exception err)
		{
			System.err.println("Registration error: " + err.getMessage());
			return false;
		}
	}

	public static User login(String email, String password)
	{
		Query = "SELECT id, name, DOB, email, phone, roll, create_At FROM Users WHERE email = ? AND password = ?";

		try(PreparedStatement ps = con.prepareStatement(Query))
		{
			ps.setString(1, email);
			ps.setString(2, password);

			try(ResultSet rs = ps.executeQuery())
			{
				if(rs.next())
				{
					User user = new User();
					user.setid(rs.getInt(1));
					user.setname(rs.getString(2));
					user.setdob(rs.getDate(3).toLocalDate());
					user.setemail(rs.getString(4));
					user.setphone(rs.getString(5));
					user.setrole(rs.getString(6));
					user.setcreateDate(rs.getDate(7).toLocalDate());
					return user;
				}
			}
		}
		catch(Exception err)
		{
			System.err.println("Login error: " + err.getMessage());
		}
		return null;
	}

	public static User findCustomerById(int id)
	{
		String sql = "SELECT id, name, DOB, email, phone, roll, create_At FROM Users WHERE id = ? AND LOWER(roll) = 'customer'";

		try(PreparedStatement ps = con.prepareStatement(sql))
		{
			ps.setInt(1, id);
			try(ResultSet rs = ps.executeQuery())
			{
				if(rs.next())
				{
					User user = new User();
					user.setid(rs.getInt(1));
					user.setname(rs.getString(2));
					user.setdob(rs.getDate(3).toLocalDate());
					user.setemail(rs.getString(4));
					user.setphone(rs.getString(5));
					user.setrole(rs.getString(6));
					user.setcreateDate(rs.getDate(7).toLocalDate());
					return user;
				}
			}
		}
		catch(Exception err)
		{
			System.err.println("Error finding customer by ID: " + err.getMessage());
		}
		return null;
	}

	public static User findCustomerByEmail(String email)
	{
		String sql = "SELECT id, name, DOB, email, phone, roll, create_At FROM Users WHERE email = ? AND LOWER(roll) = 'customer'";

		try(PreparedStatement ps = con.prepareStatement(sql))
		{
			ps.setString(1, email);
			try(ResultSet rs = ps.executeQuery())
			{
				if(rs.next())
				{
					User user = new User();
					user.setid(rs.getInt(1));
					user.setname(rs.getString(2));
					user.setdob(rs.getDate(3).toLocalDate());
					user.setemail(rs.getString(4));
					user.setphone(rs.getString(5));
					user.setrole(rs.getString(6));
					user.setcreateDate(rs.getDate(7).toLocalDate());
					return user;
				}
			}
		}
		catch(Exception err)
		{
			System.err.println("Error finding customer by email: " + err.getMessage());
		}
		return null;
	}

	public static User findCustomerByPhone(String phone)
	{
		String sql = "SELECT id, name, DOB, email, phone, roll, create_At FROM Users WHERE phone = ? AND LOWER(roll) = 'customer'";

		try(PreparedStatement ps = con.prepareStatement(sql))
		{
			ps.setString(1, phone);
			try(ResultSet rs = ps.executeQuery())
			{
				if(rs.next())
				{
					User user = new User();
					user.setid(rs.getInt(1));
					user.setname(rs.getString(2));
					user.setdob(rs.getDate(3).toLocalDate());
					user.setemail(rs.getString(4));
					user.setphone(rs.getString(5));
					user.setrole(rs.getString(6));
					user.setcreateDate(rs.getDate(7).toLocalDate());
					return user;
				}
			}
		}
		catch(Exception err)
		{
			System.err.println("Error finding customer by phone: " + err.getMessage());
		}
		return null;
	}

	public static List<User> allCustomers()
	{
		String sql = "SELECT id, name, DOB, email, phone, roll, create_At FROM Users WHERE LOWER(roll) = 'customer' ORDER BY id ASC";
		List<User> customers = new ArrayList<>();

		try(Statement stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery(sql))
		{
			while(rs.next())
			{
				User user = new User();
				user.setid(rs.getInt(1));
				user.setname(rs.getString(2));
				user.setdob(rs.getDate(3).toLocalDate());
				user.setemail(rs.getString(4));
				user.setphone(rs.getString(5));
				user.setrole(rs.getString(6));
				user.setcreateDate(rs.getDate(7).toLocalDate());
				customers.add(user);
			}
		}
		catch(Exception err)
		{
			System.err.println("Error listing customers: " + err.getMessage());
		}
		return customers;
	}
}