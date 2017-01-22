package comp9321.bean;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class BookingDTO implements Serializable {
	private static final long serialVersionUID = 1L;
		
	public class bookingInterface implements Serializable{
		private static final long serialVersionUID = 1L;
		protected String[] typesofRooms = {"Single","Twin","Queen","Executive","Suite"};
		protected int rtype;
		protected double amountPaid;
		protected boolean xtraBed;
		
		public bookingInterface(int rtype, double amountPaid,boolean xtraBed)  {
			this.rtype = rtype;
			this.amountPaid = amountPaid;
			this.xtraBed = xtraBed;
		}
		public int getRoomTypeId(){
			return rtype;
		}
		public String getType(){
			return typesofRooms[rtype-1];
		}
		public double getAmountPaid(){
			return amountPaid;
		}
		public String getXtraBed(){
			if (xtraBed){
				return "Y";
			}else{
				return "N";
			}
		}
		
	}
	
	private static DateFormat pdtf = new SimpleDateFormat("dd/MM/yyyy");
	private long id;
	private int cid;
	private Date checkin;
	private Date checkout;
	private String city;

	private String fname;
	private String lname;		
	
	//private ArrayList<RoomTypeDTO> bookedRooms;
	//private ArrayList<RoomDTO> roomList;
	private ArrayList<bookingInterface> bookItemList = new ArrayList<BookingDTO.bookingInterface>();


	public String getTotalPrice(){
		double total = 0;
		for (bookingInterface bi : bookItemList){
			total += bi.amountPaid;
		}
		return new DecimalFormat("#.##").format(total);
	}
	
	public BookingDTO(long id) {
		this.id = id;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public int getCid() {
		return cid;
	}

	public void setCid(int cid) {
		this.cid = cid;
	}

	public Date getCheckin() {
		return checkin;
	}

	public void setCheckin(Date checkin) {
		this.checkin = checkin;
	}

	public Date getCheckout() {
		return checkout;
	}

	public void setCheckout(Date checkout) {
		this.checkout = checkout;
	}

	public String getFname() {
		return fname;
	}

	public void setFname(String fname) {
		this.fname = fname;
	}

	public String getLname() {
		return lname;
	}

	public void setLname(String lname) {
		this.lname = lname;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}
	
	public ArrayList<bookingInterface> getBookItemList() {
		return bookItemList;
	}

	public void addBookingInterface(int rtype, double amountPaid,String city, Date checkin, Date checkout, boolean xtraBed){
		if (this.checkin != null){
			assert this.city.equals(city);
			assert this.checkin.equals(checkin);
			assert this.checkout.equals(checkout);
		} else {
			this.city = city;
			this.checkin = checkin;
			this.checkout = checkout;
		}
		bookItemList.add(new bookingInterface(rtype, amountPaid,xtraBed));
	}
		
	
}

