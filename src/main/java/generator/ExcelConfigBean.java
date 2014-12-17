package generator;

/**
 * Created by paramveersingh on 12/16/14.
 */
public class ExcelConfigBean {
    private String titleFont = "Tahoma", subTitleFont = "Tahoma", normalFont = "Tahoma";
    private float titleFontSize = 18, normalFontSize = 8;

    public String getTitleFont() {
        return titleFont;
    }

    public void setTitleFont(String titleFont) {
        this.titleFont = titleFont;
    }

    public String getSubTitleFont() {
        return subTitleFont;
    }

    public void setSubTitleFont(String subTitleFont) {
        this.subTitleFont = subTitleFont;
    }

    public String getNormalFont() {
        return normalFont;
    }

    public void setNormalFont(String normalFont) {
        this.normalFont = normalFont;
    }

    public float getTitleFontSize() {
        return titleFontSize;
    }

    public void setTitleFontSize(float titleFontSize) {
        this.titleFontSize = titleFontSize;
    }

    public float getNormalFontSize() {
        return normalFontSize;
    }

    public void setNormalFontSize(float normalFontSize) {
        this.normalFontSize = normalFontSize;
    }

}
