/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package helpers;

import java.io.PrintWriter;

/**
 *
 * @author Tobias
 */
public class HtmlHelper {
    private PrintWriter out;
    
    
    public HtmlHelper (PrintWriter out) {
        this.out = out;
    }
    
    /**
     * Prints the start of the html page
     * @param title the title visible in the tab of your browser
     * @param bodyId the html class the body will be assigned
     */
    public void printHead (String title, String bodyId) {
        out.println("<!DOCTYPE html>");
        out.println("<html>");
        out.println("<head>");
        out.println("<meta charset=\"UTF-8\">");
        out.println("<link rel=\"icon\" href=\"placeholder_v1.png\" type=\"image/png\">");
        out.println("<link rel=\"stylesheet\" type=\"text/css\" href=\"theme.css\">");
        out.println("<title>Servlet getStudent</title>");            
        out.println("</head>");
        out.println("<body class=\"" + bodyId + "\">");
    }
    
    /**
     * Prints the closing tag of body and html
     */
    public void printEnd () {
        out.println("</body>");
        out.println("</html>");
    }
}
