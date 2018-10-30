package servlets;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Calendar;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.ArrayList;

/**
 *
 * @author adriannesvik
 */
@WebServlet(name = "serv_Calendar", urlPatterns = {"/serv_Calendar"})
public class serv_Calendar extends HttpServlet {
    
    // Creates a date formats
    SimpleDateFormat mf = new SimpleDateFormat("MMMM", Locale.US);
    SimpleDateFormat hm = new SimpleDateFormat("HH:mm");
    SimpleDateFormat wd = new SimpleDateFormat("EEEE", Locale.US);
    SimpleDateFormat dt = new SimpleDateFormat("dd.MM");
    
    // Creates calendar object based on host timezone
    Calendar calendar = Calendar.getInstance();
    
    // Assigns month, year, currentday(day of the month) and day(day of the week) to integers from calendar
    int month = calendar.get(Calendar.MONTH);
    int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
    int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
    
    // ArrayList for days(days of the month and weekdays(days of the week)
    ArrayList<Integer> days = new ArrayList<>();
    ArrayList<String> weekdays = new ArrayList<>();

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {

            // Add elements to weekdays ArrayList
            weekdays.add("Monday");
            weekdays.add("Tuesday");
            weekdays.add("Wednesday");
            weekdays.add("Thursday");
            weekdays.add("Friday");
            weekdays.add("Saturday");
            weekdays.add("Sunday");

            // HTML initialization and link to css
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<link rel=\"stylesheet\" type=\"text/css\" href=\"theme.css\">");
            out.println("<title>Servlet Calendar</title>");            
            out.println("</head>");
            out.println("<body>");

            // Calendar and timetable wrapper for css purposes
            out.println("<div class=\"calendarWrapper\">");
            
            // For loop prints out multiple calendars with different months
            out.println("<div class=\"Calendars\">");
            for(int i = 0; i < 3; i++) {
            
                // Prints month
                out.println("<div class=\"month\">");
                out.println("<h1>" + (mf.format(calendar.getTime())) + "</h1>");
                out.println("</div>");
            
                // Prints weekdays by iterating through ArrayList weekdays
                out.println("<ul class=\"weekdays\">");

                for(int w = 0; w <= 6; w++) {
                    // If-else statement for highlighting current day of the week in CSS
                    if(w == dayOfWeek-2 && month == calendar.get(Calendar.MONTH)) {
                        out.println("<li class=\"thisDay\">" + weekdays.get(w).substring(0,2) + "</li>");
                    }
                    else {
                        out.println("<li>" + weekdays.get(w).substring(0,2) + "</li>");
                    }
                }
                out.println("</ul>");
                
                out.println("<div class=\"daysDiv\">");
                out.println("<ul class=\"days\">");
                
                // Fix printing wrong startpoint for second calendar
                calendar.add(Calendar.MONTH, -1);
                int lastMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH) - calendar.get(Calendar.DAY_OF_WEEK);
                calendar.add(Calendar.MONTH, +1);
                
                calendar.set(Calendar.DAY_OF_MONTH, 1);
                
                // Prints spacing before first day of month
                for(int q = 2; q < calendar.get(Calendar.DAY_OF_WEEK); q++) {
                        out.println("<li class=\"Empty\">" + (lastMonth + q) + "</li>");
                }

                // Prints all days of month
                while(calendar.get(Calendar.DAY_OF_MONTH) != calendar.getActualMaximum(Calendar.DAY_OF_MONTH)) {
                    if(calendar.get(Calendar.DAY_OF_MONTH) == dayOfMonth) {
                        out.println("<li calss=\"thisDay\">" + calendar.get(Calendar.DAY_OF_MONTH) + "</li>");
                    }  
                    else {
                        out.println("<li>" + calendar.get(Calendar.DAY_OF_MONTH) + "</li>");
                    }
                    calendar.add(Calendar.DAY_OF_MONTH, +1);
                }
                out.println("<li>" + calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
                
                out.println("</ul>");
                out.println("</div>");
    
                // Increments calendar month
                calendar.add(Calendar.MONTH, +1);
            }
            out.println("</div>");
            
            //This is section handles the timetable in the calendar
                        
            // Prints day of the week in timetable
            calendar = Calendar.getInstance();
            calendar.set(Calendar.DAY_OF_WEEK, calendar.getFirstDayOfWeek());

            out.println("<table class=\"timetable\">");
            out.println("<thead>");
            out.println("<tr>");
            out.println("<th>&nbsp;</th>");

            for(int i = 0; i <= 6; i++) {
                    out.println("<th class= thWeekday>" + wd.format(calendar.getTime()) + "<br>" + dt.format(calendar.getTime()) + "</th>");
                    calendar.set(Calendar.DAY_OF_WEEK,(calendar.get(Calendar.DAY_OF_WEEK)) + 1);
            }
            
            out.println("</tr>");
            out.println("</thead>");
            out.println("<tbody>");
            out.println("<tr>");
            
            calendar.set(Calendar.HOUR_OF_DAY, 8);
            calendar.set(Calendar.MINUTE, 0);
            
            // Prints time of the day from 08:00 to 18:00
            while (calendar.get(Calendar.HOUR_OF_DAY) <= 18) {
                out.println("<tr><td class=\"time\">" + hm.format(calendar.getTime()) + "</td>");
                for(int e = 0; e <= 6; e++) {
                out.println("<td class=" + dt.format(calendar.getTime()) + "id=" + hm.format(calendar.getTime()) + ">" + dt.format(calendar.getTime()) + " " + hm.format(calendar.getTime()) + "</td>");
                calendar.add(Calendar.DAY_OF_MONTH, 1);
                }
                out.println("</tr>");
                calendar.add(Calendar.DAY_OF_MONTH,-7);
                calendar.add(Calendar.MINUTE, 15);
            }
            /*out.println("<td>10:00</td>\n" + 
                    "<td class=\" test-events\" rowspan=\"4\">\n" +
                    "<span class=\"clockStart\">10:00</span><br>\n" +
                    "<span class=\"clockEnd\">11:30</span>\n" +
                    "<span class=\"title\">test subject</span><br>\n" +
                    "<span class=\"lecturer\">test lecturer</span><br>\n" +
                    "<span class=\"class\">test class 3421</span>\n" +
                    "</td>");*/
            out.println("</tr>");
            out.println("</tbody>");
            out.println("</table>");
            
            out.println("<script>");
            out.println("</script>");
            
            out.println("<h1> <a href =\"CreateTimetableEvent\"> Create event </a> </h1>");
            
            out.println("<br><br><br><br><br>");
            out.println("<form>");
            out.println("<input type=\"button\" value=\"DO NOT CLICK UNDER ANY CIRCUMSTANCES\" onclick=\"window.location.href='https://www.youtube.com/watch?v=6n3pFFPSlW4'\" />");
            out.println("</form>");

            // HTML end
            out.println("</body>");
            out.println("</html>");
            
        }
        
    }
}