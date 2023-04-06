package com.example.IliushynParser.Services;

import com.example.IliushynParser.Models.ProductData;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;


@Service
public class TableGenerationService {
    private HSSFWorkbook workbook;
    private HSSFSheet sheet;

    public TableGenerationService(){
        workbook = new HSSFWorkbook();
        sheet = workbook.createSheet("ParsedRozetka");
    }

    public HSSFWorkbook GetWorkbook(){
        return workbook;
    }

    public HSSFSheet GetSheet(){
        return sheet;
    }

    private void AddColumnCaptions(Row row, int numberCell, String captions){
        row.createCell(numberCell).setCellValue(captions);
    }

    public void CreateColumnCaptions(List<String> captions){
        int numberCell = 0;
        Row row = GetSheet().createRow(0);
        for(String capt : captions){
            AddColumnCaptions(row, numberCell, capt);
            numberCell += 1;
        }
    }

    private void FillOutSheet(List<ProductData> adList){
        int rowNum = 0;
        for (ProductData dataModel : adList) {
            FillExcel(sheet, ++rowNum, dataModel);
        }
    }

    private static void FillExcel(HSSFSheet sheet, int rowNum, ProductData dataModel) {
        Row row = sheet.createRow(rowNum);
        sheet.autoSizeColumn(rowNum);
        row.createCell(0).setCellValue(dataModel.GetSearch());
        sheet.autoSizeColumn(0);
        row.createCell(1).setCellValue(dataModel.GetInternalNumber());
        sheet.autoSizeColumn(1);
        row.createCell(2).setCellValue(dataModel.GetDescription());
        sheet.autoSizeColumn(2);
        row.createCell(3).setCellValue(dataModel.GetPrice());
        sheet.autoSizeColumn(3);
        row.createCell(4).setCellValue(dataModel.GetAvailability());
        sheet.autoSizeColumn(4);
        row.createCell(5).setCellValue(dataModel.GetProductLink());
        sheet.autoSizeColumn(5);
    }

    public void CreateExcel(String fileName, List<ProductData> adList){
        File directory_PATH = new File("./GeneratedFiles");
        if(directory_PATH.mkdirs())
        {
            System.out.println("Directory created successfully");
        }
        System.out.println(directory_PATH);
        FillOutSheet(adList);
        try (FileOutputStream out = new FileOutputStream(directory_PATH + "/" + fileName + ".xls")) {
            GetWorkbook().write(out);
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("Excel створено");
    }
}
