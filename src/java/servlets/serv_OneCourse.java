/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servlets;

import helpers.AccessTokenHelper;
import helpers.AnnouncementHelper;
import helpers.CourseHelper;
import helpers.HtmlHelper;
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
@WebServlet(name = "oneCourse", urlPatterns = {"/oneCourse"})
public class serv_OneCourse extends HttpServlet {
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
            
            String course_id = request.getParameter("course_id");
            String course_name = CourseHelper.getCourseName(course_id, conn);
            
            HtmlHelper site = new HtmlHelper(out, request);
            site.printHead(course_name, "single-course");
            
            
            out.println("You are now viewing course " + course_name);
            
            out.println("<form action=\"addToCourse\">");
            out.println("<input type=\"hidden\" name=\"course_id\" value=\"" + course_id + "\">");
            out.println("<input type=\"text\" name=\"student_id\" placeholder=\"student id\">");
            out.println("<button class=\"button\">Add to course</button>");
            out.println("</form>");
            
            
            AccessTokenHelper a = new AccessTokenHelper(request);
            String username = a.getUsername();
            String role = a.getUserRole();
            String user_id = UserHelper.getUserId(conn, username);
            
            if (role.equals("Lecturer")) {
                out.println("<form action=\"createAnnouncement\" method=\"get\">");
                out.println("<input type=\"hidden\" name=\"user_id\" value=\"" + user_id + "\">");
                out.println(CourseHelper.invisInputs(course_id, role));
                out.println("<button class=\"button\">Make announcement</button>");
                out.println("</form>");
            }
            
            out.println("<div class=\"announcement-preview-container\">");
            System.out.println("heihei");
            out.println("Last announcement:");
            try {
                ResultSet rset = AnnouncementHelper.getAnnouncements(conn, course_id, 1);
                while (rset.next()) {
                    String title = rset.getString("announcement_title");
                    String content = rset.getString("announcement_content");
                    String author = UserHelper.getFullNameById(conn, rset.getString("announcement_author_id"));

                    out.println("<p>Title: " + title + "</p>");
                    out.println("<p>Content: " + content + "</p>");
                    out.println("<p>Author full name: " + author + "</p>");
                }
            } catch (SQLException ex) {
                out.println(ex);
            }
            
            out.println("</div>");
            
            
            
            
            out.println("<h3>Modules in this course (sort buttons not working correctly):</h3>");
            out.println("<form action=\"oneCourseDetails\" method=\"post\">");
            out.println(CourseHelper.invisInputs(course_id, role));
            out.println("<input type=\"submit\" class=\"button\" name=\"details\" value=\"Modules\">");
            out.println("</form>");
            
            
            out.println("<h3>Students in this course:</h3>");
            out.println("<form action=\"oneCourseDetails\" method=\"post\">");
            out.println(CourseHelper.invisInputs(course_id, role));
            out.println("<input type=\"submit\" class=\"button\" name=\"details\" value=\"Students\">");
            out.println("</form");
            
            site.printEnd();
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
