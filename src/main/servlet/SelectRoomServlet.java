package comp9321.servlet;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import comp9321.StaffServlet;
import comp9321.bean.*;
import comp9321.bean.BookingDTO.bookingInterface;

public class SelectRoomServlet extends ServletClass {
	private static final long serialVersionUID = 1L;
	//owner--> selectroomtobooking
	@Override
	public String run(HttpServletRequest request, HttpServletResponse response) {


		int bookingid = Integer.parseInt(request.getParameter("selectedbookingid"));
		System.out.println("updated: selected booking id " + bookingid);
		if (request.getParameter("action").equals("checkoutroom")){

			int roomid = Integer.parseInt(request.getParameter("checkoutroomid"));
			StaffServlet.getStaff.clearRoomStatus(roomid);
			System.out.println("action is checkoutroom for roomid: "+roomid);

		} else if (request.getParameter("action").equals("selectroom")){
			int roomid = Integer.parseInt(request.getParameter("selectedroom"));
			StaffServlet.getStaff.updateRoomStatus(bookingid, roomid, "unavailable");
			System.out.println("action is selectroom for "+roomid);
		}

		System.out.println("selected booking id " + bookingid);

		List<RoomDTO> roomlist = new ArrayList<RoomDTO>();

		BookingDTO booking = StaffServlet.getStaff.selectBookingDetails(bookingid);
		//List<RoomTypeDTO> roomsrequired = StaffServlet.getStaff.selectRoomsNeededFromBooking(bookingid);
		List<RoomDTO> roomsassigned = StaffServlet.getStaff.selectRoomsAssigned(bookingid);

		int fill = 0;
		int max = 0;
		//---to do: change loop to use DTO's
		for (int typeid = 1; typeid <= 5; typeid++ ){	
			int qtyneeded = StaffServlet.getStaff.countNeededRoomTypes(bookingid, typeid);
			if (qtyneeded > 0) {
				int qtyreserved = StaffServlet.getStaff.countRoomTypeReserved(bookingid, typeid);
				System.out.println(qtyreserved + " / "+qtyneeded + " for "+ typeid);
				System.out.println("City::: " + booking.getCity());
				if (qtyreserved < qtyneeded){
				
					List<RoomDTO> list = StaffServlet.getStaff.selectAvailableRoomsWithRoomType(typeid, booking.getCity());
					roomlist.addAll(list);
				} else {
					fill++;
				}
				max++;
			}
		} 

		boolean allroomsassigned = false;
		boolean noroomsavailable = false;
		if (fill == max) {
			System.out.println("all rooms selected");
			allroomsassigned = true;
		} else if (roomlist.isEmpty()){
			System.out.println("no rooms available");
			noroomsavailable = true;
		}

		request.getSession().setAttribute("allroomsassigned", allroomsassigned);
		request.getSession().setAttribute("noroomsavailable", noroomsavailable);
		//pass these to every subsequent page

		//request.getSession().setAttribute("selectedbookingid", bookingid);
		request.getSession().setAttribute("selectedbooking", booking);

		request.getSession().setAttribute("RoomsAssigned", roomsassigned);
		request.getSession().setAttribute("SelectedAvailableRooms", roomlist);
		return "/selectroomtobooking.jsp";
	}


}
