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
     * @param name
     * @param desc
     * @param points
     * @param conn The connection object
     * @param out The printwriter, for printing errors etc
     */
        
    public static void insertModule(String name, String desc, String points, Connection conn, PrintWriter out) {
        
        try {
            
            HtmlHelper site = new HtmlHelper(out);
            
            PreparedStatement prepInsert = conn.prepareStatement("INSERT INTO module (module_name, module_desc, module_points) values (?, ?, ?);");
            prepInsert.setString(1, site.checkForHtmlTags(name));
            prepInsert.setString(2, site.checkForHtmlTags(desc));
            prepInsert.setString(3, site.checkForHtmlTags(points));    
            
            System.out.println("The SQL query is: " + prepInsert.toString() ); // debug
            int countInserted = prepInsert.executeUpdate();         
            System.out.println(countInserted + " records inserted.\n");  
            out.println(countInserted + " records inserted.\n");  
            
            out.println("INSERTED"); // debug
            
            out.println(
                "<form action=\"getModule\" method=\"post\">\n" +
"                   <input class=\"button\" type=\"Submit\" name=\"get\" value=\"Get all Modules from Database\">   \n" +
"               </form>");
        }
        catch (SQLException ex) {
            if (ex.getMessage().contains("Incorrect integer value")) {
                out.println("Module points must be an integer, try again");
            } else {
                out.println("SQL error: " + ex.getMessage());
            }
        }
    }
    
    public static void updateModule(String id, String name, String desc, String points, Connection conn, PrintWriter out) {
        
        try {
            
            PreparedStatement prepUpdate = conn.prepareStatement("UPDATE module SET module_name = ?, module_desc = ?, module_points = ? WHERE module_id = ?");
            prepUpdate.setString(1, name);
            prepUpdate.setString(2, desc);
            prepUpdate.setString(3, points);
            prepUpdate.setString(4, id);
            
            System.out.println("The SQL query is: " + prepUpdate.toString() ); // debug
            int countInserted = prepUpdate.executeUpdate();         
            System.out.println(countInserted + " records inserted.\n");  
            out.println(countInserted + " records updated.\n");  
            
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
     * @param orderBy the column name to order the sql results in
     */
    public static void printModules(PrintWriter out, Connection conn, String orderBy) {

            HtmlHelper site = new HtmlHelper(out);
            PreparedStatement getModules; 
        
        try {
            
            //base string for sql preparedstatement
            String sqlString = "SELECT * FROM module ORDER BY ";
            
            //based on @param orderBy, something is added to complete the string
            //values received from "sort by" buttons at the top of page
            switch (orderBy.toLowerCase()) {                            
                case "name":    sqlString += "module_name";
                                break;
                                
                case "points":  sqlString += "module_points";
                                break;
                
                case "id":
                default:    sqlString += "module_id";
            }
            
            //preparedstatement is prepared and executed
            getModules = conn.prepareStatement(sqlString);
            ResultSet rset = getModules.executeQuery();
            
            
            out.println("the records selected are:" + "<br>");
            int rowCount = 0; 
            
            //"sort by"-buttons (can probably be reduced to one form)
            out.println("<h2>Sort by: </h2>");
            out.println("<form action=\"getModule\">");
            out.println("<input class=\"button\" type=\"submit\" name=\"orderBy\" value=\"ID\">");
            out.println("</form>");
            out.println("<form action=\"getModule\">");
            out.println("<input class=\"button\" type=\"submit\" name=\"orderBy\" value=\"Name\">");
            out.println("</form>");
            out.println("<form action=\"getModule\">");
            out.println("<input class=\"button\" type=\"submit\" name=\"orderBy\" value=\"Points\">");
            out.println("</form>");
            
            // While there exists more entries (rows?)
            while (rset.next()) {               
                // The different columns from the db
                String module_id = rset.getString("module_id");
                String module_name = rset.getString("module_name");
                String module_desc = rset.getString("module_desc");
                String module_points = rset.getString("module_points");
                
                //the module info in a container
                out.println("<div class=\"module-container\">");
                out.println("<form action=\"oneModule\" method=\"post\">");
                out.println("<input class=\"invisible\" name=\"module_id\" value=\"" + module_id + "\">");
                out.println("<div>Row " + rowCount + "</div>");
                out.println("<div name=\"modid\">Module Id:" + module_id + "</div>");
                out.println("<div>Name:" + module_name + "</div>");
                out.println("<div>Description:" + module_desc + "</div>");
                out.println("<div>Max points:" + module_points + "</div>");
                out.println("<input class=\"button more-info-button\" type=\"submit\" value=\"Details\">");
                out.println("</form>");
                site.printDeleteButton("deleteModule", "module_id", module_id);
                out.println("</div>");
                
                rowCount++;
            }
            out.println("Total number of records: " + rowCount);
            
            site.printJsForDeleteButton();
            
            conn.close();
        }
        catch (SQLException ex) {
            out.println("SQL error: " + ex);
        }
        catch (Exception e) {
            out.println("Something wrong happened: " + e);
        }       
    }
    
    public static void printOneModule(PrintWriter out, Connection conn, String module_id) {
        PreparedStatement getOneModule;
        
        try {
            getOneModule = conn.prepareStatement("SELECT * FROM module WHERE module_id = ?");
            getOneModule.setString(1, module_id);
            
            ResultSet rset = getOneModule.executeQuery();
            
            while (rset.next()) {
                String module_name = rset.getString("module_name");
                String module_desc = rset.getString("module_desc");
                String module_points = rset.getString("module_points");
                out.println("<div>");
                out.println("<form action=\"updateModule\">");
                out.println("<div class=\"inline-block module-edit-input\">");
                out.println("<p>Module ID (can't be changed)</p>");
                out.println("<input type=\"text\" name=\"module_id\" value=\"" + module_id + "\" disabled>");
                out.println("</div>");
                out.println("<div class=\"inline-block module-edit-input\">");
                out.println("<p>Module name</p>");
                out.println("<input type=\"text\" name=\"module_name\" value=\"" + module_name + "\" disabled>");
                out.println("</div>");
                out.println("<div class=\"inline-block module-edit-input\">");
                out.println("<p>Module description</p>");
                out.println("<input type=\"text\" name=\"module_desc\" value=\"" + module_desc + "\" disabled>");
                out.println("</div>");
                out.println("<div class=\"inline-block module-edit-input\">");
                out.println("<p>Module points</p>");
                out.println("<input type=\"text\" name=\"module_points\" value=\"" + module_points + "\" disabled>");
                out.println("</div>");
                out.println("<input class=\"button\" id=\"one-module-edit\" type=\"button\" value=\"Edit module\" onclick=\"enable();\">");
                out.println("<input class=\"button\" id=\"one-module-save\" type=\"submit\" value=\"Save\">");
                out.println("</form>");
                out.println("<form action=\"createDeliverables\">");
                out.println("<input class=\"button\" type=\"submit\" value=\"Create Deliverables\">");
                out.println("</form>");
                out.println("</div>");
                
            }
                
        }
        
        catch (SQLException ex) {
            out.println("SQL error: " + ex);
        }
    }
    
}
