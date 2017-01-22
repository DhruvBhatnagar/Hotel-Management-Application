package comp9321.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;
import comp9321.bean.*;
public class StaffDAO {

	static final Logger logger = Logger.getLogger(StaffDAO.class.getName());
	private Connection connection;
	DateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");

	public StaffDAO() throws ServiceLocatorException, SQLException{
		connection = DBConnectionFactory.getConnection();
		logger.info("StaffDAO() ready");
	}

	public List<BookingDTO> selectAllBooking() {
		ArrayList<BookingDTO> booking = new ArrayList<BookingDTO>();
		try{
			System.out.println("selecting all booking details");
			PreparedStatement ps = 
					connection.prepareStatement(
							"select bkid, emailID " +
							"from booking " +
							"left join consumer on consumer.id = booking.consid ");
			ResultSet res = ps.executeQuery();
			while(res.next()){
				long id = res.getLong("bkid");
				//String emailID = res.getString("emailID");
				//logger.info(id + " " +" " + checkin + " "+checkout);
				BookingDTO b = new BookingDTO(id);
				booking.add(b);
			}		
		} catch (Exception e){
			System.out.println("Caught Exception");
			e.printStackTrace();	
		}

		return booking;

	}

	public int countNeededRoomTypes (int bookingid, int typeid){
		int count = 0;
		try{
			
			PreparedStatement ps = 
					connection.prepareStatement(
							"select bookingdetails.roomType as rtype, count (*) as qty "+
							"from bookingdetails "+ 
							"where bookid = ? and bookingdetails.roomType=? "+
							"group by bookingdetails.roomType");
							
			ps.setInt(1, bookingid);
			ps.setInt(2, typeid);
			ResultSet res = ps.executeQuery();
			if (res.next()){
				count = res.getInt("qty");
			}	
			//System.out.println("countNeededRoomTypes for typeid " + typeid + " qty "+count);
		} catch (Exception e){
			System.out.println("Caught Exception");
			e.printStackTrace();	
		}

		return count;
	}
	
	
	public BookingDTO selectBookingDetails (int bookingid){
		BookingDTO booking = null;
		try{
			System.out.println("selecting details for ID " + bookingid);
			PreparedStatement ps = 
					connection.prepareStatement(
							"select bkid, HOTEL.CITY as hotelid, typename, BOOKINGDETAILS.ROOMTYPE as rtype, count (*) as qty, price, checkin, checkout "+
							"from booking "+ 
							"left join consumer on consumer.id = booking.consid "+
							"left join bookingdetails on bookid = bkid "+
							"left join roomtype on ROOMTYPE.ID = BOOKINGDETAILS.ROOMTYPE "+
							"left join HOTEL on hotel.id = BOOKINGDETAILS.HOTEL "+
							"where bkid = ? "+
							"group by typename, bkid, HOTEL.CITY, BOOKINGDETAILS.ROOMTYPE, price, checkin, checkout");

			ps.setInt(1, bookingid);
			ResultSet res = ps.executeQuery();
			if (res.next()){
				int id = res.getInt("bkid");
				booking = new BookingDTO(id);
				Date checkin = res.getDate("checkin");
				Date checkout = res.getDate("checkout");
				String city = res.getString("hotelid");
				//System.out.println("City: is " + city);
				booking.setCheckin(checkin);
				booking.setCheckout(checkout);
				booking.setCity(city);
				while(res.next()){
		
					//Date checkin =res.getDate("checkin");
					//Date checkout =res.getDate("checkout");
					int rtype = Integer.parseInt(res.getString("rtype"));
					double price = Double.parseDouble(res.getString("price"));
					booking.addBookingInterface(rtype, price, city, checkin, checkout, true);
					//System.out.println("book interface: "+id+" "+firstn+" "+lastn+ " " +rtype+ " ");
					logger.info(id + " " +" " + null + " "+null);
					
				}
			}		
		} catch (Exception e){
			System.out.println("Caught Exception");
			e.printStackTrace();	
		}

		return booking;
	}
/*
	public List<RoomTypeDTO> selectRoomdfsNeededFromBooking(int bookingid){
		ArrayList<RoomTypeDTO> roomlist = new ArrayList<RoomTypeDTO>();
		try{
			System.out.println("selecting all rooms need under ID " + bookingid);
			PreparedStatement ps = 
					connection.prepareStatement(
							"select booking.id as bookingid, roomType.id as roomtypeid, " +
									"roomType.tname as tname, bookedrooms.roomqty as roomqty " +
									"from booking  " +
									"left join bookedrooms on booking.id = bookedrooms.id " +
									"left join roomType on roomType.id = bookedrooms.roomtype " +
							"where booking.id = ?");

			ps.setInt(1, bookingid);
			ResultSet res = ps.executeQuery();

			while(res.next()){
				int id = res.getInt("roomtypeid");
				String tname =res.getString("tname");
				int roomqty = res.getInt("roomqty");
				System.out.println(tname+" by "+roomqty);
				logger.info(id + " " + tname +" " + roomqty);
				if (tname == null) {System.out.println("tname in selectRoomsNeededFromBooking() is null");break;};
				roomlist.add(new RoomTypeDTO(id, tname , 0,roomqty));
			}		
		} catch (Exception e){
			System.out.println("Caught Exception");
			e.printStackTrace();	
		}

		return roomlist;
	}
*/
	public List<RoomDTO> selectAvailableRoomsWithRoomType (int roomtypeid, String city){
		ArrayList<RoomDTO> roomlist = new ArrayList<RoomDTO>();
		System.out.println("Finding available rooms for the type: "+roomtypeid);
		try{
			PreparedStatement getrooms = connection.prepareStatement("select room.id, roomType.typename, hotel.city "+
					"from room "+
					"left join roomType on room.roomTypeid = roomType.id "+
					"left join hotel on room.hotelid = hotel.id "+
					"where room.roomTypeid = ? and room.status = 'available'" +
					"and hotel.city = ?");
			getrooms.setInt(1, roomtypeid);
			getrooms.setString(2, city);
			ResultSet res = getrooms.executeQuery();

			while(res.next()){
				int id = res.getInt("id");
				String tname = res.getString("typename");
				String hotelbranch = res.getString("city");
				//System.out.println("Found room with ID: " + id);
				roomlist.add(new RoomDTO(id, tname ,hotelbranch));
				
			}
		} catch (Exception e){
			System.out.println("Caught Exception");
			e.printStackTrace();	
		}
		return roomlist;
	}



	public List<RoomDTO> selectAvailableRoomsForBooking (int bookingid){
		ArrayList<RoomDTO> roomlist = new ArrayList<RoomDTO>();
		try{
			System.out.println("selecting all rooms for ID " + bookingid);
			PreparedStatement getroomtypes = 
					connection.prepareStatement(
							"select booking.id as bookingid, HOTEL.id as hotelid, hotel.city as city" +
							"roomType.id as rtype, typename "+
							"from booking "+ 
							"left join consumer on consumer.id = booking.cid "+
							"left join bookingdetails on bookid = booking.id "+
							"left join roomtype on ROOMTYPE.ID = BOOKINGDETAILS.ROOMTYPE "+
							"left join HOTEL on hotel.id = BOOKINGDETAILS.HOTEL "+
							"where bookid = ? "+
							"group by typename, booking.id,  HOTEL.id, rtype, typename");
			getroomtypes.setInt(1, bookingid);
			ResultSet typeres = getroomtypes.executeQuery();
			System.out.println("The booking id "+bookingid+ " needs the following rooms:");
		
			
			while (typeres.next()){
				int id = typeres.getInt("bookingid");
				int typeid = typeres.getInt("rtype");
				String tname  =typeres.getString("typename");
				int roomqty = typeres.getInt("qty");
				int hotelid = typeres.getInt("hotelid");
				String city = typeres.getString("city");
				System.out.println(tname+" by "+roomqty);
				if (tname == null) {System.out.println("tname is null");break;};
				//logger.info(id + " " + tname +" " + roomqty);
				
				System.out.println("Finding available rooms for the type: "+tname + " with ID: " +typeid);
				PreparedStatement getrooms = connection.prepareStatement("select room.id as roomid, hotel.city "+
						"from room "+
						"left join hotel on room.hotelid = ? "+
						"where room.roomTypeid = ? and room.status = 'available'");
				getrooms.setInt(1, typeid);
				getrooms.setInt(2, hotelid);
				ResultSet res = getrooms.executeQuery();

				while(res.next()){
					int roomid = res.getInt("roomid");
					//logger.info(id + " " + tname +" " + hotelbranch);
					//System.out.println("Found room with ID: " + id);
					roomlist.add(new RoomDTO(roomid, tname ,city));
				}

				res.close();
				getrooms.close();
				
			}

		} catch (Exception e){
			System.out.println("Caught Exception");
			e.printStackTrace();	
		}
		return roomlist;

	}
	//select room types needed by a booking
	/*
	public List<RoomTypeDTO> selectRoomTypeFromBooking(int bookingid){
		ArrayList<RoomTypeDTO> roomtypes = new ArrayList<RoomTypeDTO>();
		try {
			PreparedStatement ps = connection.prepareStatement(
					"select booking.id, roomType.tname, bookedrooms.roomqty " +
							"from booking " +
							"left join bookedrooms on booking.id = bookedrooms.id " +
							"left join roomType on roomType.id = bookedrooms.roomtype " +
					"where booking.id = ?");
			ps.setInt(1,bookingid);
			ResultSet res = ps.executeQuery();

			while(res.next()){
				int id = res.getInt("id");
				String roomtype = res.getString("tname");
				int qty = res.getInt("roomqty");
				logger.info(id + " " + roomtype +" " + qty);
				roomtypes.add(new RoomTypeDTO(id, roomtype,0,qty));
			}

		} catch (Exception e){
			System.out.println("Exception: selectRoomTypeFromBooking");
			e.printStackTrace();	
		}
		return roomtypes;
	}
*/
	//select available rooms with roomtype
	public List<RoomDTO> selectAllRoomsWithRoomType(int roomType){
		ArrayList<RoomDTO> list = new ArrayList<RoomDTO>();
		try{
			PreparedStatement ps = connection.prepareStatement(
					"select room.id, roomType.tname,hotel.city  " +
							"from room  " +
							"left join roomType on room.roomTypeid = roomType.id " +
							"left join hotel on room.hotelid = hotel.id " +
					"where roomType.id = ? and room.status='available'" );
			ps.setInt(1,roomType);
			ResultSet res = ps.executeQuery();
			while(res.next()){
				int id = res.getInt("id");
				String roomtype = res.getString("tname");
				String hotelbranch = res.getString("city");
				logger.info(id + " " + roomtype +" " + hotelbranch);
				list.add(new RoomDTO(id, roomtype ,hotelbranch));
			}
			res.close();
			ps.close();

		} catch (Exception e){
			System.out.println("Exception: selectAllRoomsWithRoomType()");
			e.printStackTrace();	
		}
		return list;

	}



	public BookingDTO selectBookingFromId(int bookingid) {
		BookingDTO booking = null;
		try{
			//Statement stmnt = connection.createStatement();
			PreparedStatement ps = connection.prepareStatement(
					"select id, cid,checkin,checkout from booking where id=?");
			ps.setInt(1, bookingid);
			ResultSet res = ps.executeQuery();

			if(res.next()){
				int id = res.getInt("id");
				int cid = res.getInt("cid");
				Date checkin = res.getDate("checkin");
				Date checkout = res.getDate("checkout");
				booking = new BookingDTO(id);
				logger.info(id + " " + cid +" " + checkin + " "+checkout);
			}
			res.close();
			ps.close();

		} catch (Exception e){
			System.out.println("Caught Exception");
			e.printStackTrace();	
		}

		return booking;
	}
	public List<RoomTypeDTO> selectRoomTypes(){
		ArrayList<RoomTypeDTO> roomtypes = new ArrayList<RoomTypeDTO>();
		try{
			Statement stmnt = connection.createStatement();
			System.out.println("selectAllBooking()");
			String query = 
					"select * from roomType ";
			ResultSet res = stmnt.executeQuery(query);

			while(res.next()){
				int id = res.getInt("id");
				String tname = res.getString("typename");
				int price = res.getInt("initialprice");
				roomtypes.add(new RoomTypeDTO(id, tname, price, 0));
				logger.info(id + " " + tname +" " + price + " "+price);
			}

		} catch (Exception e){
			System.out.println("Caught Exception");
			e.printStackTrace();	
		}

		return roomtypes;
	}
	
	public String selectRoomTypeName(int roomTypeId){
		String tname = "No such type";
		try{
			Statement stmnt = connection.createStatement();
			System.out.println("selectRoomTypeName");
			PreparedStatement ps = connection.prepareStatement("select typename from roomType where id = ?");
		
			ps.setInt(1, roomTypeId);
			ResultSet res = ps.executeQuery();

			if(res.next()){
				tname = res.getString("typename");
			}

		} catch (Exception e){
			System.out.println("Caught Exception");
			e.printStackTrace();	
		}

		return tname;
	}

	public ArrayList<RoomDTO> selectAllRoomsAssigned (){
		ArrayList<RoomDTO> list = new ArrayList<RoomDTO>();
		try{
			PreparedStatement ps = connection.prepareStatement(
					"select room.id, roomType.typename,hotel.city, room.bkid  " +
							"from room  " +
							"left join roomType on room.roomTypeid = roomType.id " +
							"left join hotel on room.hotelid = hotel.id " +
							"where status = 'unavailable'");

			ResultSet res = ps.executeQuery();
			while(res.next()){
				int id = res.getInt("id");
				String roomtype= res.getString("typename");
				String hotelbranch = res.getString("city");
				int bkid = res.getInt("bkid");
				//logger.info(id + " " + roomtype +" " + hotelbranch);
				RoomDTO newroom = new RoomDTO(id, roomtype ,hotelbranch);
				newroom.setBkid(bkid);
				list.add(newroom);
			}
			res.close();
			ps.close();

		} catch (Exception e){
			System.out.println("Exception: selectRoomsAssigned()");
			e.printStackTrace();	
		}
		return list;
	}
	
	
	public ArrayList<RoomDTO> selectRoomsAssigned (int bookingid){
		ArrayList<RoomDTO> list = new ArrayList<RoomDTO>();
		try{
			PreparedStatement ps = connection.prepareStatement(
					"select room.id, roomType.typename,hotel.city  " +
							"from room  " +
							"left join roomType on room.roomTypeid = roomType.id " +
							"left join hotel on room.hotelid = hotel.id " +
					"where room.bkid = ?");
			ps.setInt(1,bookingid);
			ResultSet res = ps.executeQuery();
			while(res.next()){
				int id = res.getInt("id");
				String roomtype = res.getString("typename");
				String hotelbranch = res.getString("city");
				//logger.info(id + " " + roomtype +" " + hotelbranch);
				list.add(new RoomDTO(id, roomtype ,hotelbranch));
			}
			res.close();
			ps.close();

		} catch (Exception e){
			System.out.println("Exception: selectRoomsAssigned()");
			e.printStackTrace();	
		}
		return list;
	}
	/*
	public void insertAddRoomToBooking (int bookingid, int roomid ){
	
		try{
			//Statement stmnt = connection.createStatement();
			PreparedStatement ps = connection.prepareStatement("update room set status = ? where id = ?;");
			ps.setString(1, "");
			ps.setInt(2, bookingid);
			ResultSet res = ps.executeQuery();
			logger.info("updated booking id" +  bookingid + " to status " + status);
			res.close();
			ps.close();

		} catch (Exception e){
			System.out.println("Caught Exception");
			e.printStackTrace();	
		}
		
	}
 */
	public void clearRoomStatus (int roomid){
		try{
			//Statement stmnt = connection.createStatement();
			PreparedStatement ps = connection.prepareStatement("update room set status = 'available', bkid = null where id = ?");
			ps.setInt(1, roomid);
			ps.executeUpdate();
			ps.close();

		} catch (Exception e){
			System.out.println("Caught Exception");
			e.printStackTrace();	
		}
	}

	public void updateRoomStatus(int bkdid, int roomid, String status) {
		try{
			//Statement stmnt = connection.createStatement();
			
			PreparedStatement ps = connection.prepareStatement("update room set status = ?, bkid = ? where id = ?");
			System.out.println("bkid is: " + bkdid);
			ps.setString(1, status);
			ps.setInt(2, bkdid);
			ps.setInt(3, roomid);
			ps.executeUpdate();
			ps.close();

		} catch (Exception e){
			System.out.println("Caught Exception");
			e.printStackTrace();	
		}
	}
	//count each roomtype of booked rooms
	public int countRoomTypeReserved(int bookingid, int roomTypeId){
		int count = 0;
		try{
			PreparedStatement ps = connection.prepareStatement("select count (*) as counter " +
					"from room where status = 'unavailable' and bkid = ? and roomTypeid = ? " +
					"group by roomTypeid");

			ps.setInt(1, bookingid);
			ps.setInt(2, roomTypeId);
			ResultSet res = ps.executeQuery();
			if (res.next()){
				count = res.getInt("counter");
				//System.out.println("counted booking id: "+bookingid+ "room type: "+ roomTypeId+ " = "+count);
			}
			ps.close();
			res.close();

		} catch (Exception e){
			System.out.println("Caught Exception");
			e.printStackTrace();	
		}
		return count;
	}
	
	public ArrayList<HotelBranchDTO> selectAllHotelBranch(){
		ArrayList<HotelBranchDTO> hotels = new ArrayList<HotelBranchDTO>();
		try{
			PreparedStatement ps = connection.prepareStatement("select * from hotel");
			ResultSet res = ps.executeQuery();
			while (res.next()){
				int id = res.getInt("id");
				String city = res.getString("city");
				//System.out.println("new hotel : "+id+ "  "+ name + " " + city);
				hotels.add(new HotelBranchDTO(id,"hotelbranch",city));
			}
		} catch (Exception e){
			System.out.println("Caught Exception");
			e.printStackTrace();	
		}
		return hotels;
	}
	 
	public int countHotelOccupied (int hotelid){
		int count = 0;
		try{
			PreparedStatement ps = connection.prepareStatement("select count(*) as counter " +
					"from room " +
					"where hotelid = ? and status = 'unavailable'");
			ps.setInt(1, hotelid);
			ResultSet res = ps.executeQuery();
			if (res.next()){
				count = res.getInt("counter");
				//System.out.println("counted hotels: "+hotelid+ " count: "+ count);
			}
		} catch (Exception e){
			System.out.println("Caught Exception");
			e.printStackTrace();	
		}
		return count;
	}
	
	public int countHotelAvailable (int hotelid){
		int count = 0;
		try{
			PreparedStatement ps = connection.prepareStatement("select count(*) as counter " +
					"from room " +
					"where hotelid = ? and status = 'available'");
			ps.setInt(1, hotelid);
			ResultSet res = ps.executeQuery();
			if (res.next()){
				count = res.getInt("counter");
				//System.out.println("counted hotels: "+hotelid+ " count: "+ count);
			}
		} catch (Exception e){
			System.out.println("Caught Exception");
			e.printStackTrace();	
		}
		return count;
	}
	
	public boolean addDiscount(DiscountDTO d){
		try {
			DateFormat dtf = new SimpleDateFormat("yyyy-MM-dd");
			PreparedStatement ps = connection.prepareStatement("insert into HotelDiscounts " +
					"values (?, ?, ?, ?, ?)");
			ps.setDouble(1,d.getRate());
			ps.setString(2,dtf.format(d.getFrom()));
			ps.setString(3,dtf.format(d.getTo()));
			ps.setInt(4,d.getHotelid());
			ps.setInt(5,d.getRoomtypeid());
	
			ps.executeUpdate();
			//System.out.println("not inserted, fix insert date");
			return true;
		} catch (Exception e){
			System.out.println("Caught Exception");
			e.printStackTrace();
			return false;
		}

	} 
	
}


