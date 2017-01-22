package comp9321.servlet;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public abstract class ServletClass extends HttpServlet implements ServletInterface{
	private static final long serialVersionUID = 1L;

	public abstract String run(HttpServletRequest request, HttpServletResponse response);

	protected final void doGet (HttpServletRequest request, HttpServletResponse response) {
		System.out.println("doGet");
		run(request, response);
	}
	protected final void doPost (HttpServletRequest request, HttpServletResponse response) {
		System.out.println("doPost");
		run(request, response);
	}
}
