/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package helpers;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import javax.servlet.http.HttpServletRequest;
import network.Login;

/**
 *
 * @author Tobias
 */
public class HtmlHelper {
    private PrintWriter out;
    private HttpServletRequest request;
    
    
    public HtmlHelper (PrintWriter out) {
        this.out = out;
    }
    
    public HtmlHelper (PrintWriter out, HttpServletRequest request) {
        this.request = request;
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
        out.println("<meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">");
        out.println("<link rel=\"icon\" href=\"images/Placeholder_v2.png\" type=\"image/png\">");
        out.println("<link rel=\"stylesheet\" type=\"text/css\" href=\"css/theme.css\">");
        out.println("<title>" + title + "</title>");            
        out.println("</head>");
        //out.println(nav());
        //printNav();
        out.println("<body id=\"" + bodyId + "\">");
        out.println("<form action=\"http://localhost:8084/WEB/\"> <button class=\"button button-home\">Go home</button> </form>");
        
        String loggedUserName;
        String loggedUserRole = "";
        try {
            AccessTokenHelper a = new AccessTokenHelper(request);
            loggedUserName = a.getUsername();
            loggedUserRole = a.getUserRole();
        } catch (Exception ex) {
            loggedUserName = "not implemented in this servlet | " + ex;
        }
        out.printf("<p>Logged in. | User: %s | Role %s </p>\n", loggedUserName, loggedUserRole);
    }
    
    public void printDeleteButton (String servletName, String entityPK, String entityID) {
                out.println("<form name=\"delete-form-" + entityID + "\" action=\"" + servletName + "\">");
                out.println("<input class=\"invisible\" name=\"" + entityPK + "\" value=\"" + entityID + "\">");
                out.println("<input class=\"button makesure-" + entityID + "\" type=\"button\" value=\"Delete\" onclick=\"makeSure(" + entityID + ");\"  style=\"display: inline-block\">");
                out.println("<p class=\"invisible makesure-" + entityID + "\">Really delete?<br></p>");
                out.println("<input class=\"invisible button makesure-" + entityID + "\" type=\"submit\" value=\"Yes\">");
                out.println("<input class=\"invisible button makesure-" + entityID + "\" type=\"button\" value=\"No\" onclick=\"makeSure(" + entityID + ");\">");
                out.println("</form>");
    }
        
    //javascript for handling delete buttons
    public void printJsForDeleteButton() {
        out.println("<script src=\"buttons-for-delete.js\"></script>");
    }
    
    public void useJS(String filename) {
        out.println("<script src=\"" + filename + "\"></script>");
    }
    
    public String checkIfValidText(String toCheck) {
        //returns null if the string is empty, to prevent empty strings being inserted
        //into database (columns have 'NOT NULL' property
        if (toCheck.equals("")) {
            return null;
        }
        String checked = "";
        String[] letters = toCheck.split("");
            //replaces '<' and '>' with unicode symbols, so the page doesn't treat them as html code
            //(prevents images etc being posted instead of text
            for (String letter : letters) {
                if (letter.equals("<")) {
                    letter = "&#x003C";
                }
                if (letter.equals(">")) {
                    letter = "&#x003E";
                }
                //adds each letter to a new string to be used later
                checked += letter;
            }
        return checked;
    }
    
    //only works with absolute path
    public String nav() {
        String contents = "heihei";
        try {
            contents = new String(Files.readAllBytes(Paths.get("C:\\Users\\tobia\\Documents\\IS-202-Servlet\\web\\nav.html")));
        } catch (IOException ex) {
            out.println(ex);
        }
        return contents;
    }
    
    
    /**
     * Prints a button that takes you back one step on the website
     */
    public void printBackButton() {
        out.println("<button class=\"button\" onclick=\"window.history.back();\">Go back</button>");
    }
    
    /**
     * Prints the closing tag of body and html
     */
    public void printEnd () {
        out.println("</body>");
        out.println("</html>");
    }
    
    /**
     * Prints the closing tag of body and html and closes connection
     * @param login the connection to be closed
     */
    public void closeAndPrintEnd(Login login) {
        login.close();
        printEnd();
    }
}
