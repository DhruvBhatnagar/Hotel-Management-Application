package comp9321.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;

import javax.activity.InvalidActivityException;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.sun.xml.internal.ws.addressing.model.ActionNotSupportedException;

import comp9321.bean.RoomStatusDTO;
import comp9321.bean.BookDTO;
import comp9321.dao.DBConnectionFactory;
import comp9321.dao.CustomerDAO;
import comp9321.dao.ServiceLocatorException;
import comp9321.bean.Triplet;



public class BookingServlet extends HttpServlet {
	
	private CustomerDAO hotel;
	private Triplet<Integer,Integer,Integer> tripStr;

	
	
    public BookingServlet() throws ServletException, ServiceLocatorException {
        super();
		try {
			hotel = new CustomerDAO();
		} catch (SQLException exc) {
			
			throw new ServletException();
		}
	}

	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		PrintWriter pwout = response.getWriter();
		if (request.getParameter("id") == null){
			pwout.println("<html><body><h1>Invalid ID</h1></body></html>");
		}else{
			pwout.println("<html>");
			pwout.println("<body\">");
			pwout.println("<div align=\"center\">");
			pwout.println("<H1>Your Booking ID is"+request.getParameter("id")+"</H1>");
			pwout.println("<h3>Please enter your PIN here:</H3>");
			pwout.println("<form name=\"auth\" action=\"custLogin?id="+request.getParameter("id")+"\" method=\"POST\">"
					+ "4 digit pin: <input size=4 type=\"password\" name=\"pin\" autofocus pattern=\"[0-9]{4}\">\n"
					+ "<input type=\"submit\" value=\"Enter\">\n"
					+ "<input type=\"hidden\" name=\"id\" value=\"" + request.getParameter("id") + "\">\n"
					+ "<input type=\"hidden\" name=\"action\" value=\"loginAttempt\"/>\n"
					+ "</form>\n");
			if (request.getParameter("errors")!=null) pwout.println("<div style=\"color:blue\">"+request.getParameter("errors")+"</div>");
			pwout.println("</div>");
			pwout.println("</body>");
			pwout.println("</html>");
			pwout.close();
			
		}
	}

	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String action = request.getParameter("action");
		if (action.equals("loginAttempt")){
			System.out.println(request.getParameter("id"));
			System.out.println(request.getParameter("pin"));
			long id;
			try {
				id = Long.parseLong(request.getParameter("id"));
				int pin = Integer.parseInt(request.getParameter("pin"));
				if (hotel.verifyBookingPin(id, pin)){				
					request.setAttribute("confBooking", 0);
					getBookingItems(request, response, id);
				}else{
					throw new NumberFormatException();
				}
			}catch(NumberFormatException exc){
				response.sendRedirect("custLogin?id="+request.getParameter("id")+
						"&errors=ID/Pin combination invalid");
			}catch (ActionNotSupportedException exc){
				response.sendRedirect("custLogin?id="+request.getParameter("id")+
						"&errors=This booking is now locked");
			}
		}else if (action.equals("addToBooking")){
			BookDTO bvf = (BookDTO) request.getSession().getAttribute("bvf");
			if (bvf == null) {
				response.sendRedirect("custLogin?id="+request.getParameter("id")+
						"&errors=Session expired");
			}
			try{
				int numRooms = 0;
				int roomType = 1;
				int extraBeds = 0;
				if (request.getParameter("button").equals("Add Room to booking")){
					numRooms = Integer.parseInt(request.getParameter("numRooms"));
					roomType  = Integer.parseInt(request.getParameter("roomType"));
					extraBeds = Integer.parseInt(request.getParameter("extraBeds"));
					if (roomType == 1){
						extraBeds = 0;
					}
					tripStr = new Triplet<Integer, Integer,Integer>(numRooms, roomType,extraBeds);
				}else{
					if (request.getParameter("button").equals("Cancel")){
						request.setAttribute("confBooking", 0);
						getBookingItems(request, response, bvf.getbkid());
						return;
					}
					numRooms = tripStr.x;
					roomType = tripStr.y;
					extraBeds = tripStr.z;
					tripStr = null;
					DBConnectionFactory.sem.acquire();
				} 
				ArrayList<RoomStatusDTO> vacancies = hotel.getRoomsAvailable(bvf.getcity(), 1000, bvf.checkin, bvf.checkout);
				RoomStatusDTO toadd = null;
				
				for (RoomStatusDTO v:vacancies){
					if (v.getRoomTypeid() == roomType){
						System.out.println(v.getType() +" available : "+v.getMaximumRooms());
						toadd = v;
						if (v.getMaximumRooms() < numRooms){
							throw new ActionNotSupportedException("Excess rooms booked");
						}
					}
				}
				if (toadd == null){
					throw new ActionNotSupportedException("More rooms tried to be booked than available");
				}
				System.out.println("Rooms :"+ numRooms);
				assert bvf != null;
				if (request.getParameter("button").equals("Add Room to booking")){
					request.setAttribute("confMsg", "Your new booking will cost: $"
							+ toadd.getBookTotalPrice() + " per room (no extra beds) <br>"
									+ "Adding a total off $" + (toadd.getBookTotalPrice()*numRooms+extraBeds*35) + 
									" to the booking cost<br>"
											+ "If you are happy with this charge to your CC hit the \"Confirm addition of room(s)\" button below<br>");
					request.setAttribute("confBooking", 1);
				}else{
					for (;numRooms>0;numRooms--){
						if (extraBeds > 0){
							hotel.makeBookingItem(bvf.checkin,bvf.checkout,bvf.getbkid(),toadd.getBookTotalPrice()+35,true,toadd.getRoomTypeid(),toadd.getHotelid());									
						}else{
							hotel.makeBookingItem(bvf.checkin,bvf.checkout,bvf.getbkid(),toadd.getBookTotalPrice(),false,toadd.getRoomTypeid(),toadd.getHotelid());
						}
						extraBeds--;
					}
					request.setAttribute("confBooking", 0);
					DBConnectionFactory.sem.release();
				}
				getBookingItems(request, response, bvf.getbkid());
			}catch (NumberFormatException exc){
				getBookingItems(request, response, bvf.getbkid());
			}catch (ActionNotSupportedException exc){
				DBConnectionFactory.sem.release();
				request.setAttribute("confBooking", 0);
				request.setAttribute("errors", exc.getMessage());
				request.setAttribute("abilityToDropBooking", 1);
				getBookingItems(request, response, bvf.getbkid());
			} catch (InterruptedException exc) {
				// TODO Auto-generated catch block
				exc.printStackTrace();
			}
		}else if (action.equals("dropBooking")){
			BookDTO bvf = (BookDTO) request.getSession().getAttribute("bvf");
			if (bvf == null) {
				response.sendRedirect("custLogin?id="+request.getParameter("id")+
						"&errors=Session expired");
			}
			hotel.deleteBooking(bvf.getbkid());
		}
	}

	private BookDTO getBookingItems(HttpServletRequest request,
			HttpServletResponse response, long id) throws ServletException,
			IOException, ActionNotSupportedException{
		BookDTO bvf = (BookDTO) request.getSession().getAttribute("bvf");
		bvf = hotel.getBookingbyID(id);
		System.out.println("LOGIN SUCCESS");
		request.getSession().setAttribute("bvf", bvf);
		Date now = new Date();
		int days = (int) (((bvf.checkin.getTime()-now.getTime())/(1000 * 60 * 60 * 24)) + 1);
		System.out.println("DAYS LEFT = "+days);
		if (days <= 2){
			throw new ActionNotSupportedException("Booking locked");
		}
		RequestDispatcher reqd = request.getRequestDispatcher("/custLogin.jsp?id="+id);
		reqd.forward(request, response);
		return bvf;
	}
}
