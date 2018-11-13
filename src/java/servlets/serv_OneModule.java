/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servlets;

import helpers.AccessTokenHelper;
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
            /* TODO output your page here. You may use following sample code. */
            HtmlHelper site = new HtmlHelper(out, request);
            site.printHead("Single module", "one-module-container");
            
            String module_id = request.getParameter("module_id");
            String orderBy = request.getParameter("orderBy");
            if (orderBy == null) {
                orderBy = "";
            }
            
            
            Connection conn;
            conn = login.loginToDB(out);
            out.println("<h2>Viewing a single module</h2>");
            
            ModuleHelper.printOneModule(out, conn, module_id);            
            
            out.println("<div class=\"module-student-list\">");
            
            AccessTokenHelper a = new AccessTokenHelper(request);
            String role = a.getUserRole();
            
            
            
            if (role.equals("Lecturer")) {
                String sqlString = "SELECT users.user_username, course.course_name, module.module_name, module.module_desc, module.module_points AS \'max_points\',\n" +
                "module_details.module_points AS \'your_points\', module_details.student_id, module_details.module_status \n" +
                "FROM course\n" +
                "INNER JOIN module ON course.course_id = module.course_id\n" +
                "INNER JOIN module_details ON module.module_id = module_details.module_id\n" +
                "INNER JOIN users ON module_details.student_id = users.user_id\n" +
                "WHERE module.module_id = ?\n" +
                "AND module_details.student_id LIKE '%'\n" + 
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
                out.println("<input type=\"submit\" class=\"button\" name=\"orderBy\" value=\"Not delivered\">");
                out.println("<input type=\"submit\" class=\"button\" name=\"orderBy\" value=\"Pending\">");
                out.println("<input type=\"submit\" class=\"button\" name=\"orderBy\" value=\"Completed\">");
                out.println("</form>");
                
                try {
                    PreparedStatement getPeople = conn.prepareStatement(sqlString);
                    getPeople.setString(1, module_id);

                    ResultSet rset = getPeople.executeQuery();
                    
                    //is true if rset isn't empty
                    if (rset.isBeforeFirst()) {
                        out.println("<table class=\"module-students-table\">");
                        out.println("<tr>");
                        out.println("<th>Name</th>");
                        out.println("<th>Your points</th>");
                        out.println("<th>Status</th>");
                        out.println("</tr>");

                        while (rset.next()) {
                            String user_name = rset.getString("user_username");
                            String user_id = rset.getString("student_id");
                            String course_name = rset.getString("course_name");
                            String module_name = rset.getString("module_name");
                            String module_desc = rset.getString("module_desc");
                            String max_points = rset.getString("max_points");
                            String your_points = rset.getString("your_points");
                            String status = rset.getString("module_status");

                            if (your_points == null) {
                                your_points = "N/A";
                            }

                            out.println("<form action=\"oneStudentModule\" method=\"post\">");
                            out.println("<input type=\"hidden\" name=\"module_id\" value=\"" + module_id +"\">");
                            out.println("<input type=\"hidden\" name=\"user_id\" value=\"" + user_id + "\">");
                            out.println("<tr>");
                            out.println("<td>" + user_name + "</td>");
                            out.println("<td>" + your_points + "</td>");
                            out.println("<td class=\"deliver-status\"><div class=\"module-status\">" + status + "</div><button class=\"button\">Grade</button></td>");
                            out.println("</tr>");
                            out.println("</form>");
                        }
                        out.println("</table>");
                    } else {
                        out.println("<p style=\"text-align: center\">No students have this module, check if db mistake</p>");
                    }
                    
                } catch (SQLException ex) {
                    out.println("SQL error: " + ex);
                }
            }
            out.println("</div>");
            site.useJS("editmodule.js");
            site.useJS("somebackgrounds.js");
            
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
        response.getWriter().println("hei");
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
