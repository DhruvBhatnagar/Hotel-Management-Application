package comp9321.bean;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import comp9321.bean.BookDTO;
import comp9321.bean.BookDTO.bookingInterface;

public class BookDTO {
	private static final long serialVersionbookid = 1L;

	public class bookingInterface{
		protected String[] typesofRooms = {"Single","Twin","Queen","Executive","Suite"};
		protected int rtype;
		protected double amountPaid;
		protected boolean xtraBed;
		
		public bookingInterface(int rtype, double amountPaid,boolean xtraBed) {
			this.rtype = rtype;
			this.amountPaid = amountPaid;
			this.xtraBed = xtraBed;
		}
		
		public String getType(){
			return typesofRooms[rtype-1];
		}
		public double getAmountPaid(){
			return amountPaid;
		}
		public String getXtraBed(){
			if (xtraBed){
				return "Yes";
			}else{
				return "No";
			}
		}
	}

	private static DateFormat pdtf = new SimpleDateFormat("dd/MM/yyyy");

	long bkid;
	private String city = null;
	public Date checkin = null;
	public Date checkout = null;
	private ArrayList<bookingInterface> bookItemList = new ArrayList<BookDTO.bookingInterface>();

	public BookDTO(){}
	
	public BookDTO(long bkid){
		this.bkid = bkid;
	}
	
	public long getbkid(){
		return bkid;
	}
	public String getcity() {
		return city;
	}

	public String getCheckin() {
		return pdtf.format(checkin);
	}

	public String getCheckout() {
		return pdtf.format(checkout);
	}
	
	public ArrayList<bookingInterface> getbookItemList(){
		return bookItemList;
	}
	
	public String getTotalPrice(){
		double total = 0;
		for (bookingInterface bi : bookItemList){
			total += bi.amountPaid;
		}
		return new DecimalFormat("#.##").format(total);
	}

	public void addBookingInterface(int rtype, double amountPaid,String city, Date checkin, Date checkout, boolean xtraBed){
		if (this.checkin != null){
			assert this.city.equals(city);
			assert this.checkin.equals(checkin);
			assert this.checkout.equals(checkout);
		}else{
			this.city = city;
			this.checkin = checkin;
			this.checkout = checkout;
		}
		bookItemList.add(new bookingInterface(rtype, amountPaid,xtraBed));
	}
}