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
    
    /**
     * Closes the connection to the database
     */
    public String close() {
         
        try {
            conn.close();
        }
        catch (SQLException ex) {
            System.out.println("Cannot close connection: " + ex);
            return "not closed";
        }
        return "closed";
    } 

    /*
    *   This method is when we use the DataSource given in context.xml
        What we do is: 
        1: Create a Context object
           See https://docs.oracle.com/javase/tutorial/jdbc/basics/connecting.html
        2: Create a DataSource object, and we "connect" this DataSource object to 
           the datasource LocalhostDS, given in the tomee.xml file. 
        3: The connection object is set to the DataSource getConnection.    
        4: The Statemet object is set to the Statement object created by the Connection objects crateStatement() method 
    */
    public Connection loginToDB(PrintWriter out) {
        
        try {
            // Step 1: Allocate a database 'Connection' object
            Context cont = new InitialContext();
            DataSource ds = (DataSource)cont.lookup("java:comp/env/jdbc/localhostDS"); 
            
            //DataSource ds = (DataSource)cont.lookup("jdbc/LocalhostDS");
            conn = ds.getConnection();
            return conn;
            // Step 2: Allocate a 'Statement' object in the Connection
            //stmt = conn.createStatement();     
        }
        catch (SQLException ex) {
            out.println("Not connected to database: " + ex);
        }
        catch (NamingException nex) {
            out.println("Not correct naming: " + nex);
        }
        catch (Exception e) {
            out.println("Other error: " + e);
        }
        return null; 
    }
}
