package comp9321.bean;

import java.util.ArrayList;

public class StatusChanges {
	private String city;
	private int tid;
	private ArrayList<StatusDTO> dtolist;
	
	public StatusChanges(String city, ArrayList<StatusDTO> occ){
		this.city = city;
		dtolist = occ;
	}

	public String getLocation() {
		return city;
	}

	public ArrayList<StatusDTO> getData() {
		return dtolist;
	}

	public int getTypeid() {
		return tid;
	}

	public void setTypeid(int tid) {
		this.tid = tid;
	}
	
	public int roomsPerType(int id){
		return dtolist.get(0).getTotal(id);
	}
	
}
