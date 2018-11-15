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
@WebServlet(name = "deleteUser", urlPatterns = {"/deleteUser"})
public class serv_DeleteUser extends HttpServlet {
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
            
            HtmlHelper site = new HtmlHelper(out);

            site.printHead("Delete user", "delete-user");
            
            out.println("<h1>Deletion page</h1>");
            
            Connection conn = login.loginToDB(out);
            
            String user_id = request.getParameter("user_id");
            
            PreparedStatement deleteUser;
            try {
                deleteUser = conn.prepareStatement("DELETE FROM users WHERE user_id = ?;");
                deleteUser.setString(1, user_id);
                
                int amountDeleted = deleteUser.executeUpdate();
                out.println("<div>" + amountDeleted + " users deleted.</div>");
                out.println("<form action=\"getUser\" method=\"get\"><button class=\"button\">Back to user list</button></form>");

            } catch (SQLException ex) {
                out.println("SQL error: " + ex);
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
        doGet(request, response);
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
