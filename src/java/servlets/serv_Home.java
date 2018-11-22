/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servlets;

import helpers.AccessTokenHelper;
import helpers.CourseHelper;
import helpers.HtmlHelper;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
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
@WebServlet(name = "Home", urlPatterns = {"/Home"})
public class serv_Home extends HttpServlet {
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
        try (PrintWriter out = response.getWriter()) {
            HtmlHelper site = new HtmlHelper(out, request);
            site.printHead("Home", "home");
            
            Connection conn = login.loginToDB(out);
            
            AccessTokenHelper a = new AccessTokenHelper(request);
            String username = a.getUsername();
            String user_id = "";
            
            ResultSet rset = null;
            if (request.isUserInRole("Lecturer")) {
                rset = CourseHelper.getAllCourses(out, conn);
            } else {
                try {
                    PreparedStatement getId = conn.prepareStatement("SELECT user_id FROM users WHERE user_username = ?");
                    getId.setString(1, username);
                    rset = getId.executeQuery();

                    while (rset.next()) {
                        user_id = rset.getString("user_id");
                    }
                } catch (SQLException ex) {
                    out.println("SQLException: " + ex);
                }
                rset = CourseHelper.getCourses(user_id, out, conn);
            }
            
            out.println("<div class=\"courses-container\">");
            try {
                while (rset.next()) {
                    String course_id = rset.getString("course_id");
                    String course_name = rset.getString("course_name");
                    courseBox(out, course_id, course_name);
                }
            }
            catch (SQLException ex) {
                out.println("Hater å skrive errormeldinger " + ex);
            }
            
            out.println("</div>");
            
            out.println("<a href=\"FileUpload\">Temporary link to fileupload</a>");
            
            
            site.closeAndPrintEnd(login);
        }
    }

    public void courseBox(PrintWriter out, String course_id, String course_name) {
        out.println("<form action=\"oneCourse\" method=\"post\">");
        out.println("<button class=\"course-container\">");
        out.println("<img class=\"course-img\" src=\"http://via.placeholder.com/200x100\">");
        out.println("<input name=\"course_id\" type=\"hidden\" value=\"" + course_id + "\">");
        out.println("<input name=\"course_name\" type=\"hidden\" value=\"" + course_name + "\">");
        out.println("<h2 class=\"course-title\">" + course_name + "</h2>");
        out.println("<p class=\"course-name\">Lorem ipsum</p>");
        out.println("</button>");
        out.println("</form>");
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
