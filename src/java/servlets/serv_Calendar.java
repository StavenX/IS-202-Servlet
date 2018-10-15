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
import java.util.Date;
/**
 *
 * @author adriannesvik
 */
@WebServlet(name = "serv_Calendar", urlPatterns = {"/serv_Calendar"})
public class serv_Calendar extends HttpServlet {
    
    // Creates a date formats
    SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
    SimpleDateFormat mf = new SimpleDateFormat("MMMM", Locale.US);
    SimpleDateFormat hm = new SimpleDateFormat("HH:mm");
    
    // Creates calendar object based on host timezone
    Calendar calendar = Calendar.getInstance();
    Calendar cal = Calendar.getInstance();
    
    // Assigns month, year, currentday(day of the month) and day(day of the week) to integers from calendar
    int month = calendar.get(Calendar.MONTH);
    int year = calendar.get(Calendar.YEAR);
    int currentDay = calendar.get(Calendar.DAY_OF_MONTH);
    int day = calendar.get(Calendar.DAY_OF_WEEK);
    
    // ArrayList for days(days of the month and weekdays(days of the week)
    ArrayList<Integer> days = new ArrayList<>();
    ArrayList<String> weekdays = new ArrayList<>();
    ArrayList<Date> time = new ArrayList<>();
    
    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {

            // Clears arraylist and calendars to avoid element duplication
            cal.clear();
            time.clear();
            days.clear();
            weekdays.clear();
            calendar = Calendar.getInstance();
            
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

            // Calendar and timetable wrapper for layout purposes
            out.println("<div class=\"calendarWrapper\">");
            
            // Prints out multiple calendars with different months
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
                    if(w == day-2 && month == calendar.get(Calendar.MONTH)) {
                        out.println("<li class=\"thisDay\">" + weekdays.get(w).substring(0,2) + "</li>");
                    }
                    else {
                        out.println("<li>" + weekdays.get(w).substring(0,2) + "</li>");
                    }
                }
                out.println("</ul>");
                
                // Creates three ArrayLists containing days for three months
                out.println("<div class=\"daysDiv\">");
                out.println("<ul class=\"days\">");
                for(int d = 1; d <= calendar.getActualMaximum(Calendar.DAY_OF_MONTH); d++) {
                
                    // Finds and assigns current day of current month to HTML class and adds day to days(ArrayList)
                    if (calendar.get(Calendar.MONTH) == month && d == currentDay) {
                            out.println("<li class=\"thisDay\">" + currentDay + "</li>");
                    }
                    else {  
                        days.add(d);
                        out.println("<li>" + d + "</li>");    
                    }
                }
                out.println("</ul>");
                out.println("</div>");
            
            /*  Delete this after testing for leap year
                }
                // Includes leap year
                else if (year % 4 == 0 && year % 100 != 0) {
                        out.println("<li>29</li>");
                }
                else if (year % 400 == 0) {
                        out.println("<li>29</li>");
                }*/
                
                // Increments calendar month
                calendar.add(Calendar.MONTH, +1);
            }
            
            out.println("</div>");
            /*
                This is section handles the timetable in the calendar
            */
            
            // Code below is definetly subject to change. Momentarily functionality.
            
            // If-else statement for highlighting current day of the week in CSS
            calendar = Calendar.getInstance();
            out.println("<div class=\"schedule\">");
            out.println("<div class=\"events\">");
            out.println("<ul>");

            for(int i = 0; i <= 6; i++) {
                if(i == day && month == calendar.get(Calendar.MONTH)) {
                    out.println("<li class=\"events-group\">");
                    out.println("<div class=\"top-info\">");
                    out.println("<span>" + weekdays.get(day) + "</span>");
                    out.println("</div>");
                    out.println("<ul></li></ul></li>");
                }
                else {
                    out.println("<li class=\"events-group\">");
                    out.println("<div class=\"top-info\">");
                    out.println(weekdays.get(i));
                    out.println("</div>");
                    out.println("<ul></li></ul></li>");
                }
            }
            out.println("</li>");
            out.println("</ul>");
            out.println("</div>");
            
            // Prints time of the day from 08:00 to 18:00
            out.println("<div class=\"timeline\">");
            out.println("<ul>");
            int l = 6;
            for (int g = 0; g <= 11; g++) {
                cal.set(Calendar.HOUR_OF_DAY, l += 1);
                cal.set(Calendar.MINUTE, 0);
            
                Date caldate = cal.getTime();
                time.add(caldate);
                
                out.println("<li>");
                out.println("<span>" + hm.format(time.get(g)) + "</span>");
                out.println("</li>");
            }
            
            out.println("</ul>");
            out.println("</div>");
            out.println("</div>");
            
            // HTML end
            out.println("</body>");
            out.println("</html>");
            
        }
        
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}