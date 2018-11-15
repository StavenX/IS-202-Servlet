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
import java.sql.PreparedStatement;
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
@WebServlet(name = "serv_CreateAnnouncement", urlPatterns = {"/createAnnouncement"})
public class serv_CreateAnnouncement extends HttpServlet {
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
            site.printHead("New announcement", "new-announcement");
            
            Connection conn = login.loginToDB(out);
            
            String author = request.getParameter("user_id");
            String course_id = request.getParameter("course_id");
            String course_name = CourseHelper.getCourseName(course_id, conn);
            
            out.println("<h1>Create a new announcement for course " + course_name + " </h2>");
            out.println("<form id=\"new-announcement\" action=\"createAnnouncement\" method=\"post\">");
            out.println("<input type=\"hidden\" name=\"announcement_author_id\" value=\"" + author + "\">");
            out.println("<input type=\"hidden\" name=\"course_id\" value=\"" + course_id + "\">");
            out.println("</form>");
            out.println("<textarea name=\"announcement_title\" form=\"new-announcement\" placeholder=\"Announcement title goes here\"></textarea>");
            out.println("<textarea name=\"announcement_content\" form=\"new-announcement\" placeholder=\"Announcement content goes here\"></textarea>");
            out.println("<br>");
            out.println("<button class=\"button\" onclick=\"submit(\'new-announcement\');\"> Create</button>");
            
            
            
            site.useJS("submitform.js");
            site.printEnd();
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
            site.printHead("Announcement created", "announcement-created");
            
            Connection conn = login.loginToDB(out);
            
            String announcement_id = request.getParameter("announcement_id");
            String announcement_title = request.getParameter("announcement_title");
            String announcement_content = request.getParameter("announcement_content");
            String course_id = request.getParameter("course_id");
            String announcement_author_id = request.getParameter("announcement_author_id");
            
            String course_name = CourseHelper.getCourseName(course_id, conn);
            
            try {
                String sqlString = "INSERT INTO announcement (announcement_id, announcement_title, announcement_content, course_id, announcement_author_id)"
                        + "VALUES (?, ?, ?, ?, ?)";
                PreparedStatement insertAnnouncement = conn.prepareStatement(sqlString);
                insertAnnouncement.setString(1, announcement_id);
                insertAnnouncement.setString(2, announcement_title);
                insertAnnouncement.setString(3, announcement_content);
                insertAnnouncement.setString(4, course_id);
                insertAnnouncement.setString(5, announcement_author_id);
                int amount = insertAnnouncement.executeUpdate();
                
                out.println(amount + " announcement(s) made");
                
                
                
                out.println("<form action=\"oneCourse\" method=\"post\">");
                out.println("<input type=\"hidden\" name=\"course_id\" value=\"" + course_id + "\">");
                out.println("<button class=\"button\">Back to " + course_name + "</button>");
                out.println("</form>");
                
            } catch (SQLException ex) {
                if (ex.getMessage().contains("Column 'announcement_title' cannot be null")) {
                    out.println("Your announcement needs a title! Please try again.");
                    site.printBackButton();
                } else {
                    out.println(ex);
                }
            }
            
            
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
