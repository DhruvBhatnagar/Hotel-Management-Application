package comp9321.servlet;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Util {
	public static boolean isNumeric(String str) {  
		try {  
			double d = Double.parseDouble(str);  
		} catch(NumberFormatException nfe) {
			return false;  
		}  
		return true;  
	}

	public static boolean checkDay (String days, String months, String years){
		int day = Integer.parseInt(days);
		int month = Integer.parseInt(months);
		int year = Integer.parseInt(years);
		boolean valid = false;
		if(day >=1){
			if ((month == 4 || month == 6 || month == 9 || month == 11) && day <= 30)
				valid = true;  
			if ((month == 1 || month == 3 || month == 5 || month == 7 || month == 8 || month == 10 || month == 12) && day <= 31)
				valid = true;

			if (month == 2) {
				if(day <= 28)
					valid = true;
				else if(day == 29)
					if ((year%4 == 0 && year%100!=0) || year%400 == 0) valid = true;

			}
		} 
		return valid;
	}

	public static boolean isValidDate (String d, String m, String y){
		boolean valid = false;
		SimpleDateFormat dmy = new SimpleDateFormat("dd-MM-yyyy");
		if (checkDay(d,m,y)){
			String fromdate = d+"-"+m+"-"+y;
			Date from = null;
			try {
				from = dmy.parse(fromdate);
			} catch (ParseException ex) { 
				System.out.println("Invalid date");
				ex.printStackTrace(); 
				return false;
			}
			valid = true;
		}
		return valid;
	}

	public static boolean isValidNumber (String amt){
		if (amt.equals("") || !isNumeric(amt)){
			System.out.println("Invalid number");
			return false;
		} else if (Double.parseDouble(amt) < 0 && isNumeric(amt)){
			System.out.println("Negative rate");
				return false;
		}
		return true;
	}

	public static Date String2Date (String d, String m, String y){
		SimpleDateFormat dmy = new SimpleDateFormat("dd-MM-yyyy");
		dmy.setLenient(false);
		String fromdate = d+"-"+m+"-"+y;
		Date from = null;
		try {
			from = dmy.parse(fromdate);
		} catch (ParseException ex) { 
			ex.printStackTrace(); 
		}
		dmy.format(from);
		return from;
	}

	public static int getDateDifferent(Date d1, Date d2) {
		int daysdiff=0;
		long diff = d2.getTime() - d1.getTime();
		long diffDays = diff / (24 * 60 * 60 * 1000);
		daysdiff = (int) diffDays;
		return daysdiff;
	}

}
