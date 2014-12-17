/**
 *
 */
package generator;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;

import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.*;

/**
 * Generator for excel workbook. It takes configuration from spring container.
 * It works only with sorted object list if group by feature is enabled
 * (configured).
 */
public class ExcelGenerator {

    private ExcelConfigBean excelConfig;
    private Object object;
    private String outFile;
    private String overrideSubtitle;
    private Workbook workbook;

    @SuppressWarnings("rawtypes")
    public ExcelGenerator(String configFile, String outFile) {
        ApplicationContext ctx = new FileSystemXmlApplicationContext("file:"
                + configFile);
        this.outFile = outFile;
        Map beans = ctx.getBeansOfType(ExcelConfigBean.class);
        if (beans.isEmpty()) {
            throw new IllegalArgumentException("There is no ExcelConfigBean beans.");
        }

        Iterator it = beans.entrySet().iterator();
        Map.Entry entry = (Map.Entry) it.next();
        excelConfig = (ExcelConfigBean) entry.getValue();
        this.workbook = new HSSFWorkbook();
    }

    public void include(Object object) {
        this.object = object;

    }

    public void include(Object object, String overrideSubtitle) {
        include(object);
        this.overrideSubtitle = overrideSubtitle;

    }


    public Workbook populate() throws Exception {
        // global font bold
        Font boldFont = workbook.createFont();
        boldFont.setBoldweight(Font.BOLDWEIGHT_BOLD);

        // title font & style
        Font titleFont = workbook.createFont();
        titleFont.setFontName(excelConfig.getTitleFont());
        titleFont.setFontHeight((short) (excelConfig.getTitleFontSize() * 20));
        titleFont.setBoldweight(Font.BOLDWEIGHT_BOLD);
        CellStyle titleStyle = workbook.createCellStyle();
        titleStyle.setFont(titleFont);
        titleStyle.setAlignment(CellStyle.ALIGN_LEFT);
        titleStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER);

        // header font & style
        Font headerFont = workbook.createFont();
        headerFont.setBoldweight(Font.BOLDWEIGHT_BOLD);
//        headerFont.setColor(IndexedColors.WHITE.getIndex());
        CellStyle headerStyle = workbook.createCellStyle();
        headerStyle.setFont(headerFont);
//        headerStyle.setFillPattern(CellStyle.SOLID_FOREGROUND);
//        headerStyle.setFillForegroundColor(IndexedColors.BLUE_GREY.getIndex());
        headerStyle.setAlignment(CellStyle.ALIGN_CENTER);
//        headerStyle.setBorderRight(CellStyle.BORDER_THIN);
//        headerStyle.setBorderBottom(CellStyle.BORDER_THIN);

        // total cell style
        CellStyle totalStyle = workbook.createCellStyle();
        totalStyle.setFont(boldFont);
        totalStyle.setAlignment(CellStyle.ALIGN_RIGHT);
        totalStyle.setWrapText(true);

        // loop over configured groups
        int groupCount = 0;
        GroupConfigurationBean group = excelConfig.getGroupConfigurations().get(0);
        List<PropertyConfigurationBean> propConfigs = group
                .getPropertyConfigurations();

        CellStyle[] columnWhiteStyles = new CellStyle[propConfigs.size()];
        CellStyle[] columnGreyStyles = new CellStyle[propConfigs.size()];

        // each group as separate sheet
        Sheet sheet = workbook
                .createSheet(group.getSubtitle() != null ? group
                        .getSubtitle() : ("Group" + groupCount));

        sheet.setColumnWidth(0, 15 * 256);
        sheet.setDefaultColumnStyle(0, totalStyle);
        // title row
        Row titleRow = sheet.createRow(0);
        titleRow.setHeightInPoints(28.5f);
        Cell titleCell = titleRow.createCell(0, Cell.CELL_TYPE_STRING);
        titleCell.setCellValue(overrideSubtitle != null ? overrideSubtitle
                : group.getSubtitle());
        titleCell.setCellStyle(titleStyle);
        sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, propConfigs
                .size()));
        // create a header row
        Row headerRow = sheet.createRow(1);
        headerRow.setHeightInPoints(15.75f);

        // data rows
        int objCount = 0, currentRow = 1;

        short numberFormat = workbook.createDataFormat().getFormat("##,##0.00");


        int propCount = 0;
        for (PropertyConfigurationBean pConfig : propConfigs) {
            Row row = sheet.createRow(currentRow++);
            Object value = getProperty(object, pConfig.getPropertyName());

            if (objCount == 0) {
                Cell headerCell = row.createCell(0, Cell.CELL_TYPE_STRING);
                headerCell.setCellValue(pConfig.getDisplayName());
                headerCell.setCellStyle(headerStyle);
                // fill column styles
                CellStyle formatWhiteStyle = workbook.createCellStyle();
                CellStyle formatGreyStyle = workbook.createCellStyle();
                formatGreyStyle
                        .setFillPattern(CellStyle.SOLID_FOREGROUND);
                formatGreyStyle
                        .setFillForegroundColor(IndexedColors.GREY_25_PERCENT
                                .getIndex());

                if (value instanceof Date) {
                    formatWhiteStyle.setDataFormat((short) 0xe);
                    formatGreyStyle.setDataFormat((short) 0xe);
                } else if (value instanceof Number) {
                    formatWhiteStyle.setDataFormat(numberFormat);//(short) 7);
                    formatGreyStyle.setDataFormat(numberFormat);//(short) 7);
                    if (pConfig.getFontStyle() != null) {
                        formatWhiteStyle.setFont(boldFont);
                        formatGreyStyle.setFont(boldFont);
                    }
                }
                columnWhiteStyles[propCount] = formatWhiteStyle;
                columnGreyStyles[propCount] = formatGreyStyle;
            }
            Cell cell = row.createCell(1);
            if (currentRow % 2 == 0)
                cell.setCellStyle(columnWhiteStyles[propCount]);
//                    else {
//                        cell.setCellStyle(columnGreyStyles[propCount]);
//                    }
            setCellValue(cell, value);

            propCount++;
        }

        objCount++;
        currentRow++;

        return workbook;
    }

    static Object getProperty(Object bean, String name) throws Exception {
        Object value = null;
        try {
            value = PropertyUtils.getNestedProperty(bean, name);
        } catch (IllegalArgumentException e) {
            String msg = e.getMessage();
            if (msg != null && msg.contains("Null property value for"))
                value = null;
            else
                throw e;
        } catch (InvocationTargetException ie) {
            System.out.println("Error while invocation of " + name);
        }
        return value;
    }

    public void generate() throws Exception {
        populate();
        writeToFile();
    }

    void writeToFile() {
        // Write the output to a file
        FileOutputStream fileOutStream = null;
        try {
            fileOutStream = new FileOutputStream(outFile);
            workbook.write(fileOutStream);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (fileOutStream != null)
                    fileOutStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    static void setCellValue(Cell cell, Object value) {
        if (value == null) {
            cell.setCellType(Cell.CELL_TYPE_BLANK);
            cell.setCellValue((String) null);
        } else if (value instanceof Date)
            cell.setCellValue((Date) value);
        else if (value instanceof Number)
            cell.setCellValue(((Number) value).doubleValue());
        else if (value instanceof Boolean)
            cell.setCellValue(((Boolean) value).booleanValue());
        else
            cell.setCellValue(value.toString());
    }

    static void copyCell(Cell source, Cell target) {
        int cellType = source.getCellType();
        if (cellType == Cell.CELL_TYPE_STRING)
            target.setCellValue(source.getStringCellValue());
        else if (cellType == Cell.CELL_TYPE_FORMULA) {
            target.setCellFormula(source.getCellFormula());
        } else if (cellType == Cell.CELL_TYPE_NUMERIC) {
            target.setCellValue(source.getNumericCellValue());
        }
        target.setCellStyle(source.getCellStyle());
    }

    public static void main(String[] args) throws Exception {
        ExcelGenerator excelGenerator = new ExcelGenerator(
                "/Users/paramveersingh/Documents/work/intellijWorkspace/excel-view/src/main/resources/tabConfig.xml",
                "tabWorkbook.xls");
        excelGenerator.include(new TabBean());
        excelGenerator.generate();
    }

}
