package generator;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamImplicit;

import java.util.List;

/**
 * Created by paramveersingh on 12/16/14.
 */
@XStreamAlias("groupConfigurationBean")
public class GroupConfigurationBean {
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSubtitle() {
        return subtitle;
    }

    public void setSubtitle(String subtitle) {
        this.subtitle = subtitle;
    }

    public List<PropertyConfigurationBean> getPropertyConfigurations() {
        return propertyConfigurations;
    }

    public void setPropertyConfigurations(List<PropertyConfigurationBean> propertyConfigurations) {
        this.propertyConfigurations = propertyConfigurations;
    }

    @XStreamAlias("title")
    private String title;
    @XStreamAlias("subtitle")
    private String subtitle;
    @XStreamImplicit(itemFieldName = "propertyConfigurations")
    private List<PropertyConfigurationBean> propertyConfigurations;
}
