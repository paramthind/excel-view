package servlet;

import javax.servlet.*;
import javax.servlet.http.*;
import java.io.*;

/**
 * Created by paramveersingh on 12/17/14.
 */

public class FileServlet extends HttpServlet {

    private static final String EXCEL_DIR = "excel_directory";
    private static String directoryName;

    @Override
    public void init() throws ServletException{
        super.init();
        this.directoryName = this.getServletConfig().getInitParameter(EXCEL_DIR);
        if (this.directoryName == null){
                this.directoryName = "";
        }

    }

    public void doGet(HttpServletRequest request,HttpServletResponse response)
            throws ServletException, IOException {

        String filename = request.getParameter("filename");

        File file = new File(directoryName, filename);
        byte buffer[] = new byte[(int) file.length()];
        InputStream fis = new FileInputStream(file);
        fis.read(buffer);



        if (filename.contains(".xls")) {
            response.setContentType("application/vnd.ms-excel");
            response.setHeader("Cache-Control", "no-cache");
            response.addHeader("content-disposition",
                    "attachment; filename=" + filename);
        }else{
            response.addHeader("content-disposition",
                    "inline; filename=" + filename);
        }

        ServletOutputStream out = response.getOutputStream();
        out.write(buffer);
        out.flush();
        out.close();
    }

    public void doPost(HttpServletRequest request,HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request,response);
    }

    public static final String getDirectoryName(){
        return directoryName;
    }

}
