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
    
    // Assigns month and year to integers from calendar
    int month = calendar.get(Calendar.MONTH);
    int year = calendar.get(Calendar.YEAR);
    
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
            
            // Prints days
            out.println("<ul class=\"days\">");
            out.println("<li>1</li>");
            out.println("<li>2</li>");
            out.println("<li>3</li>");
            out.println("<li>4</li>");
            out.println("<li>5</li>");
            out.println("<li>6</li>");
            out.println("<li>7</li><br>");
            out.println("<li>8</li>");
            out.println("<li>9</li>");
            out.println("<li>10</li>");
            out.println("<li>11</li>");
            out.println("<li>12</li>");
            out.println("<li>13</li>");
            out.println("<li>14</li><br>");
            out.println("<li>15</li>");
            out.println("<li>16</li>");
            out.println("<li>17</li>");
            out.println("<li>18</li>");
            out.println("<li>19</li>");
            out.println("<li>20</li>");
            out.println("<li>21</li><br>");
            out.println("<li>22</li>");
            out.println("<li>23</li>");
            out.println("<li>24</li>");
            out.println("<li>25</li>");
            out.println("<li>26</li>");
            out.println("<li>27</li>");
            out.println("<li>28</li><br>");
            
                // prints according to number of days in month
                if (month == 0 || month == 1 || month == 4 || month == 6 || month == 7 || month == 9 || month == 11) {
                    out.println("<li>29</li>");
                    out.println("<li>30</li>");
                    out.println("<li>31</li>");
                }
                else if (month == 3 || month == 5 || month == 8 || month == 10) {
                    out.println("<li>29</li>");
                    out.println("<li>30</li>");
                }
                // Includes leap year
                else if (year % 4 == 0 && year % 100 != 0) {
                        out.println("<li>29</li>");
                }
                else if (year % 400 == 0) {
                        out.println("<li>29</li>");
                }
                
            out.println("</ul>");
                
            // Increments integer and calendar month
            calendar.add(Calendar.MONTH, +1);
            month++;
            }
            
            // Sets calendar and month to current time
            calendar = Calendar.getInstance();
            month = calendar.get(Calendar.MONTH);
            
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
