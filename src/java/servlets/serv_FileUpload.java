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

@WebServlet(name = "serv_FileUpload", urlPatterns = { "/serv_FileUpload" }, loadOnStartup = 1)
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
                
		PrintWriter out = response.getWriter();
		out.append("<!DOCTYPE html>\r\n")
		.append("<html>\r\n")
		.append("    <head>\r\n")
		.append("        <title>File Upload Form</title>\r\n")
                .append("            <link rel=\"stylesheet\" type=\"text/css\" href=\"css/theme.css\">")
		.append("    </head>\r\n")
		.append("    <body>\r\n");
		out.append("<h1>Upload file</h1>\r\n");
		out.append("<form method=\"POST\" action=\"serv_FileUpload\" ")
		.append("enctype=\"multipart/form-data\">\r\n");
                
		out.append("<input type=\"file\" name=\"fileName1\"/><br/><br/>\r\n");
		//writer.append("<input type=\"file\" name=\"fileName2\"/><br/><br/>\r\n");
		out.append("<input type=\"submit\" value=\"Submit\"/>\r\n");
		out.append("</form>\r\n");
		out.append("    </body>\r\n").append("</html>\r\n");
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