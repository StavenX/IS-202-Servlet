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
import servlets.serv_Index;

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
     * @return username of logged in user
     */
    public String printHead (String title, String bodyId) {
        out.println("<!DOCTYPE html>");
        out.println("<html lang=\"en\">");
        out.println("<head>");
        out.println("<meta charset=\"UTF-8\">");
        out.println("<meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">");
        out.println("<link rel=\"icon\" href=\"images/Placeholder_v2.png\" type=\"image/png\">");
        out.println("<link rel=\"stylesheet\" type=\"text/css\" href=\"css/theme.css\">");
        out.println("<title>" + title + "</title>");            
        out.println("</head>");
        out.println("<body id=\"" + bodyId + "\" class=\"flex-page\">");
        printFile("nav.html");
        //useJS("navhide.js");
        out.println("<div class=\"page-container\">");
        String username = printUserDetails();
        return username;
    }
    
    public String printUserDetails() {
        out.println("<div class=\"top-bar\">");
        String loggedUserName;
        String loggedUserRole = "";
        try {
            AccessTokenHelper a = new AccessTokenHelper(request);
            loggedUserName = a.getUsername();
            loggedUserRole = a.getUserRole();
        } catch (Exception ex) {
            loggedUserName = "not implemented in this servlet | " + ex;
        }
        out.printf("<p class=\"user-details\">Logged in. | User: %s | Role %s </p>\n", loggedUserName, loggedUserRole);
        out.println("</div>");
        return loggedUserName;
    }
    
    public void printDeleteButton (String servletName, String entityPK, String entityID) {
        out.println("<form name=\"delete-form-" + entityID + "\" action=\"" + servletName + "\">");
        out.println("<input type=\"hidden\" name=\"" + entityPK + "\" value=\"" + entityID + "\">");
        out.println("<input class=\"button makesure-" + entityID + "\" type=\"button\" value=\"Delete\" onclick=\"makeSure(" + entityID + ");\"  style=\"display: inline-block\">");
        out.println("<p class=\"invisible makesure-" + entityID + "\">Really delete?<br></p>");
        out.println("<input class=\"invisible button makesure-" + entityID + "\" type=\"submit\" value=\"Yes\">");
        out.println("<input class=\"invisible button makesure-" + entityID + "\" type=\"button\" value=\"No\" onclick=\"makeSure(" + entityID + ");\">");
        out.println("</form>");
    }
    
    public void useJS(String filename) {
        out.println("<script src=\"js\\" + filename + "\"></script>");
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
    
    //use on html file to print it
    public void printFile(String filename) {
            //gets the path of the programs current location
            ClassLoader loader = serv_Index.class.getClassLoader();
            //goes up to directory levels to 'web' dir
            String path = loader.getResource("..\\..\\").toString();
            //removes first part of path
            path = path.replace("file:/", "");
            
            //adds filename to the path
            path += filename;
            
            //prints the html file
            try {
            out.println(readFile(path));
            } catch (IOException ex) {
                out.println("oopsie file couldnt load" + ex);
            }
    }
    
    
     public String readFile(String filename) throws IOException {
        String content = new String(Files.readAllBytes(Paths.get(filename)));
        return content;
    }
    
    
    /**
     * Prints a button that takes you back one step on the website
     */
    public void printBackButton() {
        out.println("<button class=\"button back-button\" onclick=\"window.history.back();\"><img src=\"images/back.svg\">Go back</button>");
    }
    
    /**
     * Prints the closing tag of body and html
     */
    public void printEnd () {
        out.println("</div>");
        useJS("navhide.js");
        out.println("</body>");
        out.println("</html>");
    }
    
    /**
     * Prints the closing tag of body and html and closes connection
     * @param login the connection to be closed
     */
    public void closeAndPrintEnd(Login login) {
        out.println("<p>Connection is: " + login.close() + " (remove before shipping)</p>");
        printEnd();
    }
}
