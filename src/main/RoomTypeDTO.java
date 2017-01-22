package comp9321;
import java.io.Serializable;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;

import comp9321.*;
import comp9321.bean.*;
import comp9321.dao.*;
import comp9321.servlet.*;



public class RoomTypeDTO implements Serializable {
	private ArrayList<String> roomType = null;
	private CustomerDAO hotel;
	
	public RoomTypeDTO() throws ServiceLocatorException{
		try {
			hotel = new CustomerDAO();
			
		} catch (SQLException exc) {
			
			exc.printStackTrace();
		}
	}
	
	public ArrayList<String> getRoomType(){
		if (roomType==null){
			roomType = hotel.getRoomType();
		}
		return roomType;
	}
	
		
}
