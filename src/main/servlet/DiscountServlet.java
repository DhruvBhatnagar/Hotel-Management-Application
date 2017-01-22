package comp9321.servlet;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import comp9321.OwnerServlet;
import comp9321.bean.*;

public class DiscountServlet extends ServletClass {
	private static final long serialVersionUID = 1L;

	@Override
	public String run(HttpServletRequest request, HttpServletResponse response) {
		//System.out.println("occupancy() ");
		if (request.getParameter("action").equals("editdiscount") || 
				request.getParameter("action").equals("canceldiscount")){
			System.out.println("back to discount discount");
			//room class
			request.getSession().setAttribute("invaliddate", true);
			request.getSession().setAttribute("invalidnumber", true);
	
			List<RoomTypeDTO> roomtypes = OwnerServlet.getStaff.selectRoomTypes();
			request.getSession().setAttribute("allRoomTypes", roomtypes);
			
			List<HotelBranchDTO> hotels =  OwnerServlet.getStaff.selectAllHotelBranch();
			request.getSession().setAttribute("allHotels",hotels);
			return "/discount.jsp";
		} else if (request.getParameter("action").equals("submitdiscount")) {
			boolean validdate = true;
			boolean validno = true;
			int roomType = Integer.parseInt(request.getParameter("class"));
			int hotel = Integer.parseInt(request.getParameter("hotel"));
			String discountType = request.getParameter("discounttype");
			String discountname = request.getParameter("discountname");
			//discount rate
			double rate = 0;
			String amt = request.getParameter("amt");
			
			
			
			if (Util.isValidNumber(amt)) rate = Float.parseFloat(request.getParameter("amt"));
			else validno = false;
			//to
			String td = request.getParameter("td");
			String tm = request.getParameter("tm");
			String ty = request.getParameter("ty");	
			Date to = null;
			SimpleDateFormat dmy = new SimpleDateFormat("dd-MM-yyyy");
			if (!Util.isValidDate(td,tm,ty)) validdate = false;
			else to = Util.String2Date(td,tm,ty);

			//from
			String fd = request.getParameter("fd");
			String fm = request.getParameter("fm");
			String fy = request.getParameter("fy");	
			Date from = null;
			if (!Util.isValidDate(fd,fm,fy)) validdate = false;
			else from = Util.String2Date(fd,fm,fy);

			int diff = 0;
			if (validdate){
				diff = Util.getDateDifferent(from, to);
				if (diff < 1) {
					System.out.println("Negative difference");
					validdate = false;
				}
			}

			if (validdate && validno){


				request.getSession().setAttribute("invaliddate", true);
				request.getSession().setAttribute("invalidnumber", true);
				DiscountDTO discount = new DiscountDTO(discountname,roomType, discountType, rate, from, to);
				discount.setHotelid(hotel);
				discount.setDatediff(diff);
				String tname = OwnerServlet.getStaff.selectRoomTypeName(roomType);
				discount.setTname(tname);
				request.getSession().setAttribute("discountdetail", discount);
				return "/confirmdiscount.jsp";
			} else {
				request.getSession().setAttribute("invaliddate", validdate);
				request.getSession().setAttribute("invalidnumber", validno);
				return "/discount.jsp";

			}
		} else if (request.getParameter("action").equals("confirmdiscount")){
			Object d = request.getSession().getAttribute("discountdetail");
			if (d != null){
				
				DiscountDTO discount = (DiscountDTO)d;
				OwnerServlet.getStaff.addDiscount(discount);
				//System.out.println(discount.getDatediff() + " " + discount.getFrom()+" "+ discount.getTo() + " roomid " + discount.getRoomtypeid() + "hotelid "+ discount.getHotelid() + discount.getRate());
			}
			return "/adddiscount.jsp";
		}

		return "/404.jsp";
	}


}
