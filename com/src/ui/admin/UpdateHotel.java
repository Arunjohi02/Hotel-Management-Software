package src.ui.admin;

import java.util.Scanner;

import src.service.HotelService;
import src.util.InputUtil;

public class UpdateHotel
{
	public static void updateHotel()
	{
		Scanner io = InputUtil.getScanner();

		System.out.print("Enter Hotel ID to Update: ");
		int hotelId = 0;
		try
		{
			hotelId = Integer.parseInt(io.nextLine().trim());
		}
		catch(Exception e)
		{
			System.out.println("Invalid numeric input.");
			return;
		}

		String result = (HotelService.update(hotelId)) ? "\n ✅ Updated Successfully \n" : "\n ❌ Update Failed \n";

		System.out.println(result);
	}
}