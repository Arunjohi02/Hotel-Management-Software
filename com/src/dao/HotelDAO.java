package src.dao ;

import java.util.List ;
import java.util.ArrayList ;

import src.model.Hotel ;
import src.model.Room ;

import src.util.DBConnection ;

import java.sql.* ;
import java.sql.Connection ;
import java.sql.Statement ;
import java.sql.ResultSet ;
import java.sql.PreparedStatement ;
import java.sql.SQLException ;
import java.sql.Time ;


public class HotelDAO
{
	public static int id;
	static Connection con;
	static String Query;

	static 
	{
		try
		{
			con=DBConnection.getConnection();
		}
		catch(Exception err)
		{
			System.err.print(err.getMessage());
		}
	}
	
	public static boolean addHotel(Hotel hotel,Room roomInfo[])
	{
		Query = """

			  INSERT INTO Hotel(name,location,phone,email,description,checkin,checkout,totalcount) VALUES (?,?,?,?,?,?,?,?) ;

		""";

		try(
			PreparedStatement ps = con.prepareStatement(Query,Statement.RETURN_GENERATED_KEYS);
		)
		{
			con.setAutoCommit(false);

			ps.setString(1,hotel.getname());
			ps.setString(2,hotel.getlocation());
			ps.setString(3,hotel.getphone());
			ps.setString(4,hotel.getemail());
			ps.setString(5,hotel.getdescription());
			ps.setTime(6,java.sql.Time.valueOf(hotel.getcheckInTime()));
			ps.setTime(7,java.sql.Time.valueOf(hotel.getcheckoutTime()));
			ps.setInt(8,hotel.gettotalRooms());

			System.out.println("Hotel inserting...");

			int row = ps.executeUpdate();

			System.out.println("Hotel inserted. Rows = " + row);

			ResultSet rs = ps.getGeneratedKeys();

			if (!rs.next())
			{
			    System.out.println("Generated ID not found");
			    con.rollback();
			    return false;
			}

			int id = rs.getInt(1);

			System.out.println("Generated Hotel ID = " + id);

			if (addrooms(roomInfo, id))
			{
			    System.out.println("Rooms inserted successfully");
			    con.commit();
			    return true;
			}

			System.out.println("Room insertion failed");
			con.rollback();
			return false;
		}
		catch(Exception err)
		{
			System.out.println(err.getMessage());
			try
			{
				con.rollback();
			}
			catch(Exception ignored) {}
		}
		finally
		{
			try
			{
				con.setAutoCommit(true);
			}
			catch(Exception ignored) {}
		}
		return false;

	}

	public static boolean addrooms(Room room[],int id)
	{
		Query = "INSERT INTO rooms (room_number,floor_no,status,title,ACType,price,capacity,hotel_id) VALUES (?,?,?,?,?,?,?,?);";

		try(PreparedStatement ps=con.prepareStatement(Query))
		{
			for(Room res : room)
			{
				ps.setInt(1,res.getroomNumber());
				ps.setInt(2,res.getfloorNumber());
				ps.setString(3,res.getstatus());
				ps.setString(4,res.gettitle());
				ps.setString(5,res.getactype());
				ps.setDouble(6,res.getbasePrice());
				ps.setInt(7,res.getcapacity());
				ps.setInt(8,id);
				
				ps.executeUpdate();
			}	
			return true;
		}
		catch(Exception err)
		{
			System.out.println(err.getMessage());
		}

		
		return false;
	}

	public static List<Hotel> allhotels()
	{
		Query = """
			Select id,name,location,phone,email,description,checkin,checkout,totalcount from Hotel ;
		""" ;

		List<Hotel> hotels=new ArrayList<>();

		try
		(
			Statement stmt=con.createStatement();
			ResultSet res=stmt.executeQuery(Query);
		)
		{
			while(res.next())
			{
				
				Hotel hotel=new Hotel();

				hotel.setid(res.getInt(1));
				hotel.setname(res.getString(2));
				hotel.setlocation(res.getString(3));
				hotel.setphone(res.getString(4));
				hotel.setemail(res.getString(5));
				hotel.setdescription(res.getString(6));
				hotel.setcheckInTime(res.getTime(7).toLocalTime());
				hotel.setcheckoutTime(res.getTime(8).toLocalTime());
				hotel.settotalRooms(res.getInt(9));

				hotels.add(hotel);
			}

		}
		catch(Exception err)
		{
			System.err.println(err.getMessage());
		}
		return hotels;
	}

	public static List<Room> allRooms(int hotelId)
	{
		List<Room> rooms = new ArrayList<>();
		String sql = """
			SELECT r.id, r.room_number, r.floor_no, r.status, r.hotel_id, r.title, r.ACType, r.price, r.capacity, h.name as hotel_name
			FROM rooms r
			JOIN hotel h ON r.hotel_id = h.id
		""";

		if(hotelId > 0)
		{
			sql += " WHERE r.hotel_id = ? ";
		}
		sql += " ORDER BY r.hotel_id, r.room_number ";

		try
		{
			PreparedStatement ps = con.prepareStatement(sql);
			if(hotelId > 0)
			{
				ps.setInt(1, hotelId);
			}

			ResultSet rs = ps.executeQuery();
			while(rs.next())
			{
				Room room = new Room();
				room.setid(rs.getInt("id"));
				room.setroomNumber(rs.getInt("room_number"));
				room.setfloorNumber(rs.getInt("floor_no"));
				room.setstatus(rs.getString("status"));
				room.sethotelId(rs.getInt("hotel_id"));
				room.settitle(rs.getString("title"));
				room.setactype(rs.getString("ACType"));
				room.setbasePrice(rs.getDouble("price"));
				room.setcapacity(rs.getInt("capacity"));
				room.sethotelName(rs.getString("hotel_name"));

				rooms.add(room);
			}
			rs.close();
			ps.close();
		}
		catch(Exception err)
		{
			System.err.println("Error fetching rooms: " + err.getMessage());
		}
		return rooms;
	}

	public static boolean updateRoomStatus(int roomId, String newStatus)
	{
		String sql = "UPDATE rooms SET status = ? WHERE id = ?";
		try(PreparedStatement ps = con.prepareStatement(sql))
		{
			ps.setString(1, newStatus);
			ps.setInt(2, roomId);
			int rows = ps.executeUpdate();
			return rows > 0;
		}
		catch(Exception err)
		{
			System.err.println("Error updating room status: " + err.getMessage());
			return false;
		}
	}
}