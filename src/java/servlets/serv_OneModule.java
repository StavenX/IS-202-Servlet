/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servlets;

import helpers.HtmlHelper;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.Statement;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import network.Login;
import helpers.ModuleHelper;
import helpers.UserHelper;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
/**
 *
 * @author Tobias
 */
@WebServlet(name = "oneModule", urlPatterns = {"/oneModule"})
public class serv_OneModule extends HttpServlet {
    Statement stmt;
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
            site.printHead("Single module", "one-module-container");
            
            String role = UserHelper.getUserRole(request);
            
            String module_id = request.getParameter("module_id");
            String orderBy = request.getParameter("orderBy");
            if (orderBy == null) {
                orderBy = "";
            }
                       
            Connection conn = login.loginToDB(out);
            
            String module_name = ModuleHelper.getModuleName(module_id, conn);
            
            site.printBackButton();
            
            out.println("<h2>Viewing a single module</h2>");
            
            ModuleHelper.printOneModule(out, conn, module_id, role);     
            
            if (role.equals("Lecturer")) {
                String sqlString = "SELECT CONCAT(users.user_fname, \' \', users.user_lname) AS `Name`, course.course_name, module.module_name, module.module_desc,\n" +
                "module.module_points AS \'max_points\', module_details.module_points AS \'student points\', module_details.student_id, module_details.module_status \n" +
                "FROM course\n" +
                "INNER JOIN module ON course.course_id = module.course_id\n" +
                "INNER JOIN module_details ON module.module_id = module_details.module_id\n" +
                "INNER JOIN users ON module_details.student_id = users.user_id\n" +
                "WHERE module.module_id = ?\n" +
                "HAVING `Name` LIKE ?\n" + 
                "ORDER BY FIELD(module_status, ";
                
                switch(orderBy) {
                    case "Not delivered":
                        sqlString += "\'Not delivered\', \'Pending\', \'Completed\');";
                        break;
                        
                    case "Completed":
                        sqlString += "\'Completed\', \'Pending\', \'Not Delivered\');";
                        break;
                        
                    case "Pending":
                    default: sqlString += "\'Pending\', \'Not Delivered\', \'Completed\');";
                }
                
                out.println("<form action=\"oneModule\" method=\"get\" class=\"one-module-sort\">");
                
                out.println("<input type=\"hidden\" name=\"module_id\" value=\"" + module_id +"\">");
                out.println("<h2>Order module deliveries by:</h2>");
                
                out.println("<input type=\"text\" name=\"search\" placeholder=\"Search\">");
                out.println("<input class=\"button small-button\"type=\"submit\" value=\"Search\">");
                out.println("<input type=\"submit\" class=\"button\" name=\"orderBy\" value=\"Not delivered\">");
                out.println("<input type=\"submit\" class=\"button\" name=\"orderBy\" value=\"Pending\">");
                out.println("<input type=\"submit\" class=\"button\" name=\"orderBy\" value=\"Completed\">");
                
                out.println("</form>");
                
                try {
                    PreparedStatement getPeople = conn.prepareStatement(sqlString);
                    getPeople.setString(1, module_id);
                    
                    String search = request.getParameter("search");
                    search = (search == null) ? "%" : "%" + search + "%";
                    
                    getPeople.setString(2, search);

                    ResultSet rset = getPeople.executeQuery();
                    
                    //is true if rset isn't empty
                    if (rset.isBeforeFirst()) {
                        
                        
                        out.println("<div class=\"module-student-list\">");
                        out.println("<table class=\"module-students-table\">");
                        out.println("<tr>");
                        out.println("<th>Name</th>");
                        out.println("<th>Student's points</th>");
                        out.println("<th>Status</th>");
                        out.println("</tr>");

                        while (rset.next()) {
                            String user_name = rset.getString("Name");
                            String user_id = rset.getString("student_id");
                            String course_name = rset.getString("course_name");
                            module_name = rset.getString("module_name");
                            String module_desc = rset.getString("module_desc");
                            String max_points = rset.getString("max_points");
                            String student_points = rset.getString("student points");
                            String status = rset.getString("module_status");

                            if (student_points == null) {
                                student_points = "---";
                            }

                            out.println("<form action=\"oneStudentModule\" method=\"post\">");
                            out.println("<input type=\"hidden\" name=\"module_id\" value=\"" + module_id +"\">");
                            out.println("<input type=\"hidden\" name=\"user_id\" value=\"" + user_id + "\">");
                            out.println("<tr>");
                            out.println("<td>" + user_name + "</td>");
                            out.println("<td>" + student_points + "</td>");
                            out.println("<td class=\"deliver-status\"><div class=\"module-status\">" + status + "</div>");
                            out.println("<button class=\"button small-button\">Grade</button></td>");
                            out.println("</tr>");
                            out.println("</form>");
                        }
                        out.println("</table>");                        
                        out.println("</div>");
                    } else {
                        out.println("<p style=\"text-align: center\">No students have this module, check if db mistake</p>");
                    }
                    
                } catch (SQLException ex) {
                    out.println("SQL error: " + ex);
                }
            }
            
            String user_id = UserHelper.getUserId(conn, UserHelper.getUserName(request));
            
            out.println("<h2>Comments on module</h2>");
            
            out.println("<div class=\"new-comment-container\">");
            out.println("<form id=\"newComment\" action=\"oneModule\" method=\"post\">");
            out.println("<input type=\"hidden\" name=\"module_id\" value=\"" + module_id + "\">");
            out.println("<input type=\"hidden\" name=\"user_id\" value=\"" + user_id + "\">");
            out.println("</form>");
            out.println("<textarea form=\"newComment\" name=\"module_comment_content\" class=\"new-comment-input\" placeholder=\"Write a comment...\"></textarea>");
            out.println("<button class=\"button\" onclick=\"submit(\'newComment\');\">Post comment</button>");
            out.println("</div>");
            
            ModuleHelper.printModuleComments(out, conn, module_id, request);
            site.useJS("somebackgrounds.js");
            site.useJS("editmodule.js");
            site.useJS("submitform.js");
            site.useJS("buttons-for-delete.js");
            
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
            site.printHead("commenting", "commenting");
            
            
            Connection conn = login.loginToDB(out);
            String sqlString = "INSERT INTO module_comment (module_comment_content, module_id, user_id) VALUES (?, ?, ?);";
            try {
                PreparedStatement insertComment = conn.prepareStatement(sqlString);
                
                String content = request.getParameter("module_comment_content");
                String module_id = request.getParameter("module_id");
                String user_id = request.getParameter("user_id");
                insertComment.setString(1, content);
                insertComment.setString(2, module_id);
                insertComment.setString(3, user_id);
                
                int amount = insertComment.executeUpdate();
                out.println(amount + " inserted.");
                out.println("<form action=\"oneModule\" method=\"get\">");
                out.println("<input type=\"hidden\" name=\"module_id\" value=\"" + module_id + "\">");
                out.println("<input type=\"submit\" class=\"button\" value=\"Back to module\">");
                out.println("</form>");
                
            } catch (SQLException ex) {
                out.println(ex);
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
