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
    
    public void printHead (String title, String bodyId) {
        out.println("<!DOCTYPE html>");
        out.println("<html>");
        out.println("<head>");
        out.println("<meta charset=\"UTF-8\">");
        out.println("<link rel=\"icon\" href=\"placeholder_v1.svg\" type=\"image/svg\">");
        out.println("<link rel=\"stylesheet\" type=\"text/css\" href=\"theme.css\">");
        out.println("<title>Servlet getStudent</title>");            
        out.println("</head>");
        out.println("<body>");
    }
    
    public void printEnd () {
        out.println("</body>");
        out.println("</html>");
    }
}
