package cn.bytecloud.smartCommunity.util;

import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.xssf.usermodel.*;

import javax.servlet.ServletOutputStream;
import java.io.IOException;
import java.util.Vector;

public class ExcelUtil {

    /**
     * @param sheetName
     * @param titles
     * @param values    void
     * @author haocj
     * @date 2018年6月4日10:20:59
     * @MethodsName: exportToExcelHSSF
     * @Description: 导出2003版 后缀为xls 用于直接导出，返回OutputStream
     */
    public static void exportToExcelHSSF(String sheetName, Vector<String> titles, Vector<Vector<String>> values,
                                         ServletOutputStream out) {

        int sheetNum = 1;// 工作薄sheet编号
        int bodyRowCount = 1;//正文内容行号
        int currentRowCount = 1;// 当前的行号
        int perPageNum = 50000;// 每个工作薄显示50000条数据

        // 第一步，创建一个webbook，对应一个Excel文件
        HSSFWorkbook wb = new HSSFWorkbook();
        // 第二步，在webbook中添加一个sheet,对应Excel文件中的sheet
        HSSFSheet sheet = wb.createSheet(sheetName);
        // 第三步，在sheet中添加表头第0行,注意老版本poi对Excel的行数列数有限制short
        HSSFRow row = sheet.createRow((int) 0);
        // 第四步，创建单元格，并设置值表头 设置表头居中
        HSSFCellStyle style = wb.createCellStyle();
        style.setAlignment(HorizontalAlignment.CENTER); // 创建一个居中格式

        HSSFCell cell = row.createCell(0);

        for (int i = 0; i < titles.size(); i++) {
            cell.setCellValue(titles.elementAt(i));
            cell.setCellStyle(style);
            cell = row.createCell(i + 1);
        }

        for (int i = 0; i < values.size(); i++) {

            Vector<String> item = values.elementAt(i);

            row = sheet.createRow(bodyRowCount);

            for (int j = 0; j < item.size(); j++) {

                // 第四步，创建单元格，并设置值
                row.createCell(j).setCellValue(item.elementAt(j));

            }
            if (currentRowCount % perPageNum == 0) {//每个工作薄显示50000条数据
                sheet = null;
                sheetNum++;//工作薄编号递增1
                bodyRowCount = 0;
                sheet = wb.createSheet(sheetName + sheetNum);//创建一个新的工作薄
                //标题
                row = sheet.createRow(0);
                //第一行写入标题行
                cell = row.createCell((short) 0);//序号
                for (int j = 0; j < titles.size(); j++) {
                    cell.setCellValue(titles.elementAt(j));
                    cell.setCellStyle(style);
                    cell = row.createCell(i + 1);
                }
            }
            bodyRowCount++;
            currentRowCount++;//当前行号递增1
        }
        try {
            wb.write(out);
        } catch (IOException e) {

            // TODO Auto-generated catch block
            e.printStackTrace();

        }
    }

    /**
     * @param sheetName
     * @param titles
     * @param values    void
     * @author haocj
     * @date 2018年6月4日10:20:59
     * @MethodsName: exportToExcelXSSF
     * @Description: 导出2007版 后缀为xls 用于直接导出，返回OutputStream
     */
    public static void exportToExcelXSSF(String sheetName, Vector<String> titles, Vector<Vector<String>> values,
                                         ServletOutputStream out) {

        int sheetNum = 1;// 工作薄sheet编号
        int bodyRowCount = 1;//正文内容行号
        int currentRowCount = 1;// 当前的行号
        int perPageNum = 50000;// 每个工作薄显示50000条数据

        // 第一步，创建一个webbook，对应一个Excel文件
        XSSFWorkbook wb = new XSSFWorkbook();
        // 第二步，在webbook中添加一个sheet,对应Excel文件中的sheet
        XSSFSheet sheet = wb.createSheet(sheetName);
        // 第三步，在sheet中添加表头第0行,注意老版本poi对Excel的行数列数有限制short
        XSSFRow row = sheet.createRow((int) 0);
        // 第四步，创建单元格，并设置值表头 设置表头居中
        XSSFCellStyle style = wb.createCellStyle();
        style.setAlignment(HorizontalAlignment.CENTER); // 创建一个居中格式
        writeTitleContent(sheet, style, titles);//写入标题

        for (int i = 0; i < values.size(); i++) {

            Vector<String> item = values.elementAt(i);

            row = sheet.createRow(bodyRowCount);

            for (int j = 0; j < item.size(); j++) {
                // 第四步，创建单元格，并设置值
                row.createCell(j).setCellValue(item.elementAt(j));
            }
            if (currentRowCount % perPageNum == 0) {//每个工作薄显示50000条数据
                sheet = null;
                sheetNum++;//工作薄编号递增1
                bodyRowCount = 0;//正文内容行号置位为0
                sheet = wb.createSheet(sheetName + sheetNum);//创建一个新的工作薄
                writeTitleContent(sheet, style, titles);//写入标题
            }
            bodyRowCount++;//正文内容行号递增1
            currentRowCount++;//当前行号递增1

        }
        try {
            wb.write(out);
        } catch (IOException e) {

            // TODO Auto-generated catch block
            e.printStackTrace();

        }
    }

    /**
     * 写入标题行
     *
     * @param titles
     * @param
     * @return
     */
    public static void writeTitleContent(XSSFSheet sheet, XSSFCellStyle cellStyle, Vector<String> titles) {
        XSSFRow row = null;
        XSSFCell cell = null;
        //标题
        row = sheet.createRow(0);
        //第一行写入标题行
        cell = row.createCell((short) 0);//序号
        for (int i = 0; i < titles.size(); i++) {
            cell.setCellValue(titles.elementAt(i));
            cell.setCellStyle(cellStyle);
            cell = row.createCell(i + 1);
        }
    }
}
