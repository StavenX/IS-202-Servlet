package servlets;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import helpers.HtmlHelper;
import javax.servlet.annotation.MultipartConfig;
import newfileupload.UploadFile; 

@WebServlet(name = "serv_FileUpload", urlPatterns = { "/FileUpload" }, loadOnStartup = 1)
@MultipartConfig

public class serv_FileUpload extends HttpServlet {

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
            
		response.setContentType("text/html");
		response.setCharacterEncoding("UTF-8");
                
                try (PrintWriter out = response.getWriter()) {
                    
                    HtmlHelper site = new HtmlHelper(out);
                    site.printHead("File Upload", "");
                    
                    out.println("Upload a file");
                    if (request.isUserInRole("Lecturer")) {
                        out.println("ADMIN_ACCESS");
                    } else if (request.isUserInRole("Student")) {
                        out.println("STUDENT_ACCESS");
                    }
                       
                    // This is the form that contains the upload button, etc. 
                    out.println("<form method='POST' action='UploadFile' enctype='multipart/form-data'>");
                    out.println("<table>");
                    out.println("<tr>");
                    out.println("<th>Select File:</th>");
                    out.println("<td><input type='file' name='file'/></td>");
                    out.println("</tr>");
                    out.println("<tr>");
                    out.println("<td colspan='2'>");
                    out.println("<input type='submit' value='Upload selected file'>");
                    out.println("</td>");
                    out.println("</tr>");
                    out.println("</table>");
                    out.println("</form>");
                    
                    site.printEnd();
                }	
	}
        
	/*@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
            
		response.setContentType("text/html");
		response.setCharacterEncoding("UTF-8");
                
		UploadFile uf = new UploadFile(); 
                uf.processRequest(request, response);
	}*/
}