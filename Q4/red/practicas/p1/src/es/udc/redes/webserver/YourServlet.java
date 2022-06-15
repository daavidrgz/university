package es.udc.redes.webserver;
import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;

/**
 * This class must be filled to complete servlets option (.do requests).
 */
public class YourServlet implements MiniServlet {
	

	public YourServlet(){
		
	}

	@Override
	public String doGet (Map<String, String> parameters){
		String name = parameters.get("name");
		String day = parameters.get("day");
		String month = parameters.get("month");
		String birthDate = day + " " + month + " ";

		return printHeader() + printBody(name, birthDate) + printEnd();
	}	

	private String printHeader() {
		return "<html><head> <title>Time to Birth</title> </head> ";
	}

	private String printBody(String name, String birthDate) {
		SimpleDateFormat format = new SimpleDateFormat("dd MMM yyyy");
		String str;

		try {
			int year = Calendar.getInstance().get(Calendar.YEAR);
			Date now = new Date();
			Date birthDay = format.parse(birthDate + year);

			long diffInTime = birthDay.getTime() - now.getTime();
			if ( diffInTime < 0 )
				diffInTime += 31556952000L; // Year in milliseconds

			long diffInSeconds = (diffInTime / 1000) % 60;
			long diffInMinutes = (diffInTime / (1000 * 60)) % 60;
			long diffInHours = (diffInTime / (1000 * 60 * 60)) % 24;
			long diffInDays = (diffInTime / (1000 * 60 * 60 * 24)) % 365;

			str = "Hi " + name + ", the time until your birthday is: " + diffInDays +
					" days, " + diffInHours + " hours, " + diffInMinutes + " minutes, " +
					diffInSeconds + " seconds";

		} catch (Exception e) {
			str = "Error: Incorrect date";
		}
		return "<body><h1>" + str + "</h1></body>";
	}

	private String printEnd() {
		return "</html>";
	}
}
