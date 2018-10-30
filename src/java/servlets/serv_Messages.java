/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servlets;

import helpers.HtmlHelper;
import helpers.MessageHelper;
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
 * @author Staven
 */
@WebServlet(name = "serv_Messages", urlPatterns = {"/Message"})
public class serv_Messages extends HttpServlet {

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
        response.setCharacterEncoding("UTF-8");
        request.setCharacterEncoding("UTF-8"); 
        
        try (PrintWriter out = response.getWriter()) {
            HtmlHelper site = new HtmlHelper(out);
            site.printHead("Message", "");
            out.println("<h1> Create a new message </h1>");
            out.println("<div class =\"form1\">");
            out.println("<form action=\"servMessage\" method=\"post\"> ");
            out.println("<input class=\"message-input\" type=\"text\" name=\"mess_senderId\" placeholder=\"Insert senderId\">");
            out.println("<input class=\"message-input\" type=\"text\" name=\"mess_title\" placeholder=\"Insert title\">");
            out.println("<input class=\"message-input\" type=\"text\" name=\"mess_content\" placeholder=\"Insert content\">");
            out.println("<input type=\"Submit\" name=\"get\" value=\"Create\">");
            out.println("</form>");
            out.println("</div>");  
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
        response.setContentType("text/html;charset=UTF-8");
        response.setCharacterEncoding("UTF-8");
        request.setCharacterEncoding("UTF-8"); 
        
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            
            HtmlHelper site = new HtmlHelper(out);
            site.printHead("New Message", "create-message");
            
                Connection conn;
                conn = login.loginToDB(out);
                
                MessageHelper.insertMessage(
                        
                        request.getParameter("mess_senderId"),
                        request.getParameter("mess_title"),
                        request.getParameter("mess_content"),
                        conn, 
                        out
                );
                
                login.close();
                
            site.printEnd();
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
