package comp9321.servlet;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import comp9321.OwnerServlet;
import comp9321.bean.*;

public class OccupancyServlet extends ServletClass {
	private static final long serialVersionUID = 1L;
	
	@Override
	public String run(HttpServletRequest request, HttpServletResponse response) {
		//System.out.println("occupancy() ");
		ArrayList<HotelBranchDTO> hotels = OwnerServlet.getStaff.selectAllHotelBranch();
		for (HotelBranchDTO h : hotels){
			int count = OwnerServlet.getStaff.countHotelOccupied(h.getId());
			h.setOccupied(count);
			count = OwnerServlet.getStaff.countHotelAvailable(h.getId());
			h.setAvailable(count);
		}	
		request.getSession().setAttribute("occupancy", hotels);
		return "/occupancy.jsp";
	}


}
