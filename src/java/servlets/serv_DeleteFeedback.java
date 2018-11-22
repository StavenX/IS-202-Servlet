/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servlets;

import helpers.HtmlHelper;
import helpers.ModuleHelper;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
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
@WebServlet(name = "serv_deleteFeedback", urlPatterns = {"/deleteFeedback"})
public class serv_DeleteFeedback extends HttpServlet {
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
            site.printHead("Deleted feedback", "deleted-feedback");
            Connection conn = login.loginToDB(out);
            
            String module_id = request.getParameter("module_id");
            String user_id = request.getParameter("user_id");
            String module_feedback_id = request.getParameter("module_feedback_id");
            String results = ModuleHelper.deleteModuleFeedback(conn, module_feedback_id);
            out.println(results);
            out.println("<form action=\"oneStudentModule#Feedback\" method=\"post\">");
            out.println("<input type=\"hidden\" name=\"module_id\" value=\"" + module_id + "\">");
            out.println("<input type=\"hidden\" name=\"user_id\" value=\"" + user_id + "\">");
            out.println("<input type=\"submit\" class=\"button\" value=\"Back to module\">");
            out.println("</form>");
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
            doGet(request, response);
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
