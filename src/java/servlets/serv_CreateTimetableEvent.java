package servlets;

import helpers.CalendarHelper;
import helpers.HtmlHelper;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import network.Login;
import helpers.CourseHelper;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


/**
 *
 * @author adrian
 */
@WebServlet(name = "serv_CreateTimetableEvent", urlPatterns = {"/CreateTimetableEvent"})
public class serv_CreateTimetableEvent extends HttpServlet {
    
    Login dbLogin = new Login();
    
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
        request.setCharacterEncoding("UTF-8");
        try (PrintWriter out = response.getWriter()) {
            
            HtmlHelper site = new HtmlHelper(out);
            site.printHead("New event", "body");
            
            out.println("<form method=\"post\">");
            out.println("Course code<br><select name=courseCode><br><br>");
            Connection conn;
            conn = dbLogin.loginToDB(out);
            
            try {
                ResultSet rset = CourseHelper.getAllCourses(out, conn);
                while (rset.next()) {
                    out.println("<option value=" + (rset.getString("course_name")) + ">" + (rset.getString("course_name")) + "</option>");
                }
            }
            catch (Exception e) {
                out.println(e);
                dbLogin.close();
            }
            dbLogin.close();
            
            out.println("</select><br><br>");
            out.println("Classroom<br><input type=text name=classroom><br><br>");
            out.println("Lecturer(s)<br><input type=text name=lecturer><br><br>");
            out.println("Date<br><input type=date name=date required><br><br>");
            out.println("<div style=float:left;>");
            out.println("Start time<br><input type=time name=st required>");
            out.println("</div>");
            out.println("<div style=margin-left:115px;>");
            out.println("End time<br><input type=time name=et required>");
            out.println("</div><br>");
            out.println("Repeat every week ");
            out.println("<input type=checkbox name=repeat>");
            out.println("Until:<br><input type=date name=lastRepeat><br><br><br>");
            out.println("<input type=submit value=\"Submit\">");
            out.println("</form>");
        }
    }
            
            @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        request.setCharacterEncoding("UTF-8");
        try (PrintWriter out = response.getWriter()) {
            
            HtmlHelper site = new HtmlHelper(out);
            site.printHead("New event", "body");
            
            String courseCode = request.getParameter("courseCode");
            String classroom = request.getParameter("classroom");
            String lecturer = request.getParameter("lecturer");
            String date = request.getParameter("date");
            String startTime = request.getParameter("st");
            String endTime = request.getParameter("et");
            String repeat = request.getParameter("repeat");
            String lastRepeat = request.getParameter("lastRepeat");
            if (repeat == null) {
                repeat = "off";
                lastRepeat = "";
            }

            Calendar startCal = CalendarHelper.newCalendar(date, startTime);
            Calendar endCal = CalendarHelper.newCalendar(date, endTime);
            
            if (repeat.equals("on")) {
                Calendar lastRepeatCal = CalendarHelper.newCalendar(lastRepeat, startTime);
                
                while (startCal.before(lastRepeatCal)) {
                    String sc = String.valueOf(startCal.getTimeInMillis());
                    String ec = String.valueOf(endCal.getTimeInMillis());
                    String cw = String.valueOf(startCal.get(Calendar.WEEK_OF_YEAR));
                    
                    Connection conn;
                    conn = dbLogin.loginToDB(out);
                    CalendarHelper.insertEvents(out, conn, classroom, lecturer, courseCode, "IT og IS semester 1", sc, ec, cw);
                    dbLogin.close();
                    
                    startCal.add(Calendar.DATE, 7);
                    endCal.add(Calendar.DATE, 7);
                }
            }
            else if (repeat.equals("off")) {
                System.out.println("FDdfddf");
                String sc = String.valueOf(startCal.getTimeInMillis());
                String ec = String.valueOf(endCal.getTimeInMillis());
                String cw = String.valueOf(startCal.get(Calendar.WEEK_OF_YEAR));
                Connection conn;
                conn = dbLogin.loginToDB(out);
                CalendarHelper.insertEvents(out, conn, classroom, lecturer, courseCode, "IT og IS semester 1", sc, ec, cw);
                dbLogin.close();
            }
        }
        catch (Exception e) {
            System.out.println(e);
            dbLogin.close();
        }
    }
}