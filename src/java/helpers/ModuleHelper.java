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
     * @param course_id
     * @param conn The connection object
     * @param out The printwriter, for printing errors etc
     */
        
    public static void insertModule(String name, String desc, String points, String course_id, Connection conn, PrintWriter out) {
        
        HtmlHelper site = new HtmlHelper(out);
        try {
            PreparedStatement prepInsert = conn.prepareStatement("INSERT INTO module (module_name, module_desc, module_points, course_id) values (?, ?, ?, ?);");
            prepInsert.setString(1, site.checkIfValidText(name));
            prepInsert.setString(2, site.checkIfValidText(desc));
            prepInsert.setString(3, site.checkIfValidText(points));
            prepInsert.setString(4, site.checkIfValidText(course_id));
            
            System.out.println("The SQL query is: " + prepInsert.toString() ); // debug
            int countInserted = prepInsert.executeUpdate();           
            out.println("<p>" + countInserted + " module created.</p>");  
            
            
            addStudentsToNewModule(conn, out);
            
            out.println(
                "<form action=\"getModule\" method=\"get\">\n" +
"                   <input class=\"button\" type=\"Submit\" value=\"Get all Modules from Database\">   \n" +
"               </form>");
        }
        catch (SQLIntegrityConstraintViolationException ex) {
            out.println("One or more mandatory fields were empty, please try again");
            site.printBackButton();
        }
        catch (SQLException ex) {
            if (ex.getMessage().contains("Incorrect integer value")) {
                out.println("Module points must be an integer, try again");
                site.printBackButton();
            } else {
                out.println("SQL error: " + ex);
                site.printBackButton();
            }
        }
    }
    
    public static void addStudentsToNewModule(Connection conn, PrintWriter out) {
        PreparedStatement addStudents;
        try {
            addStudents = conn.prepareStatement("SELECT module_id, course_id FROM module ORDER BY module_id DESC LIMIT 1");
            ResultSet rset = addStudents.executeQuery();
            while (rset.next()) {
                String module_id = rset.getString("module_id");
                String course_id = rset.getString("course_id");
                ResultSet students = UserHelper.getUsers(conn, course_id);
                
                int studentsUpdated = 0;
                while (students.next()) {
                    String user_id = students.getString("user_id");
                    UserHelper.addUserToModule(module_id, user_id, conn, out);
                    studentsUpdated++;
                }
                out.println("<p>" + studentsUpdated + " students added to module.</p>");
            }
        }
        catch (SQLException ex) {
            System.out.println(ex);
        }
    }
    
    
    public static void updateModule(String id, String name, String desc, String points, Connection conn, PrintWriter out) {
        
        try {
            
            HtmlHelper site = new HtmlHelper(out);
            
            PreparedStatement prepUpdate = conn.prepareStatement("UPDATE module SET module_name = ?, module_desc = ?, module_points = ? WHERE module_id = ?");
            prepUpdate.setString(1, site.checkIfValidText(name));
            prepUpdate.setString(2, site.checkIfValidText(desc));
            prepUpdate.setString(3, site.checkIfValidText(points));
            prepUpdate.setString(4, site.checkIfValidText(id));
            
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
     * takes a string and converts it to the proper SQL field names
     * @param orderBy the base string that is taken from the web page buttons
     * @param direction
     * @return completed string that works for our SQL table
     */
    public static String handleOrder(String orderBy, String direction) {
        //initalize the return value, add to it in switches
        String handledOrder = "";
        
        //what to order by
        switch (orderBy.toLowerCase()) {                    
            case "course":
                handledOrder += "course.course_name";
                break;
                
            case "name":    
                handledOrder += "module.module_name";
                break;

            case "points":  
                handledOrder += "module.module_points";                            
                break;
                
            case "score":
                handledOrder += "module_details.module_points";
                break;

            case "id":
            default:    
                handledOrder += "module.module_id";
        }
        
        //what direction the order is in
        switch (direction.toLowerCase()) {
            case "^":    
                handledOrder += " DESC;";
                break;

            case "v":
            default:        
                handledOrder += " ASC;";
        }
        
        return handledOrder;
    }
    
    //course_id = "%" for all courses
    public static ResultSet getModules (PrintWriter out, Connection conn, String orderBy, String direction, String course_id) {
            PreparedStatement getModules; 
        try {
            
            //base string for sql preparedstatement
            //'LIKE' instead of '=' to allow usage of '%' to get all
            String sqlString = "SELECT * FROM module WHERE course_id LIKE ? ORDER BY ";
            
            sqlString += handleOrder(orderBy, direction);
            
            //preparedstatement is prepared and executed
            getModules = conn.prepareStatement(sqlString);
            getModules.setString(1, course_id);
            ResultSet rset = getModules.executeQuery();
            return rset;
        }
        catch (SQLException ex) {
            out.println("SQL error: " + ex);
        }
        catch (Exception e) {
            out.println("Something wrong happened: " + e);
        }       
        return null;
    }
    
    public static ResultSet getStudentsModules(PrintWriter out, Connection conn, String orderBy, String direction, String user_id) {
        try {
            
            String sqlString = "SELECT * FROM module\n" +
                            "INNER JOIN module_details ON module.module_id = module_details.module_id\n" +
                            "INNER JOIN course ON module.course_id = course.course_id\n" +
                            "WHERE module_details.student_id = ?\n" +
                            "ORDER BY ";
            
            sqlString += handleOrder(orderBy, direction);
            PreparedStatement getStudentsModules = conn.prepareStatement(sqlString);
            getStudentsModules.setString(1, user_id);
            ResultSet rset = getStudentsModules.executeQuery();
            
            return rset;
            
        } catch (SQLException ex) {
            out.println(ex);
        }
        
        
        return null;
    }
    
    public static void printStudentsModules(PrintWriter out, Connection conn, String orderBy, String direction, String user_id) {
        ResultSet rset = getStudentsModules(out, conn, orderBy, direction, user_id);
        try {
            
            
            String[] sortingTypes = {"Course", "Name", "Points", "Score"};
            
            out.println("<form action=\"myProfile\" method=\"post\">");
            out.println("<div class=\"sort-by-container\">");
            printOrderBy(out, orderBy, sortingTypes);
            out.println("</div>");
            out.println("</form>");
            
            out.println("<table class=\"module-students-table\">");
            out.println("<tr>");
            out.println("<th>Course</th>");
            out.println("<th>Name</th>");
            out.println("<th>Description</th>");
            out.println("<th>Max points</th>");
            out.println("<th>Your points</th>");
            out.println("<th>Status</th>");
            out.println("</tr>");
            
            
            boolean lastRowEven = false;
            while (rset.next()) {
                String module_id = rset.getString("module.module_id");
                String course_name = rset.getString("course_name");
                String module_name = rset.getString("module_name");
                String module_desc = rset.getString("module_desc");
                String max_points = rset.getString("module.module_points");
                String your_points = rset.getString("module_details.module_points");
                String status = rset.getString("module_status");
                
                String rowClassName = (lastRowEven) ? "even-row" : "odd-row";
                lastRowEven = !(lastRowEven);
                
                String formName = "oneModule-" + module_id;
                
                out.println("<form name=\"" + formName + "\" action=\"oneModule\">");
                out.println("<input type=\"hidden\" name=\"module_id\" value=\"" + module_id + "\">");
                out.println("<tr class=\"pointer-hover " + rowClassName + "\" onclick=\"submit(\'" + formName + "\')\">");
                out.println("<td>" + course_name + "</td>");
                out.println("<td>" + module_name + "</td>");
                out.println("<td>" + module_desc + "</td>");
                out.println("<td>" + max_points + "</td>");
                out.println("<td>" + your_points + "</td>");
                out.println("<td><div class=\"module-status\">" + status + "</div></td>");
                out.println("</tr>");
                out.println("</form>");
            }
            out.println("</table>");
        } catch (SQLException ex) {
            out.println("hei" + ex);
        }
    }
    
    public static void printOrderBy(PrintWriter out, String currentOrder, String[] options) {
        out.println("<select name=\"orderBy\" class=\"button\">");
        for (String option : options) {
            String currentOption = (option.equals(currentOrder)) ? "<option selected>" + option + "</option>" : "<option>" + option + "</option>";
            out.println(currentOption);
            
        }
        out.println("</select>");
        out.println("<div class=\"direction-container\">");
        out.println("<input class=\"button order-direction\" type=\"submit\" name=\"orderDirection\" value=\"^\">");
        out.println("<input class=\"button order-direction\" type=\"submit\" name=\"orderDirection\" value=\"V\">");
        out.println("</div>");
    }
    
    /**
     * Prints all the students located in the student
     * table.
     * 
     * @param out The printwriter to write with
     * @param conn The connection to use
     * @param orderBy the column name to order the sql results in
     * @param direction
     * @param role the role of the user logged in
     * @param course_id
     * @param currentServlet
     */
    public static void printModules(PrintWriter out, Connection conn, String orderBy, String direction, String role, String course_id, String currentServlet) {

            HtmlHelper site = new HtmlHelper(out);
        
        try {
            
            ResultSet rset = getModules(out, conn, orderBy, direction, course_id);
            
            out.println("the records selected are:" + "<br>");
            int rowCount = 0; 
            
                
            PreparedStatement getCourseName = conn.prepareStatement("SELECT course_name FROM course WHERE course_id = ?");
            getCourseName.setString(1, course_id);
            ResultSet courseResult = getCourseName.executeQuery();
            
            String course_name = "";
            while (courseResult.next()) {
                course_name = courseResult.getString("course_name");
            }
                
            //"sort by"-buttons and necessary parameters
            out.println("<div class=\"sort-by-container\">");
            
            out.println("<h2>Sort by: </h2>");
            out.println("<form action=\"" + currentServlet + "\" method=\"post\">");
            
            out.println("<input type=\"hidden\" name=\"course_id\" value=\"" + course_id + "\">");
            out.println("<input type=\"hidden\" name=\"course_name\" value=\"" + course_name + "\">");
            out.println("<input type=\"hidden\" name=\"role\" value=\"" + role + "\">");
            out.println("<input type=\"hidden\" name=\"details\" value=\"modules\">");
            
            String[] sortingTypes = {"ID", "Name", "Points"};
            printOrderBy(out, orderBy, sortingTypes);
            out.println("</form>");
            out.println("</div>");
            
            // While there exists more entries (rows?)
            while (rset.next()) {               
                // The different columns from the db
                String module_id = rset.getString("module_id");
                String module_name = rset.getString("module_name");
                String module_desc = rset.getString("module_desc");
                String module_points = rset.getString("module_points");
                course_id = rset.getString("course_id");
                
                
                getCourseName = conn.prepareStatement("SELECT * FROM course WHERE course_id LIKE ?");
                getCourseName.setString(1, course_id);
                courseResult = getCourseName.executeQuery();

                while (courseResult.next()) {
                    course_name = courseResult.getString("course_name");
                }
                
                //the module info in a container
                out.println("<div class=\"module-container\">");
                out.println("<form action=\"oneModule\" method=\"get\">");
                out.println("<input class=\"invisible\" name=\"module_id\" value=\"" + module_id + "\">");
                out.println("<div>Row " + rowCount + "</div>");
                out.println("<div name=\"modid\">Module Id:" + module_id + "</div>");
                out.println("<div>Name:" + module_name + "</div>");
                out.println("<div>Description:" + module_desc + "</div>");
                out.println("<div>Max points:" + module_points + "</div>");
                out.println("<div>Course: " + course_name + "</div>");
                out.println("<input class=\"button more-info-button\" type=\"submit\" value=\"Details\">");
                out.println("</form>");
                //button(s) for deletion of a module
                if (role.toLowerCase().equals("lecturer")) {
                    site.printDeleteButton("deleteModule", "module_id", module_id);
                }
                out.println("</div>");
                
                rowCount++;
            }
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
                out.println("<form action=\"updateModule\" method=\"get\">");
                
                out.println("<input type=\"hidden\" name=\"module_id\" value=\"" + module_id + "\">");
                
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
                
                out.println("</div>");
                
            }
                
        }
        
        catch (SQLException ex) {
            out.println("SQL error: " + ex);
        }
    }
    
    public static String deleteModule(String module_id, Connection conn) {
        
        String results = "";

        try {
            PreparedStatement deleteModuleDetails = conn.prepareStatement("DELETE FROM module_details WHERE module_id = ?");
            deleteModuleDetails.setString(1, module_id);
            int detailsDeleted = deleteModuleDetails.executeUpdate();


            PreparedStatement deleteModule = conn.prepareStatement("DELETE FROM module WHERE module_id = ?;");
            deleteModule.setString(1, module_id);
            int modulesDeleted = deleteModule.executeUpdate();
            
            results += modulesDeleted + " modules deleted. " + detailsDeleted + " students affected";
        } catch (SQLException ex) {
            results += "SQL error: " + ex;
            return results;
        }
        
        return results;
    }
    
}
