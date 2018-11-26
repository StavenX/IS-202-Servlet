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
 * @author Staven
 */
@WebServlet(name = "serv_ERROR404", urlPatterns = {"/ERROR404"})
public class serv_ERROR404 extends HttpServlet {



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
            
            site.printHead("404", "error-404");
            out.println("<p>Missing resource</p>");
            out.println("<h1>ERROR 404: PAGE NOT FOUND.</h1>");
            out.println("<p>The link is either dead or does not exist, make sure the URL was correct</p>");
            site.printEnd();
        }        
    }
    
}
