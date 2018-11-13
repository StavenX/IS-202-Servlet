package newfileupload;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.Connection;
import java.sql.SQLException;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;


/**
 * This class is a common superclass that is the result of factoring out the
 * common code from UploadFile and DownloadFile.
 *
 * @author evenal
 */
public abstract class FileTransfer extends HttpServlet {
    // sql code for inserting a row

    protected static final String INSERT_SQL
            = "insert into file (filename, filesize, filetype, filecontent) values (?,?,?,?)";

    // sql code for selecting a row
    protected static final String SELECT_SQL = "select fileid,filename,filesize,filetype from file";

    // sql code for getting the last auto generated primary key
    protected static final String LAST_AUTO_ID = "select last_insert_id()";

    // html code to start a page
    protected static final String UPLOAD_PAGE_START = "<!DOCTYPE html>\n"
            + "<html>\n"
            + "  <head>\n"
            + "    <title>Upload</title>\n"
            + "    <meta charset='UTF-8' />\n"
            + "   <link rel='stylesheet' type='text/css' href='example.css'\n/>"
            + "  </head>\n"
            + "  <body>\n"
            + "    <h1>File upload</h1>\n";

    // Start of a table that shows the uploaded files, with download links
    protected static final String UPLOAD_TABLE_START = "    <h3>Uploaded files:</h3>\n"
            + "    <p>Click on the filename to download a file</p>"
            + "    <table>\n"
            + "      <tr><th>Fileid:</th><th>Filename:</th><th>File size:</th><th>ContentType:</th></tr>\n";

    // Format string for a html table row, which displays the info  for one
    // uploaded file. it produces html code like this:
    // <tr><td>ID</td><td><a href='DownloadFile/NAME?fileid=ID'>NAME</a></td><td>SIZE</td><td>TYPE</td></tr>
    protected static final String TABLE_ROW = "<tr><td>%d</td>" // fileid
            + "<td><a href='DownloadFile/%s?fileid=%d'>%s</a>" // name id name
            + "</td><td>%d</td><td>%s</td></tr>\n"; // size type

    // all the end tags :-)
    protected static final String TABLE_DOC_END = "</table>\n</body>\n</html>\n";

    /**
     * Get a database connection
     *
     * @return
     * @throws NamingException
     * @throws SQLException
     */
    protected Connection getConnection() throws NamingException, SQLException {
        Context inital = new InitialContext();
        Context env = (Context) inital.lookup("java:/comp/env");
        DataSource ds = (DataSource) env.lookup("jdbc/localhostDS");
        Connection con = ds.getConnection();
        con.setAutoCommit(false);
        return con;
    }

    /**
     * Helper method for copying files. Reads chunks of bytes from the in stream
     * from, and writes them to outputstream to
     *
     * @param from
     * @param to
     * @throws IOException
     */
    protected void copyFile(InputStream from, OutputStream to) throws IOException {
        int read = 0;
        byte[] buffer = new byte[1024];
        while ((read = from.read(buffer)) != -1) {
            to.write(buffer, 0, read);
        }
    }

    /**
     * Subclasses must override this method.
     *
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    public abstract void processRequest(HttpServletRequest request,
                                        HttpServletResponse response)
            throws ServletException, IOException;

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
