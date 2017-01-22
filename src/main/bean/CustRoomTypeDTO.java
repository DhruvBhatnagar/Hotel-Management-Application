package comp9321.bean;

import java.io.Serializable;
import java.sql.SQLException;
import java.util.ArrayList;

import comp9321.dao.CustomerDAO;
import comp9321.dao.ServiceLocatorException;

public class CustRoomTypeDTO implements Serializable {
	private ArrayList<String> roomType = null;
	private CustomerDAO hotel;
	
	public CustRoomTypeDTO() throws ServiceLocatorException{
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