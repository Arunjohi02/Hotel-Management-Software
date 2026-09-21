package src.model;

import java.time.LocalDate ;

public class User
{
	private int id ;
	private String name ;
	private String email ;
	private String phone ;
	private String role ;
	private LocalDate craeteAt ;
	private LocalDate dob ;
	private String password ;
	
	public User() {}

	public User(String name,
			LocalDate dob,
			String email,
			String phone,
			String role 
			)
	{
		this.name = name ;
		this.dob = dob ;
		this.email = email ;
		this.phone = phone ;
		this.role = role ;
	}

	public User(String name,
			LocalDate dob,
			String email,
			String phone,
			String role,
			String password
			)
	{
		this.name = name ;
		this.dob = dob ;
		this.email = email ;
		this.phone = phone ;
		this.role = role ;
		this.password = password ;
	}

	public int getid() { return id ; }
	
	public String getname() { return name ; }
	
	public String getemail() { return email ; }
	
	public String getphone() { return phone ; }
	
	public String getrole() { return role ; }
	
	public LocalDate getcreateDate() { return craeteAt ; }
	
	public LocalDate getdob() { return dob ; }
	
	
	public void setid(int id) 
	{ 
		this.id = id ;
	}
	
	public void setname(String name) 
	{ 
		this.name = name ;
	}
	
	public void setemail(String email)
	{ 
		this.email=email ;
	}
		   
	public void setphone(String phone) 
	{ 
		this.phone = phone;
	}
		   
	public void setrole(String role)
	{ 
		this.role = role ;
	}
		   
	public void setcreateDate( LocalDate createDate)
	{ 
		this.craeteAt = createDate ;
	}
		   
	public void setdob(LocalDate dob)
	{
		this.dob = dob ; 
	}
	
	public String getpassword()
	{
		return password ;
	}

	public void setpassword(String password)
	{
		this.password = password ;
	}
	
	public String toString()
	{
		return
			   name + " " +
			   email + " " +
		       phone + " " +
			   role + " " +
			   craeteAt + " " +
			   dob ;
	}
}