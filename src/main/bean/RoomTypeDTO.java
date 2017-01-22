package comp9321.bean;
import java.io.Serializable;
import java.util.Date;

public class RoomTypeDTO implements Serializable {
	private static final long serialVersionUID = 1L;
	int id;
	String tname;
	int pricepaid;
	int qty;
	int qtyreserved;
	
	public RoomTypeDTO(int id, String tname, int price, int qty) {
		this.id = id;
		this.tname = tname;
		this.pricepaid = price;
		this.qty = qty;

	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getTname() {
		return tname;
	}
	public void setTname(String tname) {
		this.tname = tname;
	}
	public int getPricepaid() {
		return pricepaid;
	}
	public void setPricepaid(int pricepaid) {
		this.pricepaid = pricepaid;
	}
	public int getQty() {
		return qty;
	}
	public void setQty(int qty) {
		this.qty = qty;
	}
	public int getQtyreserved() {
		return qtyreserved;
	}
	public void setQtyreserved(int qtyreserved) {
		this.qtyreserved = qtyreserved;
	}
	
		
}
