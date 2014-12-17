package generator;

/**
 * Created by paramveersingh on 12/16/14.
 */
public class PropertyConfigurationBean {

    private String propertyName, displayName, fontStyle, value;

    public String getPropertyName() {
        return propertyName;
    }

    public void setPropertyName(String propertyName) {
        this.propertyName = propertyName;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String desplayName) {
        this.displayName = desplayName;
    }

    public String getFontStyle() {
        return fontStyle;
    }

    public void setFontStyle(String fontStyle) {
        this.fontStyle = fontStyle;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

}
