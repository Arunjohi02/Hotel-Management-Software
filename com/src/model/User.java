package com.src.model;

import java.time.LocalDate ;

public class User
{
	private String name ;
	private String email ;
	private String phone ;
	private String roll ;
	private LocalDate craeteAt ;
	private LocalDate dob ;
	
	public User() {}

	public User(String name,
			LocalDate dob,
			String email,
			String phone)
	{
		this.name = name ;
		this.dob = dob ;
		this.email = email ;
		this.phone = phone ;
	}

	public String getname() { return name ; }
	
	public String getemail() { return email ; }
	
	public String getphone() { return phone ; }
	
	public String getroll() { return roll ; }
	
	public LocalDate getcreateDate() { return craeteAt ; }
	
	public LocalDate getdob() { return dob ; }
	
	
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
		   
	public void setroll(String roll)
	{ 
		this.roll = roll ;
	}
		   
	public void setcreateDate( LocalDate createDate)
	{ 
		this.craeteAt = createDate ;
	}
		   
	public void setdob(LocalDate dob)
	{
		this.dob = dob ; 
	}
	
	public String toString()
	{
		return name + " " +
			   email + " " +
		       phone + " " +
			   roll + " " +
			   craeteAt + " " +
			   dob ;
	}
}