package servlets;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


import helpers.AccessTokenHelper;
import helpers.HtmlHelper;
import helpers.ModuleHelper;
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

/**
 *
 * @author Staven
 */
@WebServlet(name = "getModule", urlPatterns = {"/getModule"})
public class serv_GetModule extends HttpServlet {

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
            /* TODO output your page here. You may use following sample code. */
            
            HtmlHelper site = new HtmlHelper(out, request);

            site.printHead("Modules", "bodyy");
            
            out.println("<h1>All modules</h1>");
            
                Connection conn;
                conn = login.loginToDB(out);
                
                //is null if first time entering the page, which is handled by a
                //'default' in a switch in printModules()
                String orderBy = request.getParameter("orderBy");
                if (orderBy == null) {
                    orderBy = "";
                }
                String direction = request.getParameter("orderDirection");
                direction = (direction == null) ? "" : direction;
                
            AccessTokenHelper a = new AccessTokenHelper(request);
            String role = a.getUserRole();
                
                ModuleHelper.printModules(out, conn, orderBy, direction, role, "%", "getModule");
                
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
