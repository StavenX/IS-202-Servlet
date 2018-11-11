/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servlets;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import network.Login;
import helpers.HtmlHelper;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *
 * @author tobia
 */
@WebServlet(name = "oneStudentModule", urlPatterns = {"/oneStudentModule"})
public class serv_OneStudentModule extends HttpServlet {
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
            site.printHead("Graded", "invisible");
            
            
            String module_id = request.getParameter("module_id");
            String user_id = request.getParameter("user_id");
            String new_points = request.getParameter("new_points");
            String comment = request.getParameter("comment");
            
            Connection conn = login.loginToDB(out);
            
            try {
                PreparedStatement grade = conn.prepareStatement("UPDATE module_details SET module_points = ?, module_status = \'Completed\'\n" +
                                                                "WHERE module_ID = ? AND student_id = ?;");
                grade.setString(1, new_points);
                grade.setString(2, module_id);
                grade.setString(3, user_id);
                
                grade.executeUpdate();
                
            } catch (SQLException ex) {
                out.println(ex);
            }
            
            
            out.println("<form name=\"back\" action=\"oneStudentModule\" method=\"post\">");
            out.println("<input type=\"hidden\" name=\"module_id\" value=\"" + module_id +"\">");
            out.println("<input type=\"hidden\" name=\"user_id\" value=\"" + user_id + "\">");
            out.println("<input class=\"button\" type=\"submit\" value=\"Back\">");
            out.println("</form>");
            
            out.println("<script>window.onload=document.forms[\'back\'].submit();</script>");

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
            site.printHead("Student's module", "one-student-module");
            
            
                String sqlString = "SELECT *" +
                "FROM course\n" +
                "INNER JOIN module ON course.course_id = module.course_id\n" +
                "INNER JOIN module_details ON module.module_id = module_details.module_id\n" +
                "INNER JOIN users ON module_details.student_id = users.user_id\n" +
                "WHERE module.module_id = ?\n" +
                "AND module_details.student_id = ?;";
                
                String module_id = request.getParameter("module_id");
                String user_id = request.getParameter("user_id");
                
                
                Connection conn = login.loginToDB(out);
                
                PreparedStatement OneStudentModule;
                
                try {
                    OneStudentModule = conn.prepareStatement(sqlString);
                    OneStudentModule.setString(1, module_id);
                    OneStudentModule.setString(2, user_id);
                    
                    ResultSet rset = OneStudentModule.executeQuery();
                    
                    while (rset.next()) {
                        String user_username = rset.getString("user_fname") + " " + rset.getString("user_lname");
                        String module_name = rset.getString("module_name");
                        String course_name = rset.getString("course_name");
                        String module_desc = rset.getString("module_desc");
                        String max_points = rset.getString("module.module_points");
                        String your_points = rset.getString("module_details.module_points");
                        
                        if (your_points == null) {
                            your_points = "N/A";
                        }
                        String module_status = rset.getString("module_status");
                        
                        
                        out.println("<table>");
                        
                        out.println("<tr>");
                        out.println("<td>Name of student</td>");
                        out.println("<td>" + user_username + "</td>");
                        out.println("</tr>");
                        
                        out.println("<tr>");
                        out.println("<td>Course</td>");
                        out.println("<td>" + course_name + "</td>");
                        out.println("</tr>");
                        
                        out.println("<tr>");
                        out.println("<td>Module name</td>");
                        out.println("<td>" + module_name + "</td>");
                        out.println("</tr>");
                        
                        out.println("<tr>");
                        out.println("<td>Module description</td>");
                        out.println("<td>" + module_desc + "</td>");
                        out.println("</tr>");
                        
                        out.println("<tr>");
                        out.println("<td>Max points</td>");
                        out.println("<td>" + max_points + "</td>");
                        out.println("</tr>");
                        
                        out.println("<tr>");
                        out.println("<td>Points achieved</td>");
                        out.println("<td>" + your_points + "</td>");
                        out.println("</tr>");
                        
                        out.println("<tr>");
                        out.println("<td>Status</td>");
                        out.println("<td>"+ module_status + "</td>");
                        out.println("</tr>");
                        
                        out.println("</table>");
                        
                        out.println("<h2> Grade this module: </h2>");
                        out.println("<form name=\"grade\" action=\"oneStudentModule\" method=\"get\">");
                        out.println("<input type=\"hidden\" name=\"module_id\" value=\"" + module_id +"\">");
                        out.println("<input type=\"hidden\" name=\"user_id\" value=\"" + user_id + "\">");
                        out.println("<input type=\"text\" name=\"new_points\" placeholder=\"Points\">");
                        out.println("</form>");
                        
                        out.println("<textarea class=\"message-input\" rows=\"4\" cols=\"50\" name=\"comment\" form=\"oneStudentModule\" placeholder=\"Delivery comment (currently sent into the void)\"></textarea>");
                        
                        out.println("<button class=\"button\" onclick=\"submit(\'grade\');\">Grade</button>");
                    }
                    
                    
                    
                    
                    out.println("<h2>Comments on module:</h2>");
                    
                    out.println("<div class=\"one-module-comment\">");
                    out.println("<h4>from user:</h4>");
                    out.println("<p>example comment</p>");
                    out.println("</div>");
                    
                    out.println("<div class=\"one-module-comment\">");
                    out.println("<h4>from user2:</h4>");
                    out.println("<p>example comment</p>");
                    out.println("</div>");
                    
                    out.println("<div class=\"one-module-comment\">");
                    out.println("<h4>from user3:</h4>");
                    out.println("<p>example comment</p>");
                    out.println("</div>");
                    
                } catch (SQLException ex) {
                    out.println(ex);
                }
                
            site.useJS("submitform.js");
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
