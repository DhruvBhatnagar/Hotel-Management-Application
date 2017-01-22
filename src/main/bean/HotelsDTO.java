package comp9321.bean;

import java.sql.SQLException;
import java.util.ArrayList;

import comp9321.dao.*;


public class HotelsDTO {
	private ArrayList<String> htlName = null;
	private CustomerDAO hotel;
	
	public HotelsDTO() throws ServiceLocatorException{
		try {
			hotel = new CustomerDAO();
		} catch (SQLException exc) {
			
			exc.printStackTrace();
		}
	}
	
	public ArrayList<String> gethtlName(){
		if (htlName==null){
			htlName = hotel.getHotelName();
		}
		return htlName;
	}
	
}