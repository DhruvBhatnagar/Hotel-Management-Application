package comp9321;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.sun.xml.internal.ws.addressing.model.ActionNotSupportedException;

import comp9321.bean.*;
import comp9321.dao.*;
import comp9321.servlet.*;

@WebServlet(urlPatterns={"/custLoginController","/custLogin"})


public class CustomerServlet extends HttpServlet {       
	private static final long serialVersionUID = 1L;
	
	private CustomerDAO hotel;
	private Triplet<Integer,Integer,Integer> tripStr;

	
	
    public CustomerServlet() throws ServletException, ServiceLocatorException {
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
			pwout.println("<H2>Booking ID: "+request.getParameter("id")+"</H2>");
			
			pwout.println("<form name=\"auth\" action=\"custLogin?id="+request.getParameter("id")+"\" method=\"POST\">"
					+ "Enter PIN: <input size=4 type=\"password\" name=\"pin\" autofocus pattern=\"[0-9]{4}\">\n"
					+ "<input type=\"submit\" value=\"Enter\">\n"
					+ "<input type=\"hidden\" name=\"id\" value=\"" + request.getParameter("id") + "\">\n"
					+ "<input type=\"hidden\" name=\"action\" value=\"loginAttempt\"/>\n"
					+ "</form>\n");
			if (request.getParameter("errors")!=null) pwout.println("<div style=\"color:black\">"+request.getParameter("errors")+"</div>");
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
						"&errors=ID/PIN is invalid");
			}catch (ActionNotSupportedException exc){
				response.sendRedirect("custLogin?id="+request.getParameter("id")+
						"&errors=This booking can no longer be changed");
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
					throw new ActionNotSupportedException("Not enough rooms are available");
				}
				System.out.println("Rooms :"+ numRooms);
				assert bvf != null;
				if (request.getParameter("button").equals("Add Room to booking")){
					request.setAttribute("confMsg", "The Price of your Booking is now increased by: $"
							+ toadd.getBookTotalPrice() + " per room (No extra beds added) <br>"
									+ "This will add a total of $" + (toadd.getBookTotalPrice()*numRooms+extraBeds*35) + 
									" to the your Booking<br>"
											+ "If you would like to proceed with the change please click the confirmation button below<br>");
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
		System.out.println("Successfully Logged in");
		request.getSession().setAttribute("bvf", bvf);
		Date now = new Date();
		int days = (int) (((bvf.checkin.getTime()-now.getTime())/(1000 * 60 * 60 * 24)) + 1);
		System.out.println("Booking can only be changed for the next "+days*24+  " hours");
		if (days <= 2){
			throw new ActionNotSupportedException("Booking locked");
		}
		RequestDispatcher reqd = request.getRequestDispatcher("/custLogin.jsp?id="+id);
		reqd.forward(request, response);
		return bvf;
	}
}
