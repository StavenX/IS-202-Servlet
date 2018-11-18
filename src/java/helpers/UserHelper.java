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
import javax.servlet.http.HttpServletRequest;
/**
 *
 * @author Staven
 */
public class UserHelper {
    
    
    
    public static String getUserName(HttpServletRequest request) {
        AccessTokenHelper a = new AccessTokenHelper(request);
        String username = a.getUsername();
        return username;
    }
    
    public static String getUserRole(HttpServletRequest request) {
        AccessTokenHelper a = new AccessTokenHelper(request);
        String role = a.getUserRole();
        return role;
    }
    
    public static String getUserId (Connection conn, String username) {
        String user_id = "";
        try {
            String sqlString = "SELECT user_id FROM users WHERE user_username = ?";
            PreparedStatement getId = conn.prepareStatement(sqlString);
            getId.setString(1, username);
            
            ResultSet rset = getId.executeQuery();
            
            while (rset.next()) {
                user_id = rset.getString("user_id");
            }
        } catch (SQLException ex) {
            System.out.println(ex);
        }
        return user_id;
    }
    
    public static String getUserNameById (Connection conn, String user_id) {
        String username = "";
        try {
            String sqlString = "SELECT user_username FROM users WHERE user_id = ?";
            PreparedStatement getId = conn.prepareStatement(sqlString);
            getId.setString(1, user_id);
            
            ResultSet rset = getId.executeQuery();
            
            while (rset.next()) {
                username = rset.getString("user_username");
            }
        } catch (SQLException ex) {
            System.out.println(ex);
        }
        return username;
    }
    
    public static String getFullNameById (Connection conn, String user_id) {
        String fullname = "";
        try {
            String sqlString = "SELECT user_fname, user_lname FROM users WHERE user_id = ?";
            PreparedStatement getId = conn.prepareStatement(sqlString);
            getId.setString(1, user_id);
            
            ResultSet rset = getId.executeQuery();
            
            while (rset.next()) {
                fullname = rset.getString("user_fname") + " " + rset.getString("user_lname");
            }
        } catch (SQLException ex) {
            System.out.println(ex);
        }
        return fullname;
    }
    
    
        /**
     * Inserts a student into the student table.
     * 
     * TODO: Currently prone to SQL injection, needs to use
     * prepareStatement() instead
     * 
     * @param username The student name
     * @param password The student's education
     * @param role
     * @param fname
     * @param lname
     * @param conn The connection object
     * @param out The printwriter, for printing errors etc
     */
    public static void insertUser(String username, String password, String role, String fname, String lname, Connection conn, PrintWriter out) {
        
        try {
            HtmlHelper site = new HtmlHelper(out);
            
            PreparedStatement prepInsert = conn.prepareStatement("INSERT INTO users (user_username, user_password, user_role, user_fname, user_lname) VALUES (?, ?, ?, ?, ?);");
            prepInsert.setString(1, site.checkIfValidText(username));
            prepInsert.setString(2, site.checkIfValidText(password));
            prepInsert.setString(3, site.checkIfValidText(role));
            prepInsert.setString(4, site.checkIfValidText(fname));
            prepInsert.setString(5, site.checkIfValidText(lname));
            
            System.out.println("The SQL query is: " + prepInsert.toString() ); // debug
            int countInserted = prepInsert.executeUpdate();         
            System.out.println(countInserted + " records inserted.\n");  
            out.println(countInserted + " records inserted.\n");  
            
            // The button that prints all students
            out.println(
                "<form action=\"getUser\" method=\"get\">\n" +
"                   <input class=\"button\" type=\"Submit\" value=\"Get all Students from Database\">   \n" +
"               </form>");
        }
        catch (SQLIntegrityConstraintViolationException ex) {
            String error = "";
            if (ex.getMessage().contains("Duplicate entry")) {
                error = "Username already taken, try another";
            } else {
                error = "One or more mandatory fields were empty, please try again";
            }
            out.println("<p>" + error + "</p>");
            out.println("<button class=\"button\" onclick=\"window.history.back();\">Go back</button>");
        }
        catch (SQLException ex) {
            out.println("SQL error: " + ex);
        }
    }
    
    
    public static ResultSet getUsers(Connection conn, String course_id) throws SQLException {
            String sqlString;
            PreparedStatement getUsers;
            if (course_id.toLowerCase().equals("%")) {
                sqlString = "SELECT * FROM users ORDER BY user_id";
                getUsers = conn.prepareStatement(sqlString);
            } else {
                sqlString = "SELECT * FROM users\n"
                        + "INNER JOIN course_details ON users.user_id = course_details.user_id\n"
                        + "WHERE course_details.course_id LIKE ?;";
                getUsers = conn.prepareStatement(sqlString);
                getUsers.setString(1, course_id);
            }
            ResultSet rset = getUsers.executeQuery();
            return rset;
    }
    
    //course_id = "%" to get users not in any course
    public static ResultSet getUsersNotInCourse(Connection conn, String course_id) throws SQLException {
            String sqlString = "SELECT *\n" +
                                "FROM users\n" +
                                "WHERE users.user_id NOT IN\n" +
                                "(SELECT user_id FROM course_details\n" +
                                "WHERE course_details.course_id LIKE ?);";
            PreparedStatement getUsers = conn.prepareStatement(sqlString);
            getUsers.setString(1, course_id);
            ResultSet rset = getUsers.executeQuery();
            return rset;        
    }
    
    public static void printAllUsers(PrintWriter out, Connection conn) throws SQLException {
        printUsers(out, conn, getUsers(conn, "%"));
    }
    
    /**
     * Prints all the students located in the student
     * table.
     * 
     * @param out The printwriter to write with
     * @param conn The connection to use
     * @param rset users to be printed
     */
    public static void printUsers(PrintWriter out, Connection conn, ResultSet rset) {

        HtmlHelper site = new HtmlHelper(out);
        
        try {
            
            // While there exists more entries (rows?)
            while (rset.next()) {               
                // The different columns
                
                String user_id = rset.getString("users.user_id");
                String user_username = rset.getString("user_username");
                String user_fname = rset.getString("user_fname");
                String user_lname = rset.getString("user_lname");
                
                out.println("<div class=\"student-container\">");
                
                //form containing student information
                out.println("<div class=\"student-container-item\">");
                out.println("<input form=\"test\" type=\"checkbox\" name=\"marked\" value=\"" + user_id + "\">");
                out.println("<form action=\"oneUser\">");
                out.println("<input type=\"hidden\" name=\"user_id\" value=\"" + user_id + "\">");
                out.println("<div>User Id:" + user_id + "</div>");
                out.println("<div>Username:" + user_username + "</div>");
                out.println("<div>First name:" + user_fname + "</div>");
                out.println("<div>Last name:" + user_lname + "</div>");
                out.println("</div>");
                
                //"more info"-button
                out.println("<div class=\"student-container-item\">");
                out.println("<input class=\"button more-info-button\" type=\"submit\" value=\"Details\">");
                out.println("</div>");
                out.println("</form>");
                
                //delete-buttons
                out.println("<div class=\"student-container-item\">");
                out.println("<form name=\"delete-form-" + user_id + "\" action=\"deleteUser\" method=\"get\">");
                site.printDeleteButton("deleteUser", "user_id", user_id);
                out.println("</div>");
                out.println("</div>");
            }
            
            //prints javascript
            site.useJS("buttons-for-delete.js");
            
            conn.close();
        }
        catch (SQLException ex) {
            out.println("SQL error: " + ex);
        }
        catch (Exception e) {
            out.println("Something wrong happened: " + e);
        }       
    }
    
    
    public static ResultSet getOneUser(Connection conn, String user_id) throws SQLException {
        PreparedStatement getUser;
        getUser = conn.prepareStatement("SELECT * FROM users WHERE useR_id = ?");
        getUser.setString(1, user_id);

        ResultSet rset = getUser.executeQuery();
        return rset;
    }
    
    /**
     * Prints more details about one student
     * @param out
     * @param conn
     * @param user_id 
     */
    public static void printOneUser(PrintWriter out, Connection conn, String user_id) {
        try {
            ResultSet rset = getOneUser(conn, user_id);
            
            //for the result
            while (rset.next()) {
                String user_username = rset.getString("user_username");
                String user_fname = rset.getString("user_fname");
                String user_lname = rset.getString("user_lname");
                out.println("<div>");
                out.printf("%s | %s | %s | %s", user_id, user_username, user_fname, user_lname);
                out.println("</div>");
                
            }
                
        }
        
        catch (SQLException ex) {
            out.println("SQL error: " + ex);
        }
    }
    
    public static void addUserToModule(String module_id, String student_id, Connection conn, PrintWriter out) {
        
            String sqlString = "INSERT INTO module_details (student_id, module_id) VALUES (?, ?);";
            
            
            try {
                PreparedStatement ps = conn.prepareStatement(sqlString);
                ps.setString(1, student_id);
                ps.setString(2, module_id);
                
                int amount = ps.executeUpdate();
                out.println(amount + " inserted");
            }
            catch (SQLException ex) {
                out.println("SQL ERROR: " + ex);
            }
    }
    
    /**
     * adds a user to the specified course, and all tables related
     * @param course_id id of the course
     * @param user_id id of the user
     * @param conn the connection to the db
     * @param out the web page writer
     */
    public static void addUserToCourse(String course_id, String user_id, Connection conn, PrintWriter out) {
        
        String sqlString = "INSERT INTO course_details (course_id, user_id) VALUES (?, ?);";
            try {
                PreparedStatement ps = conn.prepareStatement(sqlString);
                ps.setString(1, course_id);
                ps.setString(2, user_id);
                int amount = ps.executeUpdate();

                //gets all modules in the course the user is to be added to
                //sorted by module_id
                ResultSet rset = ModuleHelper.getModules(out, conn, "id", "v", course_id);
                
                //for each module add the user to the module too
                while (rset.next()) {
                        String module_id = rset.getString("module_id");
                        addUserToModule(module_id, user_id, conn, out);
                }
                
                //executes 
                out.println(amount + " inserted");
            }
            catch (SQLException ex) {
                out.println("SQL ERROR: " + ex);
            }
    }
    
    public static ResultSet getUserDetails (PrintWriter out, Connection conn, String user_id) {
        String sqlString = "SELECT * FROM users WHERE user_id = ?";
        try {
            PreparedStatement getUser = conn.prepareStatement(sqlString);
            getUser.setString(1, user_id);
            ResultSet rset = getUser.executeQuery();
            return rset;
        } catch (SQLException ex) {
            out.println(ex);
        }
        return null;
    }
    
    public static void printUserPage (PrintWriter out, Connection conn, String user_id) {
            try {
                ResultSet rset = getUserDetails(out, conn, user_id);
                
                while (rset.next()) {
                    String username = rset.getString("user_username");
                    String fname = rset.getString("user_fname");
                    String lname = rset.getString("user_lname");
                    String pic_url = "images/profiles/" + rset.getString("user_pic_url");
                    
                    out.println("<p> Name: " + fname + " " + lname + "</p>");
                    out.println("<img class=\"profile-pic-medium\" src=\"" + pic_url + "\" alt=\"Profile picture for " + fname + "\">");
                }
                
            } catch (SQLException ex) {
                out.println(ex);
            }
    }
}
