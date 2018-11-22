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
import helpers.ModuleHelper;
import helpers.UserHelper;
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
            doPost(request, response);
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
            
                
            String module_id = request.getParameter("module_id");
            String user_id = request.getParameter("user_id");
            
            String role = UserHelper.getUserRole(request);

            
            out.println("<form action=\"oneModule\" method=\"get\">");
            out.println("<input type=\"hidden\" name=\"module_id\" value=\"" + module_id + "\">");
            out.println("<input type=\"submit\" class=\"button\" value=\"Back to module\">");
            out.println("</form>");
            
            
            Connection conn = login.loginToDB(out);
            String current_user = UserHelper.getUserId(conn, UserHelper.getUserName(request));
                
            ModuleHelper.printOneStudentModule(out, conn, module_id, user_id);
            
            if (role.equals("Lecturer")) {             

                out.println("<h2> Grade this module: </h2>");
                out.println("<form id=\"grade\" action=\"gradeModule\" method=\"post\">");
                out.println("<input type=\"hidden\" name=\"module_id\" value=\"" + module_id +"\">");
                out.println("<input type=\"hidden\" name=\"user_id\" value=\"" + user_id + "\">");
                out.println("<input type=\"text\" name=\"new_points\" placeholder=\"Points\">");
                out.println("</form>");

                out.println("<textarea class=\"message-input\" rows=\"4\" cols=\"50\" name=\"comment\" form=\"grade\" placeholder=\"Delivery comment (currently sent into the void)\"></textarea>");

                out.println("<button class=\"button\" onclick=\"submit(\'grade\');\">Grade</button>");

            }
            
            out.println("<h2 id=\"Feedback\">Feedback on module delivery:</h2>");

            out.println("<div class=\"new-feedback-container\">");
            out.println("<form id=\"newFeedback\" action=\"createFeedback\" method=\"post\">");
            out.println("<input type=\"hidden\" name=\"author_id\" value=\"" + current_user + "\">");
            out.println("<input type=\"hidden\" name=\"module_id\" value=\"" + module_id + "\">");
            out.println("<input type=\"hidden\" name=\"user_id\" value=\"" + user_id + "\">");
            out.println("</form>");
            out.println("<textarea form=\"newFeedback\" name=\"module_feedback_content\" class=\"new-comment-input\" placeholder=\"Write a comment...\"></textarea>");
            out.println("<button class=\"button\" onclick=\"submit(\'newFeedback\');\">Post feedback</button>");
            out.println("</div>");                
                
            ModuleHelper.printModuleFeedback(out, conn, module_id, user_id, request);
                
            site.useJS("submitform.js");
            site.useJS("buttons-for-delete.js");
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
