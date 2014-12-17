package generator;

import java.util.List;

/**
 * Created by paramveersingh on 12/16/14.
 */
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

    private String title, subtitle;
    private List<PropertyConfigurationBean> propertyConfigurations;
}
