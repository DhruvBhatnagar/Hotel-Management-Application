package comp9321.servlet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface ServletInterface {
	public abstract String run(HttpServletRequest request, HttpServletResponse response);
}
