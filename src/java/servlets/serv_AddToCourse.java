/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servlets;

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
 * @author Tobias
 */
@WebServlet(name = "addToCourse", urlPatterns = {"/addToCourse"})
public class serv_AddToCourse extends HttpServlet {
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
            
            HtmlHelper site = new HtmlHelper(out, request);
            site.printHead("Added to course", "");
            
            String course_id = request.getParameter("course_id");
            String student_id = request.getParameter("student_id");
            
            Connection conn = login.loginToDB(out);
            
            //adds user to the course
            UserHelper.addUserToCourse(course_id, student_id, conn, out);
            
            out.println("<form action=\"oneCourse\" method=\"post\"><input type=\"hidden\" name=\"course_id\" value=\"" + course_id + "\">");
            out.println("<button class=\"button\">Go to course</button></form>");
            
            
            site.closeAndPrintEnd(login);
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
            HtmlHelper site = new HtmlHelper(out, request);
            site.printHead("Students not in course", "");
            
            String course_id = request.getParameter("course_id");
            
            Connection conn = login.loginToDB(out);
            
            //button for adding a student to the course using their id
            //(also adds them to all modules in the course
            out.println("<div class=\"add-to-course-box\">");
            out.println("<h2>Type user id to add that user to this course</h2>");
            out.println("<form action=\"addToCourse\" method=\"get\">");
            out.println("<input type=\"hidden\" name=\"course_id\" value=\"" + course_id + "\">");
            out.println("<input type=\"text\" name=\"student_id\" placeholder=\"student id\">");
            out.println("<button class=\"button\">Add to course</button>");
            out.println("</form>");
            out.println("</div>");
            
            try {
                ResultSet rset = UserHelper.getUsersNotInCourse(conn, course_id);
                
                UserHelper.printUsers(out, conn, rset);
            } catch (SQLException ex) {
                out.println(ex);
            }
            
            site.useJS("delete-for-buttons.js");
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
