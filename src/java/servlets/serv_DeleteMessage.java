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
import helpers.MessageHelper;
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
 * @author Frank
 */
@WebServlet(name = "deleteMessage", urlPatterns = {"/deleteMessage"})
public class serv_DeleteMessage extends HttpServlet {
    Login login = new Login();

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
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
            site.printHead("Delete message", "delete-message");
            
            out.println("<h1>Servlet deleteMessage at " + request.getContextPath() + "</h1>");
            
            Connection conn = login.loginToDB(out);
            
            String mess_id = request.getParameter("mess_id");
            
            PreparedStatement deleteMessage;
            try {
                deleteMessage = conn.prepareStatement("DELETE FROM message WHERE mess_id = ?;");
                deleteMessage.setString(1, mess_id);
                
                int amountDeleted = deleteMessage.executeUpdate();
                out.println("<div>" + amountDeleted + " message deleted.</div>");
                out.println("<form action=\"getMessage\"><button class=\"button\">Back to message list</button></form>");
            } catch (SQLException ex) {
                out.println("SQL error: " + ex);
            }
            
            site.printEnd();
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
