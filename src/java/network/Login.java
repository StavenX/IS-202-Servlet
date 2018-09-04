/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package network;

import java.io.PrintWriter;
import java.sql.*; 
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

/**
 *
 * @author Staven
 */
public class Login 
{   
    Connection conn; 
    Statement stmt;
    
    public void printModules(PrintWriter out) {
        String strSelect = "SELECT * FROM MODULES";
        
        System.out.println("The SQL query is: " + strSelect); // DEBUG
        out.println("The SQL query is: " + strSelect);
        
        System.out.println();
        out.println();
        
    }
    
    // @Resource DataSource LocalhostDS;
    public void loginToDatabase(PrintWriter out) {
        
        try {
            // Step 1: Allocate a database 'Connection' object
            Context cont = new InitialContext();
            DataSource ds = (DataSource)cont.lookup("java:comp/env/jdbc/localhostDS"); 
            
            //DataSource ds = (DataSource)cont.lookup("jdbc/LocalhostDS");
            conn = ds.getConnection();
            
             // Step 2: Allocate a 'Statement' object in the Connection
            stmt = conn.createStatement();
            
            out.println("CONNECTED!");
        }
        catch (SQLException ex) {
            out.println("Not connected to database " + ex);
        }
        catch (NamingException nex) {
            out.println("Not correct naming " + nex);
        }
        
    }
}
