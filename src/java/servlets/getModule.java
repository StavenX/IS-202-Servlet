package servlets;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
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
public class getModule extends HttpServlet {

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
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<link rel=\"stylesheet\" type=\"text/css\" href=\"theme.css\">");
            out.println("<title>Servlet getStudent</title>");            
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet getStudent at " + request.getContextPath() + "</h1>");
            
                Login login = new Login();
                
                Connection conn;
                conn = login.loginToDB(out);
                
                printModules(out, conn);
                login.close();
                
            out.println("</body>");
            out.println("</html>");
        }
    }
    
    public void printModules(PrintWriter out, Connection conn) {

        PreparedStatement getModules; 
        
        try {
            getModules = conn.prepareStatement("SELECT * FROM module ORDER BY ?");
            getModules.setString(1, "mod_id");
                       
            ResultSet rset = getModules.executeQuery();
            
            out.println("the records selected are:" + "<br>");
            int rowCount = 0; 
            
            // While there exists more entries (rows?)
            while (rset.next()) {               
                // The different columns
                String moduleID = rset.getString("mod_id");
                String moduleName = rset.getString("mod_name");
                String moduleDescription = rset.getString("mod_desc");
                out.println("Row " + rowCount + ": " + moduleID + ": " + moduleName + ", " + moduleDescription + "<br>");
                rowCount++;
            }
            out.println("Total number of records = " + rowCount);
        }
        catch (SQLException ex) {
            out.println("FAILED TO RETRIEVE " + ex);
        }
        catch (Exception e) {
            out.println("Something wrong happened.");
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
