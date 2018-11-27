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
@WebServlet(name = "serv_CreateFeedback", urlPatterns = {"/createFeedback"})
public class serv_CreateFeedback extends HttpServlet {
Login login = new Login();

    /**
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
            site.printHead("Feedbacking...", "feedbackiing");
            
            String content = request.getParameter("module_feedback_content");
            String module_id = request.getParameter("module_id");
            String user_id = request.getParameter("user_id");
            
            Connection conn = login.loginToDB(out);
            String author_id = UserHelper.getUserId(conn, request);
            try {
                String results = ModuleHelper.newModuleFeedback(conn, module_id, user_id, author_id, content);
                
                
                out.println(results);
                out.println("<form action=\"oneStudentModule#Feedback\" method=\"post\">");
                out.println("<input type=\"hidden\" name=\"module_id\" value=\"" + module_id + "\">");
                out.println("<input type=\"hidden\" name=\"user_id\" value=\"" + user_id + "\">");
                out.println("<input type=\"submit\" class=\"button\" value=\"Back to student module\">");
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
