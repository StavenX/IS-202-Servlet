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

import helpers.*;
import java.sql.Connection;
import java.sql.Statement;
import network.Login;

/**
 *
 * @author Tobias
 */
@WebServlet(name = "oneUser", urlPatterns = {"/oneUser"})
public class serv_OneUser extends HttpServlet {
    
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
            site.printHead("Single user", "one-student-container");
            
            String user_id = request.getParameter("user_id");
            AccessTokenHelper a = new AccessTokenHelper(request);
            String username = a.getUsername();
            
            Connection conn;
            conn = login.loginToDB(out);

            
            out.println("<h2>Viewing a single user</h2>");
            
            UserHelper.printUserPage(out, conn, user_id);
            
            String currentUserId = UserHelper.getUserId(conn, username);
            if (!currentUserId.equals(user_id)) {
                out.println("<form action=\"Message\" method=\"get\">");
                out.println("<input type=\"hidden\" name=\"abc=\"" + currentUserId + "\">");
                out.println("<input type=\"hidden\" name=\"def=\"" + user_id + "\">");
                out.println("<input type=\"submit\" class=\"button\" value=\"Send message\">");
                out.println("</form>");
            }
            

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
