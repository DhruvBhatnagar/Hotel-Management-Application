package comp9321.bean;

import java.io.Serializable;

public class RoomDTO implements Serializable{
	private static final long serialVersionUID = 1L;
	private int id;
	private String roomTypeName;
	private String hotelBranch;
	private int bkid;
	public RoomDTO(int id, String roomtype, String hotelbranch) {
		this.id = id;
		this.roomTypeName = roomtype;
		this.hotelBranch = hotelbranch;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getRoomTypeName() {
		return roomTypeName;
	}
	public void setRoomTypeName(String roomTypeName) {
		this.roomTypeName = roomTypeName;
	}
	public String getHotelBranch() {
		return hotelBranch;
	}
	public void setHotelBranch(String hotelBranch) {
		this.hotelBranch = hotelBranch;
	}
	public int getBkid() {
		return bkid;
	}
	public void setBkid(int bkid) {
		this.bkid = bkid;
	}
	
}
