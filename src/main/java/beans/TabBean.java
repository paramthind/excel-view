package beans;

import generator.ExcelGenerator;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

@ManagedBean(name = "tabBean")
@SessionScoped
public class TabBean {

    private String userName1 = "Babu";
    private String address = "USA";


    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getUserName1() {
        return userName1;
    }

    public void setUserName1(String userName1) {
        this.userName1 = userName1;
    }

    public StreamedContent getExcelFile() {
        String outFile = "tmpfile.xml";
        File file1 = null;
        try {
            //generate excel file
            ExcelGenerator excelGenerator = new ExcelGenerator(
                    this.getClass().getClassLoader().getResource("tabConfig.xml").getFile(),
                    outFile);
            excelGenerator.include(new TabBean());
            excelGenerator.generate();

            //fetch file for download
            file1 = new File(outFile);
            InputStream stream = new FileInputStream(file1);
            return new DefaultStreamedContent(stream, "application/vnd.ms-excel", "downloaded_optimus.xls");

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (file1 != null && file1.exists()) {
                file1.delete();
            }
        }
        return null;
    }
}