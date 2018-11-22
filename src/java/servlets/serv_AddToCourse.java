/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servlets;

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
            
            Connection conn = login.loginToDB(out);
            
            String course_id = request.getParameter("course_id");
            String user_ids[] = request.getParameterValues("marked");
            for (String user_id : user_ids) {
                String results = UserHelper.addUserToCourse(course_id, user_id, conn, out);
                out.printf("<p>User with id %s, %s</p>\n", user_id, results);
            }
            
            
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
            
            String course_name = CourseHelper.getCourseName(course_id, conn);
            
            //button for adding a student to the course using their id
            //(also adds them to all modules in the course
            out.println("<div class=\"add-to-course-box\">");
            out.println("<h2>Mark users you wish to add</h2>");
            out.println("<form id=\"test\" action=\"addToCourse\" method=\"get\">");
            out.println(CourseHelper.invisInputs(course_id, "Lecturer"));
            out.println("<button class=\"button\">Add to " + course_name + "</button>");
            out.println("</form>");
            out.println("</div>");
            
            
            out.println(CourseHelper.backToCourseButton(course_id, course_name, "Lecturer"));
            
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
