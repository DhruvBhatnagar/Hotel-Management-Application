package comp9321.bean;

import java.text.SimpleDateFormat;
import java.util.Date;

import comp9321.servlet.Util;

public class DiscountDTO {
	int roomtypeid;
	String name;
	String tname;
	String discounttype;
	double rate;
	Date from;
	Date to;
	String sfrom;
	String sto;
	int datediff;
	int hotelid;
	
	
	
	public String getDiscounttype() {
		return discounttype;
	}

	public void setDiscounttype(String discounttype) {
		this.discounttype = discounttype;
	}
	
	public double getRate() {
		return rate;
	}

	public void setRate(double rate) {
		this.rate = rate;
	}

	public String getTname() {
		return tname;
	}

	public void setTname(String tname) {
		this.tname = tname;
	}

	public DiscountDTO(String discountname, int roomtypeid, String type, double rate, Date from, Date to){
		this.name = discountname;
		this.roomtypeid = roomtypeid;
		this.discounttype = type;
		this.rate = rate;
		this.from = from;
		this.to = to;
		SimpleDateFormat dmy = new SimpleDateFormat("dd-MM-yyyy");
		this.sfrom = dmy.format(from);
		this.sto = dmy.format(to);	
	}
	
	public int getRoomtypeid() {
		return roomtypeid;
	}
	public void setRoomtypeid(int roomtypeid) {
		this.roomtypeid = roomtypeid;
	}
	
	public Date getFrom() {
		return from;
	}
	public void setFrom(Date from) {
		this.from = from;
	}
	public Date getTo() {
		return to;
	}
	public void setTo(Date to) {
		this.to = to;
	}

	public String getSfrom() {
		return sfrom;
	}

	public void setSfrom(String sfrom) {
		this.sfrom = sfrom;
	}

	public String getSto() {
		return sto;
	}

	public void setSto(String sto) {
		this.sto = sto;
	}

	public int getDatediff() {
		return datediff;
	}

	public void setDatediff(int datediff) {
		this.datediff = datediff;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getHotelid() {
		return hotelid;
	}

	public void setHotelid(int hotelid) {
		this.hotelid = hotelid;
	}

}
