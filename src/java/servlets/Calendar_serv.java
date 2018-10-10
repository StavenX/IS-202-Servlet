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
@WebServlet(name = "Calendar_serv", urlPatterns = {"/Calendar_serv"})
public class Calendar_serv extends HttpServlet {
    
    // Creates a date formats
    SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
    SimpleDateFormat mf = new SimpleDateFormat("MMMM", Locale.US);
    
    // Creates calendar object based on host timezone
    Calendar calendar = Calendar.getInstance();
    
    // Assigns month, year and day to integers from calendar
    int month = calendar.get(Calendar.MONTH);
    int year = calendar.get(Calendar.YEAR);
    int currentDay = calendar.get(Calendar.DAY_OF_MONTH);
    
    // ArrayList for days
    ArrayList<Integer> days = new ArrayList<>();
    
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
            
            // Prints date (temporary)
            out.println(df.format(calendar.getTime()));
            
            // HTML initialization and link to css
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<link rel=\"stylesheet\" type=\"text/css\" href=\"theme.css\">");
            out.println("<title>Servlet Calendar</title>");            
            out.println("</head>");
            out.println("<body>");
            
            // Prints out multiple calendars with different months
            for(int i = 0; i < 3; i++) {
            
            // Prints month
            out.println("<div class=\"month\">");
            out.println("<h1>" + (mf.format(calendar.getTime())) + "</h1>");
            out.println("</div>");
            
            // Prints weekdays
            out.println("<ul class=\"weekdays\">");
            out.println("<li>Mo</li>");
            out.println("<li>Tu</li>");
            out.println("<li>We</li>");
            out.println("<li>Th</li>");
            out.println("<li>Fr</li>");
            out.println("<li>Sa</li>");
            out.println("<li>Su</li>");
            out.println("</ul>");
            
            // Creates three ArrayLists containing days for three months
            out.println("<ul class=\"days\">");
            for(int d = 1; d <= calendar.getActualMaximum(Calendar.DAY_OF_MONTH); d++) {
                
                // Finds and assigns current day of current month to HTML class and adds day to days(ArrayList)
                if (calendar.get(Calendar.MONTH) == month && d == currentDay) {
                            out.println("<li class=\"currentDay\">" + currentDay + "</li>");
                        }
                else {  
                    days.add(d);
                    out.println("<li>" + d + "</li>");    
                }
            }
            
            /*  Delete this after testing for leap year
                }
                // Includes leap year
                else if (year % 4 == 0 && year % 100 != 0) {
                        out.println("<li>29</li>");
                }
                else if (year % 400 == 0) {
                        out.println("<li>29</li>");
                }*/
                
            out.println("</ul>");
                
            // Increments calendar month
            calendar.add(Calendar.MONTH, +1);
            
            // Clears day ArrayList
            days.clear();
            }
            
            // Sets calendar and month to current time
            calendar = Calendar.getInstance();
            
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
