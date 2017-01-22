package comp9321.servlet;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import comp9321.OwnerServlet;
import comp9321.StaffServlet;
import comp9321.bean.*;

public class BookingListServlet extends ServletClass {
	private static final long serialVersionUID = 1L;
	//welcome --> owner
	@Override
	public String run(HttpServletRequest request, HttpServletResponse response) {
		
		if (request.isUserInRole("staff")){
			System.out.println("Logged on as staff!");
		}
		List<BookingDTO> list = StaffServlet.getStaff.selectAllBooking();
		request.getSession().setAttribute("Booking", list);
		List<RoomDTO> OccupiedRooms = StaffServlet.getStaff.selectAllRoomsAssigned();
		request.getSession().setAttribute("OccupiedRooms", OccupiedRooms);
		return "/bookinglist.jsp";
	}


}
