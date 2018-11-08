/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servlets;

import helpers.CourseHelper;
import helpers.HtmlHelper;
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
@WebServlet(name = "serv_Index", urlPatterns = {"/Index"})
public class serv_Index extends HttpServlet {
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
            
            ResultSet rset = CourseHelper.getCourses(out, conn);
            
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
            
        }
    }

    public void courseBox(PrintWriter out, String course_id, String course_name) {
        out.println("<form action=\"oneCourse\" method=\"get\">");
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
