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
 * @author tobia
 */
public class AnnouncementHelper {
    
    
    
    
    public static ResultSet getAnnouncements (int amount, Connection conn, String course_id) {
        try {
            String sqlString = "SELECT * FROM announcement WHERE course_id LIKE ? ORDER BY announcement_id DESC LIMIT ?";
            PreparedStatement getAnnouncements = conn.prepareStatement(sqlString);
            getAnnouncements.setString(1, course_id);
            getAnnouncements.setInt(2, amount);
            ResultSet rset = getAnnouncements.executeQuery();
            
            return rset;
            
        } catch (SQLException ex) {
            System.out.println(ex);
        }
        return null;
    }
    
    public static int printAnnouncements (PrintWriter out, Connection conn, ResultSet rset, String role) {
        HtmlHelper site = new HtmlHelper(out);
        
        try {
            int amount = 0;
            while (rset.next()) {
                String id = rset.getString("announcement_id");
                String title = rset.getString("announcement_title");
                String content = rset.getString("announcement_content");
                String author_id = rset.getString("announcement_author_id");
                String author = UserHelper.getFullNameById(conn, author_id);
                
                ResultSet userInfo = UserHelper.getUserDetails(out, conn, author_id);
                String profilePic = "";
                while (userInfo.next()) {
                    profilePic = userInfo.getString("user_pic_url");
                }
                
                out.println("<div class=\"one-announcement\">");
                out.println("<div class=\"author-byline\">");
                out.println("<img src=\"images/profiles/" + profilePic + "\">");
                out.println("<form id=\"getAuthor\" action=\"oneUser\" method=\"get\" onclick=\"submit(\'getAuthor\')\">");
                out.println("<input type=\"hidden\" name=\"user_id\" value=\"" + author_id + "\">");
                out.println("<p class=\"announcement-author\">" + author + ":</p>");
                out.println("</form>");
                out.println("</div>");
                out.println("<h3 class=\"announcement-title\">" + title + "</h3>");
                out.println("<p class=\"announcement-content\">" + content + "</p>");
                
                if (role.toLowerCase().equals("lecturer")) {
                    site.printDeleteButton("deleteAnnouncement", "announcement_id", id);
                }
                
                out.println("</div>");
                out.println("<br>");
                amount++;
            }
            return amount;
        } catch (SQLException ex) {
            out.println(ex);
        }
        return 0;
    }
    
    public static void printLatestAnnouncement(Connection conn, PrintWriter out, String course_id) {
        ResultSet rset = getAnnouncements(1, conn, course_id);
        printAnnouncements(out, conn, rset, "");
    }
    
    
    public static String deleteAnnouncement(String announcement_id, Connection conn) {
        
        String results = "";

        try {
            PreparedStatement deleteAnnouncement = conn.prepareStatement("DELETE FROM announcement WHERE announcement_id = ?");
            deleteAnnouncement.setString(1, announcement_id);
            int amount = deleteAnnouncement.executeUpdate();
            
            results += amount + " announcement(s) deleted.";
        } catch (SQLException ex) {
            results += "SQL error: " + ex;
            return results;
        }
        
        return results;
    }
}
