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
public class ModuleHelper {
    
    public static String getModuleName (String module_id, Connection conn) {
        String moduleName = "";
        try {
            PreparedStatement getName = conn.prepareStatement("SELECT module_name FROM module WHERE module_id LIKE ?");
            getName.setString(1, module_id);
            ResultSet rset = getName.executeQuery();
            
            while (rset.next()) {
                moduleName = rset.getString("module_name");
            }
        } catch (SQLException ex) {
            System.out.println(ex);
        }
        return moduleName;
    }
    
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
            
            int countInserted = prepInsert.executeUpdate();           
            out.println("<p>" + countInserted + " module created.</p>");  
            
            
            addStudentsToNewModule(conn, out);
            
            out.println(
                "<form action=\"getModule\" method=\"post\">\n" +
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
            out.println(ex);
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
            int countInserted = prepUpdate.executeUpdate();         
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
                
            case "status":
                handledOrder += "FIELD(module_details.module_status, "
                        + "\'Not delivered\', \'Pending\', \'Completed\') ";
                break;

            case "id":
            default:    
                handledOrder += "module.module_id";
        }
        
        //what direction the order is in
        switch (direction.toLowerCase()) {
            case "v":    
                handledOrder += " DESC;";
                break;

            case "^":
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
            
            
            String[] sortingTypes = {"Course", "Name", "Points", "Score", "Status"};
            
            out.println("<form action=\"myProfile\" method=\"post\">");
            out.println("<div class=\"sort-by-container\">");
            out.println("<h2>Sort modules by </h2>");
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
                
                out.println("<form id=\"" + formName + "\" action=\"oneModule\">");
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
        out.println("<select name=\"orderBy\" class=\"button\" onchange=\"this.form.submit();\">");
        for (String option : options) {
            String currentOption = (option.equals(currentOrder)) ? "<option selected>" + option + "</option>" : "<option>" + option + "</option>";
            out.println(currentOption);
            
        }
        out.println("</select>");
        out.println("<div class=\"direction-container\">");
        out.println("<button class=\"button sort-direction\" name=\"orderDirection\" value=\"^\">"
                + "<img class=\"up-arrow\"src=\"images/arrow.svg\">"
                + "</button>");
        out.println("<button class=\"button sort-direction\" name=\"orderDirection\" value=\"V\">"
                + "<img class=\"down-arrow\"src=\"images/arrow.svg\">"
                + "</button>");
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
            
            PreparedStatement getCourseName = conn.prepareStatement("SELECT course_name FROM course WHERE course_id = ?");
            getCourseName.setString(1, course_id);
            ResultSet courseResult = getCourseName.executeQuery();
            
            String course_name = "";
            while (courseResult.next()) {
                course_name = courseResult.getString("course_name");
            }
                
            //"sort by"-buttons and necessary parameters
            out.println("<div class=\"sort-by-container\">");
            
            out.println("<h2>Sort modules by: </h2>");
            out.println("<form action=\"" + currentServlet + "\" method=\"post\">");
            
            out.println("<input type=\"hidden\" name=\"course_id\" value=\"" + course_id + "\">");
            out.println("<input type=\"hidden\" name=\"course_name\" value=\"" + course_name + "\">");
            out.println("<input type=\"hidden\" name=\"role\" value=\"" + role + "\">");
            out.println("<input type=\"hidden\" name=\"details\" value=\"modules\">");
            
            String[] sortingTypes = {"ID", "Name", "Points"};
            printOrderBy(out, orderBy, sortingTypes);
            out.println("</form>");
            out.println("</div>");
            
            out.println("<div class=\"modules-container\">");
            
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
                out.println("<div name=\"modid\">Module Id:" + module_id + "</div>");
                out.println("<div>Name:" + module_name + "</div>");
                out.println("<div>Description:" + module_desc + "</div>");
                out.println("<div>Max points:" + module_points + "</div>");
                out.println("<div>Course: " + course_name + "</div>");
                site.printDetailsButton();
                out.println("</form>");
                //button(s) for deletion of a module
                if (role.toLowerCase().equals("lecturer")) {
                    site.printDeleteButton("deleteModule", "module_id", module_id);
                }
                out.println("</div>");
            }
            out.println("</div>");
            
            site.useJS("buttons-for-delete.js");
        }
        catch (SQLException ex) {
            out.println("SQL error: " + ex);
        }
        catch (Exception e) {
            out.println("Something wrong happened: " + e);
        }       
    }
    
    public static ResultSet getOneModule(Connection conn, String module_id) throws SQLException {
        String sqlString = "SELECT * FROM module WHERE module_id = ?";
        PreparedStatement getOneModule = conn.prepareStatement(sqlString);
        getOneModule.setString(1, module_id);
        ResultSet rset = getOneModule.executeQuery();
        
        return rset;
    }
    
    public static void printOneModule(PrintWriter out, Connection conn, String module_id, String role) {
        
        try {
            ResultSet rset = getOneModule(conn, module_id);
            
            while (rset.next()) {
                String module_points = rset.getString("module_points");
                String module_name = rset.getString("module_name");
                String module_desc = rset.getString("module_desc");
                
                String displayTypeClass = "one-module-student-view";
                if(role.toLowerCase().equals("lecturer")) {
                    displayTypeClass = "one-module-lecturer-view";
                }
                
                out.println("<div class=\"one-module-info-container\">");
                out.println("<form id=\"update-module\" action=\"updateModule\" method=\"get\">");
                
                out.println("<input type=\"hidden\" name=\"module_id\" value=\"" + module_id + "\">");
                
                //points
                out.println("<div class=\"" + displayTypeClass + " module-edit-input\">");
                out.println("<p class=\"module-input-title\">Module points</p>");
                out.println("<input class=\"module-input\" type=\"text\" name=\"module_points\" value=\"" + module_points + "\" disabled>");
                out.println("</div>");
                
                //title
                out.println("<div class=\"" + displayTypeClass + " module-edit-input\">");
                out.println("<p class=\"module-input-title\">Module name</p>");
                out.println("<input class=\"module-input\" type=\"text\" name=\"module_name\" value=\"" + module_name + "\" disabled>");
                out.println("</div>");                
                
                //description
                out.println("<div class=\"" + displayTypeClass + " module-edit-input\">");
                out.println("<p class=\"module-input-title\">Module description</p>");
                out.println("<textarea class=\"module-input\" name=\"module_desc\" disabled>" + module_desc + "</textarea>");
                out.println("</div>");
                
                out.println("</form>");
                
                
                if(role.toLowerCase().equals("lecturer")) {
                //edit / save buttons
                out.println("<input class=\"button\" id=\"one-module-edit\" type=\"button\" value=\"Edit module\" onclick=\"enable();\">");
                out.println("<input class=\"button\" id=\"one-module-save\" type=\"submit\" value=\"Save\" onclick=\"submit(\'update-module\');\">");
                }
                
                out.println("</div>");
                
            }
                
        }
        
        catch (SQLException ex) {
            out.println("SQL error: " + ex);
        }
    }
    
    public static ResultSet getOneModuleParticipants (Connection conn, String module_id, String orderBy, String search) throws SQLException {
        
        String sqlString = "SELECT CONCAT(users.user_fname, \' \', users.user_lname) AS `Name`, course.course_name, module.module_name, module.module_desc,\n" +
                "module.module_points AS \'max_points\', module_details.module_points AS \'student points\', module_details.student_id, module_details.module_status \n" +
                "FROM course\n" +
                "INNER JOIN module ON course.course_id = module.course_id\n" +
                "INNER JOIN module_details ON module.module_id = module_details.module_id\n" +
                "INNER JOIN users ON module_details.student_id = users.user_id\n" +
                "WHERE module.module_id = ?\n" +
                "HAVING `Name` LIKE ?\n" + 
                "ORDER BY FIELD(module_status, ";
        
        switch(orderBy) {
            case "Not delivered":
                sqlString += "\'Not delivered\', \'Pending\', \'Completed\');";
                break;

            case "Completed":
                sqlString += "\'Completed\', \'Pending\', \'Not Delivered\');";
                break;

            case "Pending":
            default: sqlString += "\'Pending\', \'Not Delivered\', \'Completed\');";
        }
                
        PreparedStatement getPeople = conn.prepareStatement(sqlString);
        getPeople.setString(1, module_id);
        getPeople.setString(2, search);
        ResultSet rset = getPeople.executeQuery();
        
        return rset;
    }
    
    public static void printOneModuleStudentTable(PrintWriter out, Connection conn, String module_id, String orderBy, String search) throws SQLException {
        
        ResultSet rset = getOneModuleParticipants(conn, module_id, orderBy, search);
        //is true if rset isn't empty
        if (rset.isBeforeFirst()) {
            
            //sorting and search-by-name form
            out.println("<form action=\"oneModule\" method=\"get\" class=\"one-module-sort\">");
            out.println("<input type=\"hidden\" name=\"module_id\" value=\"" + module_id +"\">");
            out.println("<h2>Order module deliveries by:</h2>");
            out.println("<input type=\"text\" name=\"search\" placeholder=\"Search\">");
            out.println("<input class=\"button small-button\"type=\"submit\" value=\"Search\">");
            out.println("<input type=\"submit\" class=\"button\" name=\"orderBy\" value=\"Not delivered\">");
            out.println("<input type=\"submit\" class=\"button\" name=\"orderBy\" value=\"Pending\">");
            out.println("<input type=\"submit\" class=\"button\" name=\"orderBy\" value=\"Completed\">");
            out.println("</form>");
            
            //header row of table
            out.println("<div class=\"module-student-list\">");
            out.println("<table class=\"module-students-table\">");
            out.println("<tr>");
            out.println("<th>Name</th>");
            out.println("<th>Student's points</th>");
            out.println("<th>Status</th>");
            out.println("</tr>");

            //prints a row for each student
            while (rset.next()) {
                String user_name = rset.getString("Name");
                String user_id = rset.getString("student_id");
                String student_points = rset.getString("student points");
                String status = rset.getString("module_status");

                if (student_points == null) {
                    student_points = "---";
                }
                
                String formName = module_id + "-" + user_id;
                out.println("<form id=\"" + formName + "\" action=\"oneStudentModule\" method=\"post\">");
                out.println("<input type=\"hidden\" name=\"module_id\" value=\"" + module_id +"\">");
                out.println("<input type=\"hidden\" name=\"user_id\" value=\"" + user_id + "\">");
                out.println("<tr class=\"pointer-hover\" onclick=\"submit(\'" + formName + "\');\">");
                out.println("<td>" + user_name + "</td>");
                out.println("<td>" + student_points + "</td>");
                out.println("<td class=\"deliver-status\"><div class=\"module-status\">" + status + "</div>");
                out.println("<button class=\"button small-button\">View</button></td>");
                out.println("</tr>");
                out.println("</form>");
            }
            out.println("</table>");                        
            out.println("</div>");
        } else {
            out.println("<p style=\"text-align: center\">No students have this module, check if db mistake</p>");
        }
    }
    
    public static boolean studentHasModule (Connection conn, String module_id, String user_id) throws SQLException {
        String sqlString = "SELECT * FROM module_details WHERE module_id LIKE ? AND student_id LIKE ?;";
        PreparedStatement check = conn.prepareStatement(sqlString);
        check.setString(1, module_id);
        check.setString(2, user_id);
        ResultSet rset = check.executeQuery();
        return rset.isBeforeFirst();
    }
    
    public static ResultSet getOneStudentModule (Connection conn, String module_id, String user_id) throws SQLException {
            String sqlString = "SELECT *" +
                            "FROM course\n" +
                            "INNER JOIN module ON course.course_id = module.course_id\n" +
                            "INNER JOIN module_details ON module.module_id = module_details.module_id\n" +
                            "INNER JOIN users ON module_details.student_id = users.user_id\n" +
                            "WHERE module.module_id = ?\n" +
                            "AND module_details.student_id = ?;";
            PreparedStatement get = conn.prepareStatement(sqlString);
            get.setString(1, module_id);
            get.setString(2, user_id);
            ResultSet rset = get.executeQuery();

            return rset;
    }
    
    public static void printOneStudentModule (PrintWriter out, Connection conn, String module_id, String user_id) {
        try {
            ResultSet rset = getOneStudentModule(conn, module_id, user_id);
             while (rset.next()) {
                        String user_username = rset.getString("user_fname") + " " + rset.getString("user_lname");
                        String module_name = rset.getString("module_name");
                        String course_name = rset.getString("course_name");
                        String module_desc = rset.getString("module_desc");
                        String max_points = rset.getString("module.module_points");
                        String your_points = rset.getString("module_details.module_points");
                        
                        if (your_points == null) {
                            your_points = "N/A";
                        }
                        String module_status = rset.getString("module_status");
                        
                        
                        out.println("<table>");
                        
                        out.println("<tr>");
                        out.println("<td>Name of student</td>");
                        out.println("<td>" + user_username + "</td>");
                        out.println("</tr>");
                        
                        out.println("<tr>");
                        out.println("<td>Course</td>");
                        out.println("<td>" + course_name + "</td>");
                        out.println("</tr>");
                        
                        out.println("<tr>");
                        out.println("<td>Module name</td>");
                        out.println("<td>" + module_name + "</td>");
                        out.println("</tr>");
                        
                        out.println("<tr>");
                        out.println("<td>Module description</td>");
                        out.println("<td>" + module_desc + "</td>");
                        out.println("</tr>");
                        
                        out.println("<tr>");
                        out.println("<td>Max points</td>");
                        out.println("<td>" + max_points + "</td>");
                        out.println("</tr>");
                        
                        out.println("<tr>");
                        out.println("<td>Points achieved</td>");
                        out.println("<td>" + your_points + "</td>");
                        out.println("</tr>");
                        
                        out.println("<tr>");
                        out.println("<td>Status</td>");
                        out.println("<td>"+ module_status + "</td>");
                        out.println("</tr>");
                        
                        out.println("</table>");
             }
        } catch (SQLException ex) {
            out.println(ex);
        }
    }
    
    public static String deleteModule(String module_id, Connection conn) {
        
        String results = "";

        try {
            
            PreparedStatement feedback = conn.prepareStatement("DELETE FROM module_feedback WHERE module_id = ?");
            PreparedStatement comments = conn.prepareStatement("DELETE FROM module_comment WHERE module_id = ?");
            PreparedStatement moduleDetails = conn.prepareStatement("DELETE FROM module_details WHERE module_id = ?");
            PreparedStatement modules = conn.prepareStatement("DELETE FROM module WHERE module_id = ?;");
            
            PreparedStatement[] stmts = {feedback, comments, moduleDetails, modules};
            String[] tableNames = {"feedback", "comments", "moduleDetails", "modules"};
            for (int i = 0; i < stmts.length; i++) {
                stmts[i].setString(1, module_id);
                int amount = stmts[i].executeUpdate();
                results += String.format("| %s %s deleted. ", amount, tableNames[i]);
            }
        } catch (SQLException ex) {
            results += "SQL error: " + ex;
            return results;
        }
        
        return results;
    }
    
    
    public static ResultSet getModuleComments(Connection conn, String module_id) throws SQLException {
        String sqlString = "SELECT * FROM module_comment WHERE module_id LIKE ? ORDER BY module_comment_id DESC";
        PreparedStatement getComments = conn.prepareStatement(sqlString);
        getComments.setString(1, module_id);
        ResultSet rset = getComments.executeQuery();
        
        return rset;
    }
    
    public static void printModuleComments(PrintWriter out, Connection conn, String module_id, HttpServletRequest request) {
        try {
            ResultSet rset = getModuleComments(conn, module_id);
            while (rset.next()) {
                String module_comment_id = rset.getString("module_comment_id");
                String user_id = rset.getString("user_id");
                String author = UserHelper.getFullNameById(conn, user_id);
                String content = rset.getString("module_comment_content");
                
                out.println("<form action=\"deleteComment\" class=\"module-comment-container\">");
                out.println("<input type=\"hidden\" name=\"module_id\" value=\"" + module_id + "\">");
                out.println("<h3>" + author + " said:</h3>");
                out.println("<p>" + content + "</p>");
                HtmlHelper site = new HtmlHelper(out);
                
                String currentuser = UserHelper.getUserId(conn, UserHelper.getUserName(request));
                String role = UserHelper.getUserRole(request);
                if (role.equals("Lecturer") || currentuser.equals(user_id)) {                    
                    site.printDeleteButton("oneModule", "module_comment_id", module_comment_id);
                }
                out.println("</form>");
            }
        } catch (SQLException ex) {
            out.println(ex);
        }
    }
    
    public static String newModuleComment(Connection conn, String module_id, String user_id, String content) throws SQLException {
        String sqlString = "INSERT INTO module_comment (module_comment_content, module_id, user_id) VALUES (?, ?, ?);";
        PreparedStatement insertComment = conn.prepareStatement(sqlString);
        insertComment.setString(1, content);
        insertComment.setString(2, module_id);
        insertComment.setString(3, user_id);
        int amount = insertComment.executeUpdate();
        
        return amount + " comments inserted.";
    }
    
    public static String deleteModuleComment(Connection conn, String module_comment_id) {
        
        String results = "";

        try {
            PreparedStatement deleteComment = conn.prepareStatement("DELETE FROM module_comment WHERE module_comment_id = ?");
            deleteComment.setString(1, module_comment_id);
            int amount = deleteComment.executeUpdate();
            
            results += amount + " comment(s) deleted.";
        } catch (SQLException ex) {
            results += "SQL error: " + ex;
            return results;
        }
        
        return results;
    }
    
    
    public static String newModuleFeedback(Connection conn, String module_id, String user_id, String author_id, String content) throws SQLException {
        String sqlString =  "INSERT INTO module_feedback (module_id, user_id, author_id, module_feedback_content) VALUES (?, ?, ?, ?)";
        PreparedStatement newFeedback = conn.prepareStatement(sqlString);
        newFeedback.setString(1, module_id);
        newFeedback.setString(2, user_id);
        newFeedback.setString(3, author_id);
        newFeedback.setString(4, content);
        String results = newFeedback.executeUpdate() + " feedback comment added.";
        
        return results;
    }
        
    public static ResultSet getModuleFeedback(Connection conn, String module_id, String user_id) throws SQLException {
        String sqlString = "SELECT * FROM module_feedback WHERE module_id LIKE ? AND user_id LIKE ? ORDER BY module_feedback_id DESC";
        PreparedStatement getFeedback = conn.prepareStatement(sqlString);
        getFeedback.setString(1, module_id);
        getFeedback.setString(2, user_id);
        ResultSet rset = getFeedback.executeQuery();
        
        return rset;
    }
    
    public static void printModuleFeedback(PrintWriter out, Connection conn, String module_id, String user_id, HttpServletRequest request) {
        try {
            ResultSet rset = getModuleFeedback(conn, module_id, user_id);
            while (rset.next()) {
                String module_feedback_id = rset.getString("module_feedback_id");
                String author_id = rset.getString("author_id");
                String author = UserHelper.getFullNameById(conn, author_id);
                String content = rset.getString("module_feedback_content");
                
                out.println("<form action=\"deleteFeedback\" class=\"module-comment-container\">");
                out.println("<input type=\"hidden\" name=\"module_id\" value=\"" + module_id + "\">");
                out.println("<input type=\"hidden\" name=\"user_id\" value=\"" + user_id + "\">");
                out.println("<h3>" + author + " said:</h3>");
                out.println("<p>" + content + "</p>");
                HtmlHelper site = new HtmlHelper(out);
                
                String currentuser = UserHelper.getUserId(conn, UserHelper.getUserName(request));
                String role = UserHelper.getUserRole(request);
                
                if (role.equals("Lecturer") || currentuser.equals(user_id)) {                    
                    site.printDeleteButton("deleteFeedback", "module_feedback_id", module_feedback_id);
                }
                
                out.println("</form>");
            }
        } catch (SQLException ex) {
            out.println(ex);
        }
    }
    public static String deleteModuleFeedback(Connection conn, String module_feedback_id) {
        
        String results = "";

        try {
            PreparedStatement deleteFeedback = conn.prepareStatement("DELETE FROM module_feedback WHERE module_feedback_id = ?");
            deleteFeedback.setString(1, module_feedback_id);
            int amount = deleteFeedback.executeUpdate();
            
            results += amount + " feedback deleted.";
        } catch (SQLException ex) {
            results += "SQL error: " + ex;
            return results;
        }
        
        return results;
    }
}
