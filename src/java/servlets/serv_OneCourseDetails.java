/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servlets;

import helpers.AnnouncementHelper;
import helpers.CourseHelper;
import helpers.HtmlHelper;
import helpers.ModuleHelper;
import helpers.UserHelper;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import network.Login;

/**
 *
 * @author tobia
 */
@WebServlet(name = "oneCourseDetails", urlPatterns = {"/oneCourseDetails"})
public class serv_OneCourseDetails extends HttpServlet {
    Login login = new Login();

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
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            
        }
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
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            
            Connection conn = login.loginToDB(out);
            HtmlHelper site = new HtmlHelper(out, request);
            site.printHead("Details", "one-course");
            
            String course_id = request.getParameter("course_id");
            String course_name = CourseHelper.getCourseName(course_id, conn);
            String role = request.getParameter("role");
            String details = request.getParameter("details").toLowerCase();
            String orderBy = request.getParameter("orderBy");
            String direction = request.getParameter("orderDirection");
            
            orderBy = (orderBy == null) ? "" : orderBy;
            direction = (direction == null) ? "" : direction;
            
            out.println("<form action=\"oneCourse\" method=\"post\">");
            out.println(CourseHelper.invisInputs(course_id, role));
            out.println("<button class=\"button\">Back to " + course_name + "</button>");
            out.println("</form>");
            
            //link to add students who isn't in the course
            if(role.toLowerCase().equals("lecturer")) {
                out.println("<form action=\"addToCourse\" method=\"post\">");
                out.println(CourseHelper.invisInputs(course_id, role));
                out.println("<button class=\"button\">View students not in this course</button>");
                out.println("</form>");
            }
            
            try {
                switch(details) {
                    case "modules":
                        ModuleHelper.printModules(out, conn, orderBy, direction, role, course_id, "oneCourseDetails");
                        break;

                    case "students":
                        UserHelper.printUsers(out, conn, UserHelper.getUsers(conn, course_id));
                        break;

                    case "announcements":
                        ResultSet rset = AnnouncementHelper.getAnnouncements(50, conn, course_id);
                        int amount = AnnouncementHelper.printAnnouncements(out, conn, rset, role);                    
                        out.println("Printed " + amount + " newest announcements. If you want to see more please contact your system admin.");
                        break;

                    default:
                        out.println("you done goofed, Tobias.");
                }
            } catch (SQLException ex) {
                out.println(ex);
            }
            site.useJS("buttons-for-delete.js");
            site.closeAndPrintEnd(login);
        }
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
