package comp9321.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Random;
import java.util.UUID;
import java.util.logging.Logger;

import comp9321.bean.*;
import comp9321.servlet.*;
import comp9321.dao.*;
import comp9321.*;


public class CustomerDAO{
	static Logger logger = Logger.getLogger(CustomerDAO.class.getName());
	private Connection connection;
	private DateFormat dtf = new SimpleDateFormat("yyyy-MM-dd");
	private static final int NumRoom = 5;

	
	public CustomerDAO() throws SQLException, ServiceLocatorException{
		connection = DBConnectionFactory.getConnection();
		logger.info("Connection Established");
	}

	public ArrayList<String> getHotelName(){
		ArrayList<String> listret = new ArrayList<String>();
		try{
			Statement statmnt = connection.createStatement();
			String qc = "select city from Hotel order by id";
			ResultSet result = statmnt.executeQuery(qc);
			while(result.next()) listret.add(result.getString("city"));
			result.close();
			statmnt.close();
		}catch(Exception exception){
			System.out.println("Caught Exception");
			exception.printStackTrace();
		}
		return listret;
	}
	
	public ArrayList<String> getRoomType(){
		ArrayList<String> listret = new ArrayList<String>();
		try{
			Statement statmnt = connection.createStatement();
			String qc = "select rtype.id, rtype.typename from ROOMTYPE rtype order by rtype.id";
			ResultSet result = statmnt.executeQuery(qc);
			logger.info("The result set size is "+result.getFetchSize());
			while(result.next()) {
				listret.add(result.getString("typename"));
			}
			result.close();
			statmnt.close();
		}catch(Exception exception){
			System.out.println("Caught Exception");
			exception.printStackTrace();
		}
		return listret;
	}
	

	
	public ArrayList<RoomStatusDTO> getRoomsAvailable (String cty,int lim, Date ci, Date co){
		ArrayList<RoomStatusDTO> listret = new ArrayList<RoomStatusDTO>(); 
		try{
			Statement statmnt = connection.createStatement();
			ResultSet result;
			
			getRoomList(cty, statmnt, listret);
			ArrayList<RoomDiscounts> disc = getRoomDiscounts(cty, ci, co, statmnt);
			
			Calendar m = Calendar.getInstance();
			m.setTime(ci);

			Calendar exc = Calendar.getInstance();
			exc.setTime(co);

			ArrayList<Triplet<Calendar,Calendar,Integer>> bookingList = new ArrayList<Triplet<Calendar,Calendar,Integer>>();
			
			
			String query = "select bd.roomType as RM_TYPE, bd.checkin as cid, bd.checkout as cod" 
					+ "  from BookingDetails bd " 
					+ "  join Hotel h on (h.id=bd.hotel)  "
					+ "  where UPPER(h.city) =  '"+cty.toUpperCase()+"' " 
					+ "  and bd.checkin <= '"+ dtf.format(co) + "' and bd.checkout >= '"+ dtf.format(ci) + "'  "
					+ "	 order by bd.roomType";
			
			result = statmnt.executeQuery(query);
			while (result.next()){
				System.out.println("found "+result.getInt("RM_TYPE") + " - "+result.getDate("cid") + " to  "+ result.getDate("cod"));
				Calendar t1 = Calendar.getInstance();
				Calendar t2 = Calendar.getInstance();
				t1.setTime(result.getDate("cid"));
				t2.setTime(result.getDate("cod"));
				bookingList.add(new Triplet<Calendar, Calendar, Integer>(t1,t2,result.getInt("RM_TYPE")));
			}
			
			int[] maximumRoomsBooked = new int[NumRoom];
			double[] maximumRoomPrice = new double[NumRoom];
			double[] initialRoomPrice = new double[NumRoom];
			
			
			for (RoomStatusDTO j:listret){
				j.setCiBooking(ci);
				j.setCoBooking(co);
				initialRoomPrice[j.getRoomTypeid()-1] = j.getMaxPrice();
			}

			while (m.compareTo(exc) <= 0){
				int[] typeClashes = {0,0,0,0,0};
				double[] roomPrice = initialRoomPrice.clone();

				for (Triplet<Calendar, Calendar, Integer> typename:bookingList){
					if (m.compareTo((Calendar)typename.x) >= 0 && m.compareTo((Calendar)typename.y) <= 0){
						
						typeClashes[((Integer)typename.z)-1]++;
					}
				}
				
				for (RoomDiscounts j:disc){
					if (m.compareTo(j.s) >= 0 && m.compareTo(j.exc) <= 0){
						if (j.type==0){
							
							for (int i=0;i<NumRoom;i++){
								roomPrice[i] = roomPrice[i] * j.disc;	
							}
						}else{
							roomPrice[j.type-1] = roomPrice[j.type-1] * j.disc;
						}
					}
				}
				
				for (int i = 0;i<NumRoom;i++){
					if (typeClashes[i] > maximumRoomsBooked[i]){
						maximumRoomsBooked[i] = typeClashes[i];
					}
					if (roomPrice[i] > maximumRoomPrice[i]){
						maximumRoomPrice[i] = roomPrice[i];
					}
				}
				
				for (RoomStatusDTO j:listret) {
					j.addToTotalPrice(roomPrice[j.getRoomTypeid()-1]);
				}

				System.out.print(m.getTime() + " - " + typeClashes[0] + "," + 
				typeClashes[1] + ","+ typeClashes[2]+","+ typeClashes[3]
						+ ","+ typeClashes[4]);
				System.out.println( "==> " + roomPrice[0] +","+ roomPrice[1] +","+ roomPrice[2] +","+ roomPrice[3] +","+ roomPrice[4] +",");

				m.add(Calendar.DATE, 1);
			}
			

			
			ArrayList<RoomStatusDTO> toRemove = new ArrayList<RoomStatusDTO>();
			for (RoomStatusDTO j:listret){
				j.setMaximumRooms(j.getMaximumRooms() - maximumRoomsBooked[j.getRoomTypeid()-1]);
				j.setMaxPrice(maximumRoomPrice[j.getRoomTypeid() - 1]);
				
			}
			listret.removeAll(toRemove);
			
			statmnt.close();
		}catch(Exception exc){
			System.out.println("Exception found");
			exc.printStackTrace();
		}
		return listret;
	}

	private ArrayList<RoomDiscounts> getRoomDiscounts(String cty, Date ci, Date co,
			Statement statmnt) throws SQLException {
		ResultSet result;
		String query;
		ArrayList<RoomDiscounts> adjdisc =  new ArrayList<RoomDiscounts>();
		query = " select p.roomType AS ROOMTYPE, disc as disc, checkinDate, checkoutDate " +
			" from HotelDiscounts p left outer join Hotel h on (p.hotel=h.id) " +
			" where (UPPER(h.city) = '"+ cty.toUpperCase() +"' or h.city is null) and " +
			" (checkinDate <= '"+ dtf.format(co) + "' and checkoutDate >= '"+ dtf.format(ci) + "') " +
			" order by p.roomType";
		result = statmnt.executeQuery(query);
		while (result.next()){
			adjdisc.add(new RoomDiscounts(result.getDate("checkinDate"), result.getDate("checkoutDate"), 
					result.getInt("ROOMTYPE"), result.getDouble("disc")));
		}
		return adjdisc;
	}

	private void getRoomList(String cty, Statement statmnt,
		ArrayList<RoomStatusDTO> listret) throws SQLException {
		String query = "select rtype.id as RM_TYPE, rtype.typename AS RM_NAME, count(*) TOTAL, rtype.initialPrice AS PRICE, h.id as HTL_TYPE, h.city as HOTEL_NAME "
				+ "from room r join Hotel h on (r.hotelid=h.id) "
				+ "     join RoomType rtype on (r.roomTypeid=rtype.id) "
				+ "where UPPER(h.city) = '" + cty.toUpperCase() + "'"
				+ "group by rtype.id,rtype.typename,rtype.initialPrice,h.id, h.city " 
			   + "order by rtype.id";
		ResultSet result = statmnt.executeQuery(query);
		logger.info("The result set size is "+result.getFetchSize());
		while(result.next()){
			listret.add(new RoomStatusDTO(result.getInt("TOTAL"), result.getInt("HTL_TYPE"), result.getInt("RM_TYPE"),
					result.getString("RM_NAME"), result.getDouble("PRICE"), result.getString("HOTEL_NAME")));
		}
		result.close();
	}
		
	
	public ArrayList<StatusDTO> viewStatus(Date d){
		ArrayList<StatusDTO> listret = new ArrayList<StatusDTO>();
		Statement statmnt;
		try {
			statmnt = connection.createStatement();
			String query = "select h.city as city,r.roomTypeid as TYPE, count(*) as COUNTER" 
					+ " from Hotel h join room r on (h.id=r.hotelid) "
					+ " group by h.city, r.roomTypeid";
			ResultSet result = statmnt.executeQuery(query);
			logger.info("The result set size is "+result.getFetchSize());
			HashMap<String, StatusDTO> map = new HashMap<String, StatusDTO>();

			while(result.next()){
				int i = result.getInt("TYPE");
				String cty = result.getString("city");
				int tempon  = result.getInt("COUNTER");
				if (map.get(cty)==null){
					map.put(cty, new StatusDTO(cty));
				}
				map.get(cty).status[i-1].x = i;
				map.get(cty).status[i-1].y = tempon;
			}
			result.close();

			String q2 = "select h.city as city,r.roomTypeid as TYPE, count(*) as COUNTER "+
					" from Hotel h join room r on (h.id=r.hotelid) "+
					"    join BookingDetails bd on  (r.bookid=bd.id)"+
					" where bd.checkin <= '"+ dtf.format(d) +"' and bd.checkout >= '"+dtf.format(d)+"'"+
					" group by h.city, r.roomTypeid";
			result = statmnt.executeQuery(q2);

			while(result.next()){
				int i = result.getInt("TYPE");
				String cty = result.getString("city");
				int tempon  = result.getInt("COUNTER");
				map.get(cty).status[i-1].z = tempon;
			} 			
			
			result.close();
			statmnt.close();
			
			listret.addAll(map.values());
		} catch (SQLException exc) {
			
			exc.printStackTrace();
		}
		return listret;
	}
	
	
	public PairDef<Long, Integer> makeBooking(ArrayList<RoomStatusDTO> bookItemList, String creditcard, String emailID) {
		int consid = 0;
		consid = makeUser(creditcard, emailID);
		PreparedStatement statmnt;
		Random rand = new Random();
		int pin = rand.nextInt(9000) + 1000;
		
		long bkid = (long) (rand.nextFloat()*(9000000)+1000000);
		
		
		System.out.println("Booking id (bkid): " + bkid);
		System.out.println("Pin id: " + pin);
		
		String bookingStr = "insert into Booking (bkid, pin, consid)" +
								"Values (?, ?, ?)";
		try {
			statmnt = connection.prepareStatement(bookingStr);
			statmnt.setLong(1, bkid);
			statmnt.setInt(2, pin);
			statmnt.setInt(3, consid);
			statmnt.executeUpdate();
			statmnt.close();
		} catch(SQLException exc) {
			exc.printStackTrace();
		}
		
		for (RoomStatusDTO l: bookItemList) {
			while (!l.decNumRooms()) {
				if (l.decExtraBeds() != true) {
					makeBookingItem(l.getCiBooking(), l.getCoBooking(), bkid, l.getBookTotalPrice() + 35, true,l.getRoomTypeid(),l.getHotelid());
				} else {
					makeBookingItem(l.getCiBooking(), l.getCoBooking(), bkid, l.getBookTotalPrice(), false,l.getRoomTypeid(),l.getHotelid());
				}
			}
		}
		return new PairDef<Long, Integer>(bkid, pin);
	}
	
	
	
	public int makeBookingItem(Date checkin, Date checkout, long bookid, double price, boolean xbed, int rtype, int hotel) {
		
		PreparedStatement statmnt;
		ResultSet rslts = null;
		int id = 0;
		String bookItmLS = "insert into BOOKINGDETAILS (checkin, checkout, bookid, price, xbed,roomType,hotel)" +
								"Values (?, ?, ?, ?, ?, ?, ?)";
		try {
			statmnt = connection.prepareStatement(bookItmLS);
			statmnt.setString(1, dtf.format(checkin));
			statmnt.setString(2, dtf.format(checkout));
			statmnt.setLong(3, bookid);
			statmnt.setDouble(4, price);
			statmnt.setBoolean(5, xbed);
			statmnt.setInt(6, rtype);
			statmnt.setInt(7, hotel);
			statmnt.executeUpdate();
			statmnt.close();
		} catch (SQLException exc) {
			exc.printStackTrace();
		}
		return id;
	}
	
	public void makeRoom(int hotelid, int roomTypeid, int bookid) {
		String roomStr = "insert into room(hotelid, roomTypeid, bookid)" +
						"Values (?, ?, ?)";
		PreparedStatement statmnt;
		try {
			statmnt = connection.prepareStatement(roomStr);
			statmnt.setInt(1, hotelid);
			statmnt.setInt(2, roomTypeid);
			statmnt.setInt(3, bookid);
			statmnt.executeUpdate();
			statmnt.close();
		} catch(SQLException exc) {
			exc.printStackTrace();
		}
	}

	private int makeUser(String creditcard, String emailID) {
		String usersltstr = "insert into CONSUMER(creditcard, emailID)" +
						 "Values (?,?)";
		ResultSet rslts = null;
		PreparedStatement statmnt;
		int id = 0;
		try {
			statmnt = connection.prepareStatement(usersltstr, Statement.RETURN_GENERATED_KEYS);
			statmnt.setString(1, creditcard);
			statmnt.setString(2, emailID);
			statmnt.executeUpdate();
			rslts = statmnt.getGeneratedKeys();
			if (rslts.next()) {
				id = rslts.getInt(1);
			}else
				throw new SQLException("unable to create user");
			rslts.close();
			statmnt.close();
		} catch(SQLException z3) {
			z3.printStackTrace();
		}
		return id;
	}
		
	public void addNewDiscounts(double disc, Date s, Date exc,int hotel, int roomType){
		 
		String sqlStr = "insert into HotelDiscounts(disc,checkinDate,checkoutDate,hotel,roomType) "+
						"Values (?,?,?,?,?)";
		try {
			PreparedStatement statmnt = connection.prepareStatement(sqlStr);
			statmnt.setDouble(1, disc);
			statmnt.setString(2, dtf.format(s));
			statmnt.setString(3, dtf.format(exc));
			if (hotel!=0){
				statmnt.setInt(4, hotel);				
			}else{
				statmnt.setNull(4, java.sql.Types.INTEGER);
			}
			if (roomType!=0){
				statmnt.setInt(5, roomType);				
			}else{
				statmnt.setNull(5, java.sql.Types.INTEGER);
			}
			statmnt.executeUpdate();
			statmnt.close();
		} catch (SQLException z3) {
			z3.printStackTrace();
		}
	}
	
	public boolean verifyBookingPin(long id, int pin){
		Statement statmnt;
		try {
			statmnt = connection.createStatement();
			String query = "select bkid, pin from booking where bkid="+id+" and pin=" + pin;
			ResultSet result = statmnt.executeQuery(query);
			while(result.next()){
				return true;
			}
			statmnt.close();
		} catch (SQLException exc) {
			
			exc.printStackTrace();
		}
		return false;
	}
	
	public BookDTO getBookingbyID(long id){
		Statement statmnt;
		BookDTO k = new BookDTO(id);
		try {
			statmnt = connection.createStatement();
			String query = "select k.bkid AS BKID, bd.checkin as CHECKIN, bd.checkout as CHECKOUT, bd.roomType as RTYPE, h.city AS CTY,price as PRICE,bd.xbed AS EXTRA "
					+ "from booking k join BookingDetails bd on (k.bkid=bd.bookid) "
					+ "     join Hotel h on (bd.hotel=h.id) "
					+ "where bkid="+id;
			ResultSet result = statmnt.executeQuery(query);
			while(result.next()){
				Date checkin = result.getDate("checkin");
				Date checkout = result.getDate("checkout");
				int rtype = result.getInt("rtype");
				String cty = result.getString("cty");
				double pricePaid = result.getDouble("PRICE");
				boolean extra = result.getBoolean("EXTRA");
				k.addBookingInterface(rtype, pricePaid,cty,checkin,checkout,extra);
				System.out.println(cty + " - " + rtype + " : " + checkin+ " to " + checkout);
			}
			statmnt.close();
		} catch (SQLException exc) {
			
			exc.printStackTrace();
		}
		return k;
	}
	
	public void deleteBooking(long bki){
		Statement statmnt;
		try {
			statmnt = connection.createStatement();
			String query = "delete from BookingDetails where bookid = "+bki;
			statmnt.executeUpdate(query);
			query = "delete from booking where bkid = "+bki;
			statmnt.executeUpdate(query);
			statmnt.close();
		} catch (SQLException exc) {
			
			exc.printStackTrace();
		}

	}

	public class RoomDiscounts{
		public Calendar s = Calendar.getInstance();
		public Calendar exc = Calendar.getInstance();
		public int type = 0;
		public double disc = 1;

		public RoomDiscounts(Date ci, Date co, int type, double disc) {
			this.s.setTime(ci);
			this.exc.setTime(co);
			this.type = type;
			this.disc = disc;
		}
	}
	
	
}
