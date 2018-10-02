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
/**
 *
 * @author Staven
 */
public class ModuleHelper {
    
        /**
     * Inserts a module into the module table.
     * 
     * TODO: Currently prone to SQL injection, needs to use
     * prepareStatement() instead
     * 
     * @param name The student name
     * @param edu The student's education
     * @param conn The connection object
     * @param out The printwriter, for printing errors etc
     */
    public static void insertModule(String name, String desc, Connection conn, PrintWriter out) {
        
        try {
            
            PreparedStatement prepInsert = conn.prepareStatement("INSERT INTO module (mod_name, mod_desc) values (?, ?);");
            prepInsert.setString(1, name);
            prepInsert.setString(2, desc);            
            
            System.out.println("The SQL query is: " + prepInsert.toString() ); // debug
            int countInserted = prepInsert.executeUpdate();         
            System.out.println(countInserted + " records inserted.\n");  
            out.println(countInserted + " records inserted.\n");  
            
            out.println("INSERTED"); // debug
            
            out.println(
                "<form action=\"getModule\" method=\"post\">\n" +
"                   <input type=\"Submit\" name=\"get\" value=\"Get all Modules from Database\">   \n" +
"               </form>");
        }
        catch (SQLException ex) {
            out.println("SQL error: " + ex);
        }
    }
    
    public static void updateModule(String id, String name, String desc, Connection conn, PrintWriter out) {
        
        try {
            
            PreparedStatement prepUpdate = conn.prepareStatement("UPDATE module SET mod_name = ?, mod_desc = ? WHERE mod_id = ?");
            prepUpdate.setString(1, name);
            prepUpdate.setString(2, desc);
            prepUpdate.setString(3, id);
            
            System.out.println("The SQL query is: " + prepUpdate.toString() ); // debug
            int countInserted = prepUpdate.executeUpdate();         
            System.out.println(countInserted + " records inserted.\n");  
            out.println(countInserted + " records inserted.\n");  
            
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
    public static void printModules(PrintWriter out, Connection conn) {

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
                String mod_id = rset.getString("mod_id");
                String mod_name = rset.getString("mod_name");
                String mod_desc = rset.getString("mod_desc");
                //out.println("<div>Row " + rowCount + ": " + mod_id + ": " + mod_name + ", " + mod_desc + "</div>");
                
                
                out.println("<form class=\"module-container\" action=\"oneModule\">");
                out.println("<input class=\"invisible\" name=\"singleMod_id\" value=\"" + mod_id + "\">");
                out.println("<div>Row " + rowCount + "</div>");
                out.println("<div name=\"modid\">Module Id:" + mod_id + "</div>");
                out.println("<div>Name:" + mod_name + "</div>");
                out.println("<div>Description:" + mod_desc + "</div>");
                out.println("<input type=\"submit\" value=\"Details\" class=\"more-info-button\">");
                out.println("</form>");
                rowCount++;
            }
            out.println("Total number of records: " + rowCount);
            conn.close();
        }
        catch (SQLException ex) {
            out.println("SQL error: " + ex);
        }
        catch (Exception e) {
            out.println("Something wrong happened: " + e);
        }       
    }
    
    public static void printOneModule(PrintWriter out, Connection conn, String singleMod_id) {
        PreparedStatement getOneModule;
        
        try {
            getOneModule = conn.prepareStatement("SELECT * FROM module WHERE mod_id = ?");
            getOneModule.setString(1, singleMod_id);
            
            ResultSet rset = getOneModule.executeQuery();
            
            while (rset.next()) {
                String mod_id = rset.getString("mod_id");
                String mod_name = rset.getString("mod_name");
                String mod_desc = rset.getString("mod_desc");
                out.println("<div>");
                out.println("<form action=\"updateModule\">");
                out.println("<input type=\"text\" name=\"singleMod_id\" value=\"" + mod_id + "\" disabled>");
                out.println("<input type=\"text\" name=\"mod_name\" value=\"" + mod_name + "\" disabled>");
                out.println("<input type=\"text\" name=\"mod_desc\" value=\"" + mod_desc + "\" disabled>");
                out.println("<input id=\"one-module-edit\" type=\"button\" value=\"Edit module\" onclick=\"enable();\">");
                out.println("<input id=\"one-module-save\" type=\"submit\" value=\"Save\">");
                out.println("</form>");
                out.println("<form action=\"createDeliverables\"><input type=\"submit\" value=\"Create Deliverables\"></form>");
                out.println("</div>");
                
            }
                
        }
        
        catch (SQLException ex) {
            out.println("SQL error: " + ex);
        }
    }
    
}
