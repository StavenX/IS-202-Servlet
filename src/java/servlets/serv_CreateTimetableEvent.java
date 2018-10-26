package servlets;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author adrian
 */
@WebServlet(name = "serv_CreateTimetableEvent", urlPatterns = {"/CreateTimetableEvent"})
public class serv_CreateTimetableEvent extends HttpServlet {

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
            
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet serv_CreateTimetableEvent</title>");  
            out.println("<link rel=\"stylesheet\" type=\"text/css\" href=\"theme.css\">");
            out.println("</head>");
            out.println("<body>");
            
            // Form for new event input
            out.println("<form action=\"serv_CreateTimetableEvent\" method=\"post\">");
            out.println("Class<br><input type=\"text\" name=\"class\"><br><br>");
            out.println("Classroom<br><input type=\"text\" name=\"classroom\"><br><br>");
            out.println("Lecturer(s)<br><input type=\"text\" name=\"lecturer\"><br><br>");
            out.println("Start time<br><input type=\"time\" name=\"st\"><br><br>");
            out.println("End time<br><input type=\"time\" name=\"et\"><br><br>");
            out.println("Repeat every: <br><input type=\"checkbox\" name=\"Monday\" value=0> Monday<br>");
            out.println("<input type=\"checkbox\" name=\"Tuesday\" value=1> Tuesday<br>");
            out.println("<input type=\"checkbox\" name=\"Wednesday\" value=2> Wednesday<br>");
            out.println("<input type=\"checkbox\" name=\"Thursday\" value=3> Thursday<br>");
            out.println("<input type=\"checkbox\" name=\"Friday\" value=4> Friday<br>");
            out.println("<input type=\"checkbox\" name=\"Saturday\" value=5> Saturday<br>");
            out.println("<input type=\"checkbox\" name=\"Sunday\" value=6> Sunday<br><br>");
            out.println("Note<br><input type=\"text\" name=\"note\"><br><br>");
            out.println("<input type=\"submit\" value=\"Submit\">");
            out.println("</form>");
            
            request.getParameter("class");
            request.getParameter("classroom");
            request.getParameter("lecturer");
            request.getParameter("st");
            request.getParameter("et");
            request.getParameter("Monday");
            request.getParameter("Tuesday");
            request.getParameter("Wednesday");
            request.getParameter("Thursday");
            request.getParameter("Friday");
            request.getParameter("Saturday");
            request.getParameter("Sunday");
            request.getParameter("note");
            
            out.println("</body>");
            out.println("</html>");
        }
    }
}
