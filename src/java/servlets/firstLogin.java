/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import network.Login;

/**
 *
 * @author tobia
 */
@WebServlet(name = "firstLogin", urlPatterns = {"/firstLogin"})
public class firstLogin extends HttpServlet {
    Login login = new Login();
    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            /* TODO output your page here. You may use following sample code. */
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet firstLogin</title>");            
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet firstLogin at " + request.getContextPath() + "</h1>");
            
            Connection conn = login.loginToDB(out);
            PreparedStatement getUsers;
            try {
                getUsers = conn.prepareStatement("SELECT * FROM users ORDER BY ?");
                getUsers.setString(1, "users_id");
                
                ResultSet rset = getUsers.executeQuery();
                
                String tryUserName = request.getParameter("username");
                String tryPassword = request.getParameter("password");
                boolean correctInfo = false;
                
                while (rset.next()) {
                    
                    String users_id = rset.getString("users_id");
                    String users_username = rset.getString("users_username");
                    String users_password = rset.getString("users_password");

                    //checks the entered username and password against all db entries
                    if (tryUserName.equals(users_username)) {
                        if (tryPassword.equals(users_password)) {
                            correctInfo = true;
                            out.println("<form action=\"index.html\"><input type=\"submit\" value=\"Go to home\"></form>");
                        }
                    }
                }
                if (!correctInfo) {
                    wrongLogin(out);
                }
                
            } catch (SQLException e) {
                out.println("sql exception: " + e);
            }
            
            
            out.println("</body>");
            out.println("</html>");
        }
    }
    
    public void wrongLogin(PrintWriter out) {
        out.println("Username or password was wrong");
    }

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
        processRequest(request, response);
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
        processRequest(request, response);
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
