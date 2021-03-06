package comp9321;



import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.logging.Logger;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import comp9321.dao.StaffDAO;
import comp9321.servlet.*;
import comp9321.dao.*;

/**
 * Servlet implementation class controlservlet
 */

public class StaffServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
    private HashMap<String, ServletInterface> servlets;
    public static StaffDAO getStaff;
    static Logger logger = Logger.getLogger(StaffServlet.class.getName());
    /**
     * @throws ServletException 
     * @see HttpServlet#HttpServlet()
     */
    public StaffServlet() throws ServletException {
        super();
        
    	try {
    		getStaff = new StaffDAO();
    	} catch (ServiceLocatorException e) {
			logger.severe("Trouble connecting to database "+e.getStackTrace());
			throw new ServletException();
		} catch (SQLException e) {
			logger.severe("Trouble connecting to database "+e.getStackTrace());
			throw new ServletException();
		}
	
    	servlets = new HashMap<String, ServletInterface>();
    	
    	servlets.put("404", new ErrorServlet());
    	SelectRoomServlet s = new SelectRoomServlet();
    	servlets.put("selectroomforbooking", s);
    	servlets.put("checkoutroom", s);
    	servlets.put("selectroom", s);
    	BookingListServlet l = new BookingListServlet();
    	servlets.put("backtobookings", l);
    	servlets.put("booklist", l);
    	//servlets.put("logout", new LogoutServlet());
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	
		ServletInterface serv = findServlet(request);
		String page = serv.run(request, response);
		RequestDispatcher dispatcher = getServletContext().getRequestDispatcher(page);
		dispatcher.forward(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		ServletInterface serv = findServlet(request);
		String page = serv.run(request, response);
		RequestDispatcher dispatcher = getServletContext().getRequestDispatcher(page);
		dispatcher.forward(request, response);
	}
	
	private ServletInterface findServlet (HttpServletRequest request){
		ServletInterface servlet = servlets.get(request.getParameter("action"));
		if (servlet == null)
			servlet = servlets.get("404");
		return servlet;

	}

}
