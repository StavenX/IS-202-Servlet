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
@WebServlet(name = "createUser", urlPatterns = {"/createUser"})
public class serv_CreateUser extends HttpServlet {

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
        response.setCharacterEncoding("UTF-8");
        request.setCharacterEncoding("UTF-8"); 
        
        try (PrintWriter out = response.getWriter()) {
            HtmlHelper site = new HtmlHelper(out);
            site.printHead("New user", "create-user");
            out.println("");
            out.println("<form action=\"createUser\" method=\"post\">");
            out.println("<input class=\"student-input\" type=\"text\" name=\"user_name\" placeholder=\"Insert username\">");   
            out.println("<input class=\"student-input\" type=\"password\" name=\"user_password\" placeholder=\"Insert password\">");   
            out.println("<input class=\"student-input\" type=\"text\" name=\"user_role\" placeholder=\"Insert role\">");   
            out.println("<input class=\"student-input\" type=\"text\" name=\"user_fname\" placeholder=\"Insert first name\">");
            out.println("<input class=\"student-input\" type=\"text\" name=\"user_lname\" placeholder=\"Insert last name\">");
            out.println("<input class=\"button\" type=\"Submit\" name=\"get\" value=\"Create\">");
            out.println("</form>");
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
            site.printHead("New user created", "create-user");
            
                Connection conn;
                conn = login.loginToDB(out);
                
                StudentHelper.insertUser(
                        request.getParameter("user_name"),
                        request.getParameter("user_password"),
                        request.getParameter("user_role"),
                        request.getParameter("user_fname"),
                        request.getParameter("user_lname"),
                        conn, 
                        out
                );                
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
    }

}
