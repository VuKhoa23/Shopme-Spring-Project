package com.shopme.admin.user;

import com.shopme.common.entity.User;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.xssf.usermodel.*;


import java.io.IOException;
import java.util.List;

public class UserExcelExporter extends AbstractExporter {

    private XSSFWorkbook workbook;
    private XSSFSheet sheet;
    public UserExcelExporter(){
        workbook = new XSSFWorkbook();

    }

    private void writeHeaderLine(){

        sheet = workbook.createSheet("Users");
        XSSFRow row = sheet.createRow(0);

        XSSFCellStyle cellStyle = workbook.createCellStyle();
        XSSFFont font = workbook.createFont();
        font.setBold(true);
        font.setFontHeight(16);
        cellStyle.setFont(font);

        createCell(row, 0 ,"Users ID", cellStyle);
        createCell(row, 1 ,"Email", cellStyle);
        createCell(row, 2 ,"First Name", cellStyle);
        createCell(row, 3 ,"Last Name", cellStyle);
        createCell(row, 4 ,"Roles", cellStyle);
        createCell(row, 5 ,"Enabled", cellStyle);

    }
    private void createCell(XSSFRow row, int colIndex, Object value, CellStyle cellStyle){

        XSSFCell cell = row.createCell(colIndex);
        sheet.autoSizeColumn(colIndex);
        if(value instanceof Integer){
            cell.setCellValue((Integer)value);
        }
        else if(value instanceof Boolean){
            cell.setCellValue((Boolean) value);
        }
        else{
            cell.setCellValue((String)value);
        }
        cell.setCellStyle(cellStyle);
    }
    public void export(List<User> users, HttpServletResponse response) throws IOException {
        super.setResponseHeader(response, "application/octet-stream", ".xlsx");

        writeHeaderLine();
        writeDataLine(users);

        ServletOutputStream outputStream = response.getOutputStream();
        workbook.write(outputStream);
        workbook.close();
        outputStream.close();
    }

    private void writeDataLine(List<User> users) {
        XSSFCellStyle cellStyle = workbook.createCellStyle();
        XSSFFont font = workbook.createFont();
        font.setFontHeight(13);
        cellStyle.setFont(font);

        int rowIndex= 1;
        for (User user : users) {
            XSSFRow row = sheet.createRow(rowIndex++);
            int colIndex = 0;
            createCell(row, colIndex, user.getId(), cellStyle);
            colIndex++;
            createCell(row, colIndex, user.getEmail(), cellStyle);
            colIndex++;
            createCell(row, colIndex, user.getFirstName(), cellStyle);
            colIndex++;
            createCell(row, colIndex, user.getLastName(), cellStyle);
            colIndex++;
            createCell(row, colIndex, user.getRoles().toString(), cellStyle);
            colIndex++;
            createCell(row, colIndex, user.isEnabled(), cellStyle);
        }


    }
}
