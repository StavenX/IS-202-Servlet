/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package errors;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import helpers.HtmlHelper; 

/**
 *
 * @author Tobias
 */
@WebServlet(name = "serv_ERROR403", urlPatterns = {"/ERROR403"})
public class serv_ERROR403 extends HttpServlet {



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

        try (PrintWriter out = response.getWriter()) {
            HtmlHelper site = new HtmlHelper(out, request);
            
            site.printHead("403", "error-403");
            out.println("<h1>ERROR 403: UNAUTHORIZED</h1>");
            out.println("<p>Your user role does not have permission to view this page</p>");
            site.printEnd();
        }        
    }
    
}
