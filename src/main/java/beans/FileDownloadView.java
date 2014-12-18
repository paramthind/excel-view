package beans;

/**
 * Created by paramveersingh on 12/17/14.
 */
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;
import javax.servlet.ServletContext;

import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;
import servlet.FileServlet;

@ManagedBean(name = "fileDownloadView")
@RequestScoped
public class FileDownloadView {

    private StreamedContent file;

    public FileDownloadView() {
        try {
            System.out.println("hi");
            File file1 = new File("/Users/paramveersingh/Documents/tabWorkbook.xls");
            byte buffer[] = new byte[(int) file1.length()];
            InputStream stream =  new FileInputStream(file1);

//                    ((ServletContext) FacesContext.getCurrentInstance().getExternalContext()
//                    .getContext()).getResourceAsStream("tabWorkbook.xls");

            System.out.println(stream.available());
//        file = new DefaultStreamedContent(stream, "image/jpg", "downloaded_optimus.jpg");
            file = new DefaultStreamedContent(stream, "application/vnd.ms-excel", "downloaded_optimus.xls");
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    public StreamedContent getFile() {
        try {
        return file;
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
}
