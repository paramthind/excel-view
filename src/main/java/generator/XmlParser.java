package generator;

import com.thoughtworks.xstream.XStream;

import java.io.File;

/**
 * Created by paramveersingh on 12/18/14.
 */
public class XmlParser {

    private XStream xstream;

    public XmlParser() {
        this.xstream = new XStream();
        xstream.processAnnotations(ExcelConfigBean.class);
        xstream.processAnnotations(GroupConfigurationBean.class);
        xstream.processAnnotations(PropertyConfigurationBean.class);
    }

    /**
     * Parse the xml file located in given xmlFilePath.
     *
     * @param xmlFilePath
     * @return ExcelConfigBean
     */
    public ExcelConfigBean parse(String xmlFilePath) {
        return (ExcelConfigBean) this.xstream.fromXML(new File(xmlFilePath));
    }
}
