package comp9321.bean;

import java.io.Serializable;
import java.util.Date;

public class RoomStatusDTO implements Serializable {
    private static final long serialVersionUID = 1L;
	private int maximumRooms; 
	private String Type;
	private int roomTypeid;
	private double maxPrice;
	private Date ciBooking;
	private Date coBooking;
	private double bookTotalPrice = 0;
	private int bedsNum;
	private int xtra_bed;
	private int hotelid;
	private String h_name;
	private double bookPrice; 
	
	public RoomStatusDTO(int maximumRooms, int hotelid, int roomTypeid, String type, double price, String h_name) {
		super();
		this.maximumRooms = maximumRooms;
		this.roomTypeid = roomTypeid;
		this.hotelid = hotelid;
		this.Type = type;
		this.maxPrice = price;
		this.h_name = h_name;
	}
	
	public void addToTotalPrice(double ppd){
		this.setBookTotalPrice(this.getBookTotalPrice() + ppd);
	}

	public int getNumDaysOfBooking(){
		return (int) ((coBooking.getTime()-ciBooking.getTime())/(1000 * 60 * 60 * 24)) + 1;
	}
	
	
	
	public boolean decExtraBeds() {
		boolean isZero = false;
		if (this.xtra_bed == 0) {
			isZero = true;
		} else {
			this.xtra_bed--;
		}
		return isZero;
	}



	public int getMaximumRooms() {
		return maximumRooms;
	}



	public void setMaximumRooms(int maximumRooms) {
		this.maximumRooms = maximumRooms;
	}

	public boolean decNumRooms() {
		boolean isZero = false;
		if (this.bedsNum == 0) {
			isZero = true;
		} else {
			this.bedsNum--;
		}
		return isZero;
	}

	public int getRoomTypeid() {
		return roomTypeid;
	}



	public void setRoomTypeid(int roomTypeid) {
		this.roomTypeid = roomTypeid;
	}



	public double getMaxPrice() {
		return maxPrice;
	}



	public void setMaxPrice(double maxPrice) {
		this.maxPrice = maxPrice;
	}



	public String getType() {
		return Type;
	}



	public void setType(String type) {
		Type = type;
	}



	public double getBookTotalPrice() {
		return bookTotalPrice;
	}



	public void setBookTotalPrice(double bookTotalPrice) {
		this.bookTotalPrice = bookTotalPrice;
	}



	public int getHotelid() {
		return hotelid;
	}



	public void setHotelid(int hotelid) {
		this.hotelid = hotelid;
	}



	public String getH_name() {
		return h_name;
	}



	public void setH_name(String h_name) {
		this.h_name = h_name;
	}



	public double getBookPrice() {
		return bookPrice;
	}



	public void setBookPrice(double bookPrice) {
		this.bookPrice = bookPrice;
	}



	public Date getCiBooking() {
		return ciBooking;
	}



	public void setCiBooking(Date ciBooking) {
		this.ciBooking = ciBooking;
	}



	public Date getCoBooking() {
		return coBooking;
	}



	public void setCoBooking(Date coBooking) {
		this.coBooking = coBooking;
	}



	public int getXtra_bed() {
		return xtra_bed;
	}



	public void setXtra_bed(int xtra_bed) {
		this.xtra_bed = xtra_bed;
	}



	public int getBedsNum() {
		return bedsNum;
	}



	public void setBedsNum(int bedsNum) {
		this.bedsNum = bedsNum;
	}



	

	
}
