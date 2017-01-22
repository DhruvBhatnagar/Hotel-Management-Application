package comp9321.servlet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ErrorServlet extends ServletClass {
	private static final long serialVersionUID = 1L;

	@Override
	public String run(HttpServletRequest request, HttpServletResponse response) {
		return "/404.jsp";
	}

}
