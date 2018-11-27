/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servlets;

import helpers.HtmlHelper;
import helpers.UserHelper;
import helpers.MessageHelper;
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
/**
 *
 * @author Tobias & Frank
 */
@WebServlet(name = "oneMessage", urlPatterns = {"/oneMessage"})
public class serv_OneMessage extends HttpServlet {
    Statement stmt;
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
            /* TODO output your page here. You may use following sample code. */
            HtmlHelper site = new HtmlHelper(out, request);
            site.printHead("Single message", "one-message-container");
            
            String singleMess_id = request.getParameter("mess_id");
            
            Connection conn;
            conn = login.loginToDB(out);

            out.println("<h2>Viewing a single message</h2>");
            
            MessageHelper.printOneMessage(out, conn, singleMess_id);
            
            //TODO box containing students
            out.println("<div class=\"message-list\"");
            out.println("<div class=\"message-list-item\">");
            out.println("<div>TODO: Table of messages</div>");
            out.println("</div>");
            out.println("</div>");
            
            
            site.printEnd();
            login.close();
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
