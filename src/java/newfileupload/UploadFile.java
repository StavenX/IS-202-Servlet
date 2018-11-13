package newfileupload;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.naming.NamingException;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;


/**
 * This servlet handles the server side of a file upload. It extracts the file
 * from the request object, and copies the file contents into a blob, which is
 * stored in a database. The table definition is in FileTable.sql
 *
 *
 *
 * @author evenal
 */
@WebServlet(name = "UploadFile", urlPatterns = {"/UploadFile"})
@MultipartConfig
public class UploadFile extends FileTransfer {

    /**
     * The Upload class is just a box that is used to hold file information
     * retrieved from a request, or from a db query.
     */
    private class Upload {

        long fileid;
        String filename;
        long filesize;
        String filetype;
        Blob filecontent;
        InputStream inputStream;

        public Upload(Part filepart) throws IOException {
            this.filename = filepart.getSubmittedFileName();
            this.filesize = filepart.getSize();
            this.filetype = filepart.getContentType();
            this.inputStream = filepart.getInputStream();
        }
    }

    public void processRequest(HttpServletRequest request,
                               HttpServletResponse response)
            throws ServletException, IOException {
        try {
            saveUpload(request, response);
            writeResponse(request, response);
        } catch (SQLException | NamingException e) {
            throw new ServletException(e);
        }
    }

    private void saveUpload(HttpServletRequest request,
                            HttpServletResponse response)
            throws ServletException, IOException, SQLException, NamingException {
        // get the fileupload part of the request
        Part filePart = request.getPart("file");
        Upload upload = new Upload(filePart);
        request.setAttribute("upload", upload);

        try (Connection conn = getConnection();
             PreparedStatement stmt = prepInsStmt(conn, upload)) {
            stmt.executeUpdate();
            conn.commit();
        }
    }

    private PreparedStatement prepInsStmt(Connection conn, Upload upload)
            throws SQLException, IOException {
        PreparedStatement ps = conn.prepareStatement(INSERT_SQL);
        ps.setString(1, upload.filename);
        ps.setLong(2, upload.filesize);
        ps.setString(3, upload.filetype);
        ps.setBlob(4, upload.inputStream);
        return ps;
    }

    /*
     * This method writes a page with download links for all uploaded files An
     * alternative would be to write a page that simply confirms that the file
     * was successfully uploaded.
     */
    private void writeResponse(HttpServletRequest request,
                               HttpServletResponse response)
            throws IOException, ServletException, SQLException, NamingException {
        PrintWriter out = response.getWriter();

        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(SELECT_SQL);
             ResultSet uploads = ps.executeQuery()) {
            out.print(UPLOAD_PAGE_START);
            out.print(UPLOAD_TABLE_START);

            while (uploads.next()) {
                // write one table row, some of the fields are repeated
                // because they are useds several times in the template
                out.format(TABLE_ROW,
                           uploads.getLong(1), // id
                           uploads.getString(2),// filename
                           uploads.getLong(1), // id
                           uploads.getString(2),// filename
                           uploads.getLong(3), // filesize
                           uploads.getString(4)); // mimetype
            }
            out.format(TABLE_DOC_END);
        }
    }
}
