package newfileupload;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.naming.NamingException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/**
 * This class is used to download uploaded files
 *
 * @author evenal
 */
@WebServlet(name = "DownloadFile", urlPatterns = {"/DownloadFile/*"})
public class DownloadFile extends FileTransfer {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods. The fileid is retrieved from a request parameter, and then used
     * to select the right tablerow.
     *
     * Then the file is streamed from the blob to the body of the response
     * object
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    public void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String tmp = request.getParameter("fileid");
        long fileid = Integer.valueOf(tmp);

        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareCall("select "
                     + "filename,filesize,filetype,filecontent"
                     + " from file where fileid = ?")) {
            ps.setLong(1, fileid);
            ResultSet rs = ps.executeQuery();

            // go to first (and only) row in the resultset
            if (rs.next()) {
                String filename = rs.getString(1);
                int fileSize = rs.getInt(2);
                String contentType = rs.getString(3);
                InputStream in = rs.getBinaryStream(4);
                OutputStream out = response.getOutputStream();
                // make sure to write all headers before starting to write
                // the file
                response.setContentType(contentType);
                response.setContentLength(fileSize);
                copyFile(in, out);
                response.setHeader("Content-disposition",
                                   String.format("attachment; filename='%s'",
                                                 filename));
            }
        } catch (SQLException | NamingException e) {
            throw new ServletException(e);
        }
    }

}
