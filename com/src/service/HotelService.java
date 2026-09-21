package src.service ;

import java.util.List ;

import src.model.Hotel ;
import src.model.Room ;

import src.dao.HotelDAO;

public class HotelService
{
	static boolean result ;

	public static boolean addHotel(Hotel hotel,Room roominfo[])
	{
		result = ( hotel.getname() == null ||
			hotel.getname().trim().isEmpty() || 
			hotel.getphone() == null ||
			hotel.getphone().trim().isEmpty() || !addrooms(roominfo))   ? false : HotelDAO.addHotel(hotel,roominfo);

		return result ;
	}

	public static boolean addrooms(Room room[])
	{
		for(Room res:room)
		{
			if(res.getfloorNumber() < 0 || res.getbasePrice()<0)
			{
				return false;
			}
			
		}
		return true;
	}

	public static boolean update(int id)
	{
		result = (true) ? true : false ;

		return result ;
	}

	public static List<Hotel> allhotels()
	{
		return HotelDAO.allhotels();
	}

	public static int getid()
	{
		int id = HotelDAO.id ;
		return id;
	}
}