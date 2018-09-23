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
            getModules = conn.prepareStatement("SELECT * FROM student ORDER BY ?");
            getModules.setString(1, "student_id");
                       
            ResultSet rset = getModules.executeQuery();
            
            out.println("the records selected are:" + "<br>");
            int rowCount = 0; 
            
            // While there exists more entries (rows?)
            while (rset.next()) {               
                // The different columns
                String mod_id = rset.getString("mod_id");
                String mod_name = rset.getString("mod_name");
                String mod_desc = rset.getString("mod_desc");
                out.println("Row " + rowCount + ": " + mod_id + ": " + mod_name + ", " + mod_desc + "<br>");
                rowCount++;
            }
            out.println("Total number of records: " + rowCount);
        }
        catch (SQLException ex) {
            out.println("SQL error: " + ex);
        }
        catch (Exception e) {
            out.println("Something wrong happened: " + e);
        }       
    }
    
}
