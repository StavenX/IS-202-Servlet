/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package helpers;

import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
/**
 *
 * @author Frank
 */
public class MessageHelper {
    
        /**
     * Inserts a student into the student table.
     * 
     * TODO: Currently prone to SQL injection, needs to use
     * prepareStatement() instead
     * 
     * @param senderId who is sending the message (user_id)
     * @param recipient who is receiving the message
     * @param title title of the message
     * @param content content of the message
     * @param conn The connection object
     * @param out The printwriter, for printing errors etc
     */
    public static void insertMessage(String senderId, String recipient, String title, String content, Connection conn, PrintWriter out) {
        
        try {
            
            HtmlHelper site = new HtmlHelper(out);
            
            PreparedStatement prepInsert = conn.prepareStatement("INSERT INTO message (mess_senderId, mess_recipient, mess_title, mess_content) values ( ?, ?, ?, ?);");
            
            prepInsert.setString(1, site.checkIfValidText(senderId));
            prepInsert.setString(2, site.checkIfValidText(recipient));
            prepInsert.setString(3, site.checkIfValidText(title));            
            prepInsert.setString(4, site.checkIfValidText(content));            
            
            
            System.out.println("The SQL query is: " + prepInsert.toString() ); // debug
            int countInserted = prepInsert.executeUpdate();         
            System.out.println(countInserted + " records inserted.\n");  
            out.println(countInserted + " records inserted.\n");  
            
            // The button that prints all messages
            out.println(
                "<form action=\"getMessage\" method=\"post\">\n" +
"                   <input class=\"button\" type=\"Submit\" name=\"get\" value=\"Get all Messages from Database\">   \n" +
"               </form>");
        }
        catch (SQLIntegrityConstraintViolationException ex) {
            out.println("One or more mandatory fields were empty, please try again");
            out.println("<button class=\"button\" onclick=\"window.history.back();\">Go back</button>");
        }
        catch (SQLException ex) {
            out.println("SQL error: " + ex);
        }
    }
    
    
    
    /**
     * Prints all the students located in the student
     * table.
     * 
     * @param out The printwriter to write with
     * @param conn The connection to use
     */
    public static void printMessages(PrintWriter out, Connection conn, String userId) {

        HtmlHelper site = new HtmlHelper(out);
        PreparedStatement getMessage; 
        
        try {
            getMessage = conn.prepareStatement("SELECT * FROM message WHERE mess_senderId LIKE ? OR mess_recipient LIKE ? ORDER BY mess_senderId");
            getMessage.setString(1, userId);
            getMessage.setString(2, userId);
                       
            ResultSet rset = getMessage.executeQuery();
            
            out.println("the records selected are:" + "<br>");
            int rowCount = 0; 
            
            // While there exists more entries (rows?)
            out.println("<div class=\"message-flex-container\">");
            while (rset.next()) {               
                // The different columns
                String mess_id = rset.getString("mess_id");
                String mess_senderId = rset.getString("mess_senderId");
                String mess_title = rset.getString("mess_title");
                String mess_content = rset.getString("mess_content");
                String mess_recipient = rset.getString("mess_recipient");
                
                out.println("<div class=\"message-container\">");
                //out.println("<div class=\"message-bold-container\">");

                //form containing student information
                out.println("<div class=\"message-container-item\">");
                out.println("<form  action=\"oneMessage\">");
                out.println("<input class=\"invisible\" name=\"mess_id\" value=\"" + mess_id + "\">");
                out.println("<div>Row " + rowCount + "</div>");
                out.println("<div name=\"mess_id\"><b>mess_id:  " + "</b>" + mess_id + "</div>");
                out.println("<div><b>To:  " + "</b>" + mess_recipient + "</div>");
                out.println("<div><b>Name:  " + "</b>" + mess_senderId + "</div>");
                out.println("<div><b>Title:  " +  "</b>" + mess_title + "</div>");
                out.println("<div><b>Content:  " + "</b>"+ mess_content + "</div>");

                //out.println("</div>");
                out.println("</div>");

                out.println("</form>");
                
                //delete buttons
                out.println("<div class=\"message-container-item\">");                
                out.println("<form name=\"delete-form-" + mess_id + "\" action=\"deleteMessage\">");
                site.printDeleteButton("deleteMessage", "mess_id", mess_id);
                out.println("</div>");
                
                out.println("</div>");
                rowCount++;
            }
            out.println("</div>");
            out.println("Total number of records: " + rowCount);
            
            site.useJS("buttons-for-delete.js");
            
        }
        catch (SQLException ex) {
            out.println("SQL error: " + ex);
        }
        catch (Exception e) {
            out.println("Something wrong happened: " + e);
        }       
    }
    
    /**
     * Prints more details about one student
     * @param out
     * @param conn
     * @param stid 
     */
    public static void printOneMessage(PrintWriter out, Connection conn, String stid) {
        PreparedStatement getOneMessage;
        
        try {
            //sql statement
            getOneMessage = conn.prepareStatement("SELECT * FROM message WHERE mess_id = ?");
            getOneMessage.setString(1, stid);
            ResultSet rset = getOneMessage.executeQuery();
            
            //loop only executes once but is necessary?
            while (rset.next()) {
                String mess_id = rset.getString("mess_id");
                String mess_recipient = rset.getString("mess_recipient");
                String mess_senderId = rset.getString("mess_senderId");
                String mess_title = rset.getString("mess_title");
                String mess_content = rset.getString("mess_content");

                out.println("<div>");
                out.println(mess_id + mess_senderId + mess_recipient + mess_title + mess_content);
                out.println("</div>");
                
            }
                
        }
        
        catch (SQLException ex) {
            out.println("SQL error: " + ex);
        }
    }
}
