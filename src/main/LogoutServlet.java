package comp9321;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import comp9321.servlet.ServletClass;


public class LogoutServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	

	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String s = "";
		HttpSession session  = request.getSession();
		try{      
		    session.invalidate();   
		    System.out.println("invalidated");
		}
		catch (Exception sqle)	{
		    System.out.println("error UserValidateServlet message : " + sqle.getMessage());
		}
		RequestDispatcher reqd = request.getRequestDispatcher("/login.jsp");
		reqd.forward(request, response);
	}

}
