package servlets;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import helpers.HtmlHelper;

@WebServlet(name = "serv_FileUpload", urlPatterns = { "/FileUpload" }, loadOnStartup = 1)
@MultipartConfig(
                fileSizeThreshold = 6291456,    // 6 MB
		maxFileSize =       10485760L,  // 10 MB
		maxRequestSize =    20971520L   // 20 MB
)

public class serv_FileUpload extends HttpServlet {

	private static final String UPLOAD_DIR = "uploads";
        
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
                       
                    out.append("<form method=\"POST\" action=\"FileUpload\" ")
                    .append("enctype=\"multipart/form-data\">\r\n");
                    out.append("<input class=\"button\" type=\"file\" name=\"fileName1\"/><br/><br/>\r\n");
                    //writer.append("<input type=\"file\" name=\"fileName2\"/><br/><br/>\r\n");
                    out.append("<input class=\"button\" type=\"submit\" value=\"Submit\"/>\r\n");
                    out.append("</form>\r\n");

                    site.printEnd();
                }
		
		
	}
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html");
		response.setCharacterEncoding("UTF-8");
                
		// gets absolute path of the web application
		String applicationPath = request.getServletContext().getRealPath("");
		// constructs path of the directory to save uploaded file
		String uploadFilePath = applicationPath + File.separator + UPLOAD_DIR;
                
		// creates upload folder if it does not exists
		File uploadFolder = new File(uploadFilePath);
		if (!uploadFolder.exists()) {
			uploadFolder.mkdirs();
		}
                
		PrintWriter writer = response.getWriter();
                
                // write all files in upload folder / location
                for (Part part : request.getParts()) {
                    if (part != null && part.getSize() > 0) {
                        String fileName = part.getSubmittedFileName();
                        String contentType = part.getContentType();

                        // allows only JPEG files to be uploaded, might
                                        // be useful later
                        /*if (!contentType.equalsIgnoreCase("image/jpeg")) {
                            continue;
                        }*/

                                        // To fix Microsoft Edge handling filenames
                                        String[] fileNameSplit = fileName.split("\\\\");
                        fileName = fileNameSplit[fileNameSplit.length - 1];
                        part.write(uploadFilePath + File.separator + fileName);

                        writer.append("File successfully uploaded to "
                                + uploadFolder.getAbsolutePath()
                                + File.separator
                                + fileName
                                + "<br>\r\n");
                    }
                }     
	}
}