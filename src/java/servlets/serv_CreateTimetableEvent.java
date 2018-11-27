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
                    out.println("<option value=" + (rset.getString("course_id")) + ">" + (rset.getString("course_name")) + "</option>");
                }
            }
            catch (Exception e) {
                out.println(e);
            }
            dbLogin.close();
            
            out.println("</select><br><br>");
            out.println("Classroom<br><input type=text name=classroom><br><br>");
            out.println("Lecturer(s)<br><input type=text name=lecturer><br><br>");
            out.println("Date<br><input type=date name=date><br><br>");
            out.println("<div style=float:left;>");
            out.println("Start time<br><input type=time name=st>");
            out.println("</div>");
            out.println("<div style=margin-left:115px;>");
            out.println("End time<br><input type=time name=et>");
            out.println("</div><br>");
            out.println("Repeat every: <br><input type=checkbox name=Monday value=0> Monday<br>");
            out.println("<input type=checkbox name=\"Tuesday\" value=1> Tuesday<br>");
            out.println("<input type=checkbox name=\"Wednesday\" value=2> Wednesday<br>");
            out.println("<input type=checkbox name=\"Thursday\" value=3> Thursday<br>");
            out.println("<input type=checkbox name=\"Friday\" value=4> Friday<br>");
            out.println("<input type=checkbox name=\"Saturday\" value=5> Saturday<br>");
            out.println("<input type=checkbox name=\"Sunday\" value=6> Sunday<br><br>");
            out.println("<input type=submit value=\"Submit\">");
            out.println("</form>");
            
            /*<div id="addEventForm">
            <form>
                <br>
                Course ID <input type="text" name="Course ID"><br>
                Lecturers <input type="text" name="Lecturers"><br>
                Room      <input type="text" name="Room"><br>
                Start time <input type="time" name="Start time" min="08:00:00" max="18:00:00">
                End time <input type="time" name="End time" min="08:00:00" max="18:00:00"><br><br>
                Event date <input type="date" name="Date"><br><br>
                <span> Repeat event every: </span><br>
                <input type="checkbox" name="Monday">Monday<br>
                <input type="checkbox" name="Tuesday">Tuesday<br>
                <input type="checkbox" name="Wednesday">Wednesday<br>
                <input type="checkbox" name="Thursday">Thursday<br>
                <input type="checkbox" name="Friday">Friday<br>
                <input type="checkbox" name="Saturday">Saturday<br>
                <input type="checkbox" name="Sunday">Sunday<br><br>
                <input type="submit" value="Submit">
            </form>
        </div>*/
        }
    }
            
            @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            
            HtmlHelper site = new HtmlHelper(out);
            site.printHead("New event", "body");
            
            String courseCode = request.getParameter("courseCode");
            String classroom = request.getParameter("classroom");
            String lecturer = request.getParameter("lecturer");
            String date = request.getParameter("date");
            String startTime = request.getParameter("st");
            String endTime = request.getParameter("et");
            String mon = request.getParameter("Monday");
            String tue = request.getParameter("Tuesday");
            String wed = request.getParameter("Wednesday");
            String thu = request.getParameter("Thursday");
            String fri = request.getParameter("Friday");
            String sat = request.getParameter("Saturday");
            String sun = request.getParameter("Sunday");
            
            Connection conn;
            conn = dbLogin.loginToDB(out);
            CalendarHelper.createEvent(courseCode, classroom, lecturer, date, startTime, endTime,
                                       mon, tue, wed, thu, fri, sat, sun);
            dbLogin.close();
        }
    }
}