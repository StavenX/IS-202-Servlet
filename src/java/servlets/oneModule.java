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
 * @author Tobias
 */
@WebServlet(name = "oneModule", urlPatterns = {"/oneModule"})
public class oneModule extends HttpServlet {
    Statement stmt;
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
            HtmlHelper site = new HtmlHelper(out);
            site.printHead("Single module", "one-module-container");
            
            String singleMod_id = request.getParameter("singleMod_id");
            
            Connection conn;
            conn = login.loginToDB(out);

            ModuleHelper.printOneModule(out, conn, singleMod_id);
            
            out.println("<div class=\"module-student-list\"");
            
            out.println("<div class=\"module-student-list-item\">");
            out.println("<div>TODO: Table of students</div>");
            out.println("</div>");
            
            out.println("</div>");
            
            
            
            

            login.close();
            
            
            
            out.println("<script>");
            out.println("   function enable() {");
            //out.println("       document.getElementById(\'one-module-edit\').style.padding = \'20px\'");
            out.println("       var inputs = document.getElementsByTagName(\'input\');");
            out.println("       for (var i = 0; i < inputs.length; i++) {");
            out.println("           if (inputs[i].type == 'text') {");
            out.println("               inputs[i].disabled = false;");
            out.println("               inputs[i].setAttribute(\'class\',\'one-module-enabled\');");
            out.println("           }");
            out.println("       }");
            out.println("       document.getElementById(\'one-module-edit\').style.display = \'none\';");
            out.println("       document.getElementById(\'one-module-save\').style.display = \'block\';");
            out.println("   }");
            out.println("</script>");
            
            
            site.printEnd();
        }
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
