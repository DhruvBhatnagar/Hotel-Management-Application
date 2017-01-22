package comp9321.bean;

public class StatusDTO {
	private String city;
	public Triplet[] status = new Triplet[5];
	
	public StatusDTO(String location){
		this.city = location;
		status = new Triplet[5];
		for (int i = 0; i  < 5; i++){
			status[i] = new  Triplet<Integer, Integer, Integer>(i+1, 0, 0); 
		}
	}

	public String getLocation() {
		return city;
	}

	public void setLocation(String location) {
		this.city = location;
	}
	
	public int getBooked(int i){
		return (Integer) status[i-1].x;
	}
	public int getTotal(int i){
		return (Integer) status[i-1].y;
	}
}
