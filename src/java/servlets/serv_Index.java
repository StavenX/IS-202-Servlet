/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servlets;

import helpers.HtmlHelper;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author tobia
 */
@WebServlet(name = "serv_Index", urlPatterns = {"/Index"})
public class serv_Index extends HttpServlet {

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
            HtmlHelper site = new HtmlHelper(out);
            site.printHead("Home", "home");
            
            out.println("<div class=\"courses-container\">");
            for (int i = 0; i < 4; i++) {
                courseBox(out, "Course" + (i+1));
            }
            out.println("</div>");
            
        }
    }

    public void courseBox(PrintWriter out, String courseTitle) {
        out.println("<form action=\"getCourse\" method=\"get\">");
        out.println("<button class=\"course-container\">");
        out.println("<img class=\"course-img\" src=\"http://via.placeholder.com/200x100\">");
        out.println("<input name=\"course\" type=\"hidden\" value=\"" + courseTitle + "\">");
        out.println("<h2 name=\"" + courseTitle + "\" class=\"course-title\">" + courseTitle + "</h2>");
        out.println("<p class=\"course-name\">Lorem ipsum</p>");
        out.println("</button>");
        out.println("</form>");
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
