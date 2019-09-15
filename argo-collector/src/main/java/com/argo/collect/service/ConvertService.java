package com.argo.collect.service;

import com.argo.common.domain.raw.RawEvent;
import com.argo.common.domain.raw.RawEventService;
import com.google.gson.Gson;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

@Service
public class ConvertService {

    @Value("${file.upload.default-path}")
    private String path;

    @Autowired
    RawEventService rawEventService;

    public File excelUpload(MultipartFile filePart) throws IOException {
        String sourceFileName = filePart.getOriginalFilename();
        String sourceFileNameExtension = FilenameUtils.getExtension(sourceFileName).toLowerCase();
        File destinationFile;
        String destinationFileName;

        //S3에 저장한다면 파일명 변환은 필요 없음
        do {
            destinationFileName = RandomStringUtils.randomAlphanumeric(32) + "." + sourceFileNameExtension;
            destinationFile = new File(path+"/" + destinationFileName);
        } while (destinationFile.exists());
        destinationFile.getParentFile().mkdirs();
        filePart.transferTo(destinationFile);

        return destinationFile;
    }

    public List<List<HashMap<String,String>>> excelToJson(File file)
    {
        List<List<HashMap<String,String>>> result = new ArrayList<>();
        try{
            FileInputStream fInputStream = new FileInputStream(file);

            Workbook excelWorkBook = new XSSFWorkbook(fInputStream);

            int totalSheetNumber = excelWorkBook.getNumberOfSheets();

            for(int i=0;i<totalSheetNumber;i++)
            {
                Sheet sheet = excelWorkBook.getSheetAt(i);

                String sheetName = sheet.getSheetName();

                if(sheetName != null && sheetName.length() > 0)
                {

                    List<List<String>> sheetDataTable = getSheetDataList(sheet);

                    result.add(getJSONStringFromList(sheetDataTable));

                }
            }
            excelWorkBook.close();

        }catch(Exception ex){
            ex.printStackTrace();
        }
        return result;
    }



    private List<List<String>> getSheetDataList(Sheet sheet)
    {
        List<List<String>> ret = new ArrayList<List<String>>();

        int firstRowNum = sheet.getFirstRowNum();
        int lastRowNum = sheet.getLastRowNum();

        if(lastRowNum > 0)
        {
            for(int i=firstRowNum; i<lastRowNum + 1; i++)
            {
                Row row = sheet.getRow(i);

                int firstCellNum = row.getFirstCellNum();
                int lastCellNum = row.getLastCellNum();

                List<String> rowDataList = new ArrayList<String>();

                for(int j = firstCellNum; j < lastCellNum; j++)
                {
                    Cell cell = row.getCell(j);

                    CellType cellType = cell.getCellType();

                    if(cellType == CellType.NUMERIC && DateUtil.isCellDateFormatted(cell)){
                        Date date = cell.getDateCellValue();
                        String dateString = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
                        rowDataList.add(dateString);
                    }else if(cellType == CellType.NUMERIC)
                    {
                        double numberValue = cell.getNumericCellValue();

                        String stringCellValue = BigDecimal.valueOf(numberValue).toPlainString();

                        rowDataList.add(stringCellValue);

                    }else if(cellType == CellType.STRING)
                    {
                        String cellValue = cell.getStringCellValue();
                        rowDataList.add(cellValue);
                    }else if(cellType == CellType.BOOLEAN)
                    {
                        boolean numberValue = cell.getBooleanCellValue();

                        String stringCellValue = String.valueOf(numberValue);

                        rowDataList.add(stringCellValue);

                    }else if(cellType == CellType.BLANK)
                    {
                        rowDataList.add("");
                    }
                }

                ret.add(rowDataList);
            }
        }
        return ret;
    }

    private List<HashMap<String,String>> getJSONStringFromList(List<List<String>> dataTable){

        List<HashMap<String,String>> result = new ArrayList<>();
        if(dataTable != null)
        {
            int rowCount = dataTable.size();

            if(rowCount > 1)
            {
                List<String> headerRow = dataTable.get(0);

                int columnCount = headerRow.size();

                for(int i=1; i<rowCount; i++)
                {
                    List<String> dataRow = dataTable.get(i);

                    HashMap<String,String> col = new HashMap();

                    for(int j=0;j<columnCount;j++)
                    {
                        String columnName = headerRow.get(j);
                        String columnValue = dataRow.get(j);

                        col.put(columnName, columnValue);
                    }
                       result.add(col);
                }
            }
        }
        return result;
    }

    public void saveToCassandra(List<List<HashMap<String,String>>> jsonData, Long channelId, Long vendorId) {
        Gson gson = new Gson();
        //sheet 단위 채널단위가 되지 않을까?
        jsonData.forEach(jsonObjects -> {
            //record 단위
            jsonObjects.forEach(jsonRaw ->{
                SimpleDateFormat transFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Date orderDate = null;
                try {
                     orderDate = transFormat.parse(jsonRaw.get("order_date"));
                } catch (ParseException e) {
                    e.printStackTrace();
                    orderDate = new Date();
                }
                RawEvent rawEvent = RawEvent.builder()
                        .vendorId(vendorId)
                        .channelId(channelId)
                        .format("JSON")
                        .auto(false)
                        .data(gson.toJson(jsonRaw))
                        .orderId(jsonRaw.get("oder_no"))
                        .publishedAt(orderDate)
                        .createdAt(new Date())
                        .build();
                rawEventService.save(rawEvent);
            });
        });
    }
}
