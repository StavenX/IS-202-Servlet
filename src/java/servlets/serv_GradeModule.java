/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servlets;

import helpers.HtmlHelper;
import helpers.ModuleHelper;
import helpers.UserHelper;
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
 * @author Tobias
 */
@WebServlet(name = "serv_GradeModule", urlPatterns = {"/gradeModule"})
public class serv_GradeModule extends HttpServlet {
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
            HtmlHelper site = new HtmlHelper(out, request);
            site.printHead("Graded", "");
            
            
            Connection conn = login.loginToDB(out);
            String module_id = request.getParameter("module_id");
            String user_id = request.getParameter("user_id");
            String new_points = request.getParameter("new_points");
            String comment = request.getParameter("comment");
            out.println(comment);
                       
            String current_user = UserHelper.getUserId(conn, UserHelper.getUserName(request));
            try {
                PreparedStatement grade = conn.prepareStatement("UPDATE module_details SET module_points = ?, module_status = \'Completed\'\n" +
                                                                "WHERE module_ID = ? AND student_id = ?;");
                grade.setString(1, new_points);
                grade.setString(2, module_id);
                grade.setString(3, user_id);
                
                grade.executeUpdate();
                
                ModuleHelper.newModuleFeedback(conn, module_id, user_id, current_user, comment);
            } catch (SQLException ex) {
                out.println(ex);
            }
            
            
            out.println("<form name=\"back\" action=\"oneStudentModule\" method=\"post\">");
            out.println("<input type=\"hidden\" name=\"module_id\" value=\"" + module_id +"\">");
            out.println("<input type=\"hidden\" name=\"user_id\" value=\"" + user_id + "\">");
            out.println("<input class=\"button\" type=\"submit\" value=\"Back\">");
            out.println("</form>");
            
            //out.println("<script>window.onload=document.forms[\'back\'].submit();</script>");

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
