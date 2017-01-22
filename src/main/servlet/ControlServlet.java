package comp9321.servlet;

import java.io.IOException;
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

import comp9321.bean.*;
import comp9321.servlet.*;
import comp9321.dao.*;
import comp9321.*;


/**
 * Servlet implementation class ControlServlet
 */
@WebServlet("/ControlServlet")
public class ControlServlet extends HttpServlet {       
	private static final long serialVersionUID = 1L;
	
	private CustomerDAO hotel;
	private DateFormat dtf = new SimpleDateFormat("MM/dd/yyyy");

	
	public ControlServlet() throws ServletException, ServiceLocatorException {       
		super();
		try {
			hotel = new CustomerDAO();
		} catch (SQLException exc) {
			
			throw new ServletException();
		}
	}

	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	
		response.sendRedirect("consumerSearch.jsp");
	}

	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		

		String here = request.getParameter("action");
		System.out.println("The request was received from :" + here);

		if (here != null && here.equalsIgnoreCase("search")){
			processSearch(request, response);
		} else if (here != null && here.equalsIgnoreCase("results")) {
			processResults(request, response);
		} else if (here != null && here.equalsIgnoreCase("checkout")){
			processCheckout(request, response);
		} else if (here != null && here.equalsIgnoreCase("backToSearch")){
			response.sendRedirect("consumerSearch.jsp");
		}
	}

	private void processCheckout(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		
		HttpSession sess = request.getSession();
		System.out.println("URL -> " + request.getLocalAddr());
		
		Boolean emsg = false;
		ArrayList<RoomStatusDTO> carryLists = (ArrayList<RoomStatusDTO>) sess.getAttribute("CarryDTO");
		if (carryLists == null) {
			response.sendRedirect("consumerSearch.jsp");
			return;
		}
		
		String creditcardinfo = request.getParameter("creditcardinfo");
		String emailID = request.getParameter("emailID");
		try {
			
			if (creditcardinfo.equals("")) {
				throw new ParseException("Please enter credit card information", 0);
			} else if (emailID.equals("") || !emailID.matches("[A-Za-z]+@[A-Za-z]+\\.[A-Za-z]+")) {
				throw new ParseException(" Enter your email information", 0);
			}
			System.out.println("creditcardinfo: " + creditcardinfo);
			System.out.println("emailID: " + emailID);
			PairDef<Long, Integer> infoU = null;
			try {
				DBConnectionFactory.sem.acquire();
				ArrayList<RoomStatusDTO> listTempo = hotel.getRoomsAvailable(carryLists.get(0).getH_name(), 1000, 
						carryLists.get(0).getCiBooking(), carryLists.get(0).getCoBooking());
				for(RoomStatusDTO newListItem: listTempo) {
					for (RoomStatusDTO roomsList: carryLists) {
						if (roomsList.getType() == newListItem.getType()) {
							if (roomsList.getBedsNum() > newListItem.getMaximumRooms()) {
								
								emsg = true;
							}
						}
					}
				}
				if (emsg == false) {
					infoU = hotel.makeBooking(carryLists, creditcardinfo, emailID);
				}
				DBConnectionFactory.sem.release();

				if (request.getParameter("emailConf") != null) {
			
					if (emsg == false){
						
						try{
							String sentTo = emailID;
							String emailSubject = "Booking Confirmation for BookingID " + infoU.x;
							String mailMessage = 
									"Hello, \r\n\r\n" +
											"Your Booking was suceessfully processed and your Booking Details are: \r\n\r\n" +
											"Booking id = " + infoU.x + "\r\n" +
											"Booking pin = " + infoU.y + "\r\n\r\n" +
											"Your Booking can be accessed via the following link: " +
											"http://localhost:8080/Assign2Merge/custLogin?id=" + infoU.x + "\r\n\r\n";
							StringBuffer contentMail = new StringBuffer(mailMessage);
							String str = contentMail.toString();
							SendEmail msender = new SendEmail(sentTo, emailSubject, str);
							
						}
						
						catch (Exception exc){
						
							System.out.println("Unable to send Email");
						}
					}
				}
				RequestDispatcher reqd = request.getRequestDispatcher("/bookingConfirmation.jsp");
				request.setAttribute("error", emsg);
				request.setAttribute("bookNumb", infoU.x);
				request.setAttribute("pin", infoU.y);
				response.setHeader("Refresh", "10; URL=./consumerSearch.jsp");
				reqd.forward(request, response);
			} catch (InterruptedException exc) {
				
				exc.printStackTrace();
				DBConnectionFactory.sem.release();
			}
		} catch (ParseException exc) {
			request.setAttribute("overallAmount", request.getParameter("tp"));
			RequestDispatcher reqd = request.getRequestDispatcher("/consumerCheckout.jsp");
			request.setAttribute("errors", exc.getMessage());
			request.setAttribute("list", carryLists);
			reqd.forward(request, response);
		}
	}

	private void processResults(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		
		int xi = 0;
		int nTypes = 5;
		int[] nBeds = {0,0,0,0,0};
		int[] eBeds = {0,0,0,0,0};
		HttpSession sess = request.getSession();
		ArrayList <RoomStatusDTO> carryLists = (ArrayList<RoomStatusDTO>) sess.getAttribute("CarryDTO");
		if (carryLists == null) {
			response.sendRedirect("consumerSearch.jsp");
			return;
		}
		
		int totalBeds = 0;
		int nRoom = Integer.parseInt(request.getParameter("numRooms"));
		
		if (request.getParameter("single") != null) nBeds[0] = Integer.parseInt(request.getParameter("single"));
		if (request.getParameter("twin") != null) nBeds[1] = Integer.parseInt(request.getParameter("twin"));
		if (request.getParameter("queen") != null) nBeds[2] = Integer.parseInt(request.getParameter("queen"));
		if (request.getParameter("executive") != null) nBeds[3] = Integer.parseInt(request.getParameter("executive"));
		if (request.getParameter("suite") != null) nBeds[4] = Integer.parseInt(request.getParameter("suite"));

		if (request.getParameter("single_extra_beds") != null) eBeds[0] = Integer.parseInt(request.getParameter("single_extra_beds"));
		if (request.getParameter("twin_extra_beds") != null) eBeds[1] = Integer.parseInt(request.getParameter("twin_extra_beds"));
		if (request.getParameter("queen_extra_beds") != null) eBeds[2] = Integer.parseInt(request.getParameter("queen_extra_beds"));
		if (request.getParameter("executive_extra_beds") != null) eBeds[3] = Integer.parseInt(request.getParameter("executive_extra_beds"));
		if (request.getParameter("suite_extra_beds") != null) eBeds[4] = Integer.parseInt(request.getParameter("suite_extra_beds"));

		try {
			for(xi=0; xi < nTypes; xi++) {
				totalBeds = totalBeds + nBeds[xi];
				if (nBeds[xi] < eBeds[xi]) {
					throw new ParseException("Too many extra beds selected", 0);
				}
			}	
			if (totalBeds != nRoom) {
				throw new ParseException("No. of rooms requested: " + nRoom + 
						"<br>But you booked: " + totalBeds, 0);
			}
			ArrayList<RoomStatusDTO> removeRooms = new ArrayList<RoomStatusDTO>();
			double price = 0;
			for (RoomStatusDTO l: carryLists) {
				l.setBedsNum(nBeds[l.getRoomTypeid()-1]);
				l.setXtra_bed(eBeds[l.getRoomTypeid()-1]);
				if (l.getBedsNum()==0) removeRooms.add(l);
				l.setBookPrice(l.getBookTotalPrice() * l.getBedsNum() + 35 * l.getXtra_bed());
				price += l.getBookPrice();
			}
			carryLists.removeAll(removeRooms);
			
			request.setAttribute("overallAmount", price);
			RequestDispatcher reqd = request.getRequestDispatcher("/consumerCheckout.jsp");
			reqd.forward(request, response);
		} catch (ParseException exc) {
			RequestDispatcher reqd = request.getRequestDispatcher("/searchResults.jsp");
			request.setAttribute("errors", exc.getMessage());
			request.setAttribute("list", carryLists);
			request.setAttribute("numRooms", nRoom);
			request.setAttribute("city", request.getParameter("city"));
			reqd.forward(request, response);
		}
	}

	private void processSearch(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		double maxPrice = 10000000;
		try {
			if (!request.getParameter("maxPrice").equals("")){
				maxPrice= Double.parseDouble(request.getParameter("maxPrice"));
			}
			Date sess = dtf.parse(request.getParameter("checkInDate").toString());
			Date exc = dtf.parse(request.getParameter("checkOutDate").toString());
			int nRoom = Integer.parseInt(request.getParameter("numRooms"));
			int maxNumRooms = 0;
			
			Calendar cal = Calendar.getInstance();
			cal.add(Calendar.DATE, -1);
			Date date = cal.getTime();

			if (sess.after(exc)){
				throw new ParseException("Illegal Checkin Date, Checkin Date has to be before the Checkout Date.", 0);
			} else if (sess.before(date)) {
				throw new ParseException("That Day has already passed, please enter another date", 0);
			}
			ArrayList<RoomStatusDTO> carryLists = 
					hotel.getRoomsAvailable(request.getParameter("city"),1000,sess, exc);
			ArrayList<RoomStatusDTO> toremove = new ArrayList<RoomStatusDTO>();

			for (RoomStatusDTO l: carryLists){
				System.out.println(l.getType() + " - " + l.getMaximumRooms() + " $" + l.getMaxPrice());
				if (l.getMaxPrice() > maxPrice || l.getMaximumRooms() == 0){
					toremove.add(l);
				}else{
					maxNumRooms = maxNumRooms + l.getMaximumRooms();					
				}
				
			}
			carryLists.removeAll(toremove);

			if (maxNumRooms < nRoom) {
				carryLists = null;
				request.setAttribute("errors", "Enough Rooms are not currently Available in this City. " + maxNumRooms + " are currently available and not the requested " + nRoom + " rooms ");
			}
			RequestDispatcher reqd = request.getRequestDispatcher("/searchResults.jsp");
			request.setAttribute("list", carryLists);
			request.setAttribute("city", request.getParameter("city"));
			request.setAttribute("numRooms", nRoom);
			reqd.forward(request, response);
		} catch (ParseException exc) {
			RequestDispatcher reqd = request.getRequestDispatcher("/consumerSearch.jsp");
			request.setAttribute("errors", exc.getMessage());
			System.out.println("An Error was found");
			reqd.forward(request, response);
		} catch(NumberFormatException exc){
			RequestDispatcher reqd = request.getRequestDispatcher("/consumerSearch.jsp");
			request.setAttribute("errors", "The MaxPrice entered is invalid, a numerical value has to be entered.");
			System.out.println("An Error was found");
			reqd.forward(request, response);
		}
	}

}
