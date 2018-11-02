/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servlets;

import helpers.HtmlHelper;
import helpers.StudentHelper;
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
            HtmlHelper site = new HtmlHelper(out);
            site.printHead("Single module", "one-module-container");
            
            String module_id = request.getParameter("module_id");
            
            Connection conn;
            conn = login.loginToDB(out);

            out.println("<h2>Viewing a single module</h2>");
            
            ModuleHelper.printOneModule(out, conn, module_id);
            
            
            out.println("<form action=\"addToModule\">");
            out.println("<input type=\"hidden\" name=\"module_id\" value=\"" + module_id + "\">");
            out.println("<input type=\"text\" name=\"student_id\" placeholder=\"Student id\">");
            out.println("<button class=\"button\">Give access to module</button>");
            out.println("</form>");
            
            
            //TODO box containing students
            out.println("<div class=\"module-student-list\">");
            
            String sqlString = "SELECT users.user_name, course.course_name, module.module_name, module.module_desc, module.module_points AS \'max points\',\n" +
            "module_details.module_points AS \'your points\', module_details.module_status \n" +
            "FROM course\n" +
            "INNER JOIN module ON course.course_id = module.course_id\n" +
            "INNER JOIN module_details ON module.module_id = module_details.module_id\n" +
            "INNER JOIN users ON module_details.student_id = users.user_id WHERE module.module_id = ?\n" +
            "ORDER BY FIELD(module_status, \'Not delivered\', \'Pending\', \'Completed\');";
            
            
            try {
                PreparedStatement getPeople = conn.prepareStatement(sqlString);
                getPeople.setString(1, module_id);
                
                ResultSet rset = getPeople.executeQuery();
                
                out.println("<table class=\"module-students-table\">");
                out.println("<tr>");
                out.println("<th>Name</th>");
                out.println("<th>Your points</th>");
                out.println("<th>Status</th>");
                out.println("</tr>");
                
                
                while (rset.next()) {
                    String user_name = rset.getString("user_name");
                    String course_name = rset.getString("course_name");
                    String module_name = rset.getString("module_name");
                    String module_desc = rset.getString("module_desc");
                    String max_points = rset.getString("max points");
                    String your_points = rset.getString("your points");
                    String status = rset.getString("module_status");
                    
                    if (your_points == null) {
                        your_points = "LUL 0 POINTS";
                    }
                    
                    out.println("<tr>");
                    out.println("<td>" + user_name + "</td>");
                    out.println("<td>" + your_points + "</td>");
                    out.println("<td class=\"module_status\">" + status + "</td>");
                    out.println("</tr>");
                    
                }
                
                out.println("</table>");
                
                
            } catch (SQLException ex) {
                out.println("SQL error: " + ex);
            }
            
            
            out.println("</div>");
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
