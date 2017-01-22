package comp9321.bean;


import java.io.Serializable;

public class HotelBranchDTO implements Serializable{
	private static final long serialVersionUID = 1L;
	int id;
	String name;
	String city;
	int occupied;
	int available;
	
	public HotelBranchDTO(int id, String name, String city){
		this.id = id;
		this.name = name;
		this.city = city;
	}
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public int getOccupied() {
		return occupied;
	}
	public void setOccupied(int occupied) {
		this.occupied = occupied;
	}
	public int getAvailable() {
		return available;
	}
	public void setAvailable(int available) {
		this.available = available;
	}
	
}
