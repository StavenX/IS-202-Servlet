package servlets;

import helpers.CalendarHelper;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import helpers.HtmlHelper;
import java.sql.Connection;
import java.util.Calendar;
import network.Login;

/**
 *
 * @author adriannesvik
 */
@WebServlet(name = "serv_DeleteCalendarEvent", urlPatterns = {"/DeleteCalendarEvent"})
public class serv_DeleteCalendarEvent extends HttpServlet {
    
    Login dbLogin = new Login();
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            
            HtmlHelper site = new HtmlHelper(out);
            site.printHead("New event", "body");
            
            out.println("<form method=post>");
            out.println("<br>Select what event to delete based on date and start time<br>");
            out.println("Date<br><input type=date name=date required><br><br>");
            out.println("<div style=float:left;>");
            out.println("Start time<br><input type=time name=time required>");
            out.println("</div>");
            out.println("<div style=margin-left:115px;>");
            out.println("<input type=submit value=\"Submit\">");
            out.println("</form>");
        }
    }
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
            
            String date = request.getParameter("date");
            String time = request.getParameter("time");
            
            Calendar cal = CalendarHelper.newCalendar(date, time);
            String calMilli = String.valueOf(cal.getTimeInMillis());
            
            try (PrintWriter out = response.getWriter()) {
                    Connection conn;
                    conn = dbLogin.loginToDB(out);
                    CalendarHelper.deleteEvent(out, conn, calMilli);
                    dbLogin.close();
            }
            catch (Exception e) {
                System.out.println(e);
            }
        }
}
