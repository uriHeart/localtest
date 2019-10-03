package com.argo.collect.service;

import com.argo.common.domain.channel.ChannelCollectInfo;
import com.argo.common.domain.channel.ChannelCollectInfoRepository;
import com.argo.common.domain.channel.SalesChannel;
import com.argo.common.domain.channel.SalesChannelService;
import com.argo.common.domain.raw.RawEvent;
import com.argo.common.domain.raw.RawEventService;
import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.elasticsearch.action.admin.indices.alias.IndicesAliasesRequest;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexRequestBuilder;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.support.IndicesOptions;
import org.elasticsearch.action.support.master.AcknowledgedResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.core.CountRequest;
import org.elasticsearch.client.core.CountResponse;
import org.elasticsearch.client.indices.GetMappingsRequest;
import org.elasticsearch.client.indices.GetMappingsResponse;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.rest.RestStatus;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.sort.FieldSortBuilder;
import org.elasticsearch.search.sort.SortOrder;
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
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;

@Service
@Slf4j
public class ConvertService {

    @Value("${file.upload.default-path}")
    private String path;

    @Value("${file.upload.index}")
    private String excelIndex;

    @Autowired
    RawEventService rawEventService;

    @Autowired
    Gson gson;



    private RestHighLevelClient esClient;

    @Autowired
    public ConvertService(RestHighLevelClient client) {
        this.esClient = client;
    }

    public File excelUpload(MultipartFile filePart) throws IOException {
        String sourceFileName = filePart.getOriginalFilename();
        String sourceFileNameExtension = FilenameUtils.getExtension(sourceFileName).toLowerCase();
        String sourceFileBaseName = FilenameUtils.getBaseName(sourceFileName);
        File destinationFile;
        // String destinationFileName;

        //S3에 저장한다면 파일명 변환은 필요 없음
//        do {
//            //destinationFileName = RandomStringUtils.randomAlphanumeric(32) + "." + sourceFileNameExtension;
//            destinationFile = new File(path+"/" + destinationFileName);
//        } while (destinationFile.exists());

        destinationFile = new File(path+"/" + sourceFileName);

        if(destinationFile.exists()){
            String milliSecondLong = String.valueOf(Instant.now().getEpochSecond());
            destinationFile = new File(path+"/" + sourceFileBaseName+"_"+milliSecondLong + "."+sourceFileNameExtension);
        }

        destinationFile.getParentFile().mkdirs();
        filePart.transferTo(destinationFile);

        return destinationFile;
    }

    public List<HashMap<String,Object>> excelToJson(File file) throws IOException {
        List<HashMap<String,Object>> result = new ArrayList<>();

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
                List<List<String>> mainDataTable = getMainDataList(sheetDataTable);

                HashMap<String,Object> convertSheet = new HashMap<>();

                convertSheet.put("sheetHeader",mainDataTable.get(0));
                convertSheet.put("sheetName",sheetName);
                convertSheet.put("sheetData",getJSONStringFromList(mainDataTable));
                result.add(convertSheet);

            }
        }
        excelWorkBook.close();

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

                if(row == null) continue;

                int firstCellNum = row.getFirstCellNum();
                int lastCellNum = row.getLastCellNum();


                List<String> rowDataList = new ArrayList<String>();

                for(int j = firstCellNum; j < lastCellNum; j++)
                {
                    Cell cell = row.getCell(j);

                    if(cell==null) continue;


                    CellType cellType = cell.getCellType();

                    if(cellType == CellType.NUMERIC && DateUtil.isCellDateFormatted(cell)){
                        Date date = cell.getDateCellValue();
                        String dateString = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
                        rowDataList.add(dateString);
                    }else if(cellType == CellType.NUMERIC)
                    {
                        Double doubleValue = cell.getNumericCellValue();

                        String value = doubleValue.toString();
                        if(doubleValue - doubleValue.intValue() == 0){
                            value = String.valueOf(Math.round(doubleValue*100/100.0));
                        }

                        String stringCellValue = value;

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

                    }else if(cellType == CellType.FORMULA)
                    {

                        String stringCellValue ;
                        try{
                            long value = Math.round(cell.getNumericCellValue());
                            stringCellValue = String.valueOf(value);

                        }catch (IllegalStateException e){
                            stringCellValue = cell.getStringCellValue();
                        }

                        rowDataList.add(stringCellValue);

                    }else if(cellType == CellType.BLANK)
                    {
                        if(j==firstCellNum) continue;

                        rowDataList.add("");
                    }
                }

                ret.add(rowDataList);
            }
        }

        return ret;
    }

    //엑셀의 불필요한 데이터 제거
    private List<List<String>> getMainDataList(List<List<String>> sheetDataList){

        int avgCellCount = (int) sheetDataList.stream().mapToInt(List::size).average().orElse(0);


        List<List<String>> temp = new ArrayList<>();
        List<List<String>> mainDataList = new ArrayList<>();

        //row의 cell 데이터가 평균 이상이여야 등록한다. 제목 합계등 만 있는 row는 제외
        //병균 셀데이터의 1/3 보다 많은 공백값이 있는 데이터 제외
        sheetDataList.forEach(row ->{
            if(row.size() >= avgCellCount && blankCount(row) <= avgCellCount/3){
                temp.add(row);
            }
        });

        boolean findHeader = false;

        //셀을 병합하여 공백값만 있는 row의 헤더 이전 row 제외
        for (List<String> row : temp) {
            if(findHeader || isHeader(row) ){
                findHeader = true;
                mainDataList.add(row);
            }
        };

        return mainDataList;
    }

    // 헤더 value : cell 값 의 형태의 데이터를 만든다.
    private List<HashMap<String,String>> getJSONStringFromList(List<List<String>> dataTable){

        List<HashMap<String,String>> result = new ArrayList<>();
        if(dataTable != null)
        {
            int rowCount = dataTable.size();

            if(rowCount > 1)
            {
                List<String> headerRow = dataTable.get(0);

                int columnCount = headerRow.size();

                for(int i=1; i<rowCount; i++) {
                    List<String> dataRow = dataTable.get(i);

                    if (dataRow.size() >= columnCount) {

                        LinkedHashMap<String, String> col = new LinkedHashMap();

                        for (int j = 0; j < columnCount; j++) {
                            String columnName = headerRow.get(j);
                            String columnValue = dataRow.get(j);

                            col.put(columnName, columnValue);
                        }
                        result.add(col);
                    }
                }
            }
        }
        return result;
    }

    //헤더의 모든값은 공백문자가 아니여야 한다.
    private boolean isHeader(List<String> row){

        AtomicBoolean isHeader = new AtomicBoolean(true);

        row.forEach( cell ->{
            if(cell.length()==0){
                isHeader.set(false);
            }
        });

        return isHeader.get();
    }

    //공백문자의수 를 확인한다.
    private long blankCount(List<String> row){
        return row.stream().filter(cell->(cell.length()==0)).count();
    }


    public void saveToCassandra(List<HashMap<String,List<HashMap<String,String>>>> jsonData, Long channelId, Long vendorId) {
        //sheet 단위 채널단위가 되지 않을까?
        jsonData.forEach(sheet -> {
            sheet.values().forEach(jsonObjets ->{
                //record 단위

                jsonObjets.forEach(jsonRaw ->{
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

        });
    }

    public RestStatus saveToEs(String id, List<HashMap<String, Object>> jsonData) throws IOException {

        String dateTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

        IndexRequest request = new IndexRequest(excelIndex);

        HashMap<String, Object> data = new HashMap<>();
        data.put("upLoadDate",dateTime);
        data.put("@timeStamp",Instant.now().getEpochSecond());
        data.put("excelData",jsonData);
        data.put("fileName",id);

        request.source(gson.toJson(data), XContentType.JSON);

        IndexResponse response = esClient.index(request, RequestOptions.DEFAULT);
        CountRequest countRequest = new CountRequest(excelIndex);
        CountResponse countResponse =  esClient.count(countRequest, RequestOptions.DEFAULT);
        log.info(String.valueOf(countResponse.getCount()));
        return response.status();
    }

    public List<Map<String,Object>> getExcelList(String userId) throws IOException {

        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();

        QueryBuilder matchAll = QueryBuilders.matchAllQuery();

        //TODO 사용자 ID에 해당하는 데이터 조회
        searchSourceBuilder.query(matchAll);

        searchSourceBuilder.size(1000);
        searchSourceBuilder.fetchSource(new String[]{"upLoadDate", "fileName"},new String[]{""});
        searchSourceBuilder.sort(new FieldSortBuilder("@timeStamp").order(SortOrder.DESC));

        SearchRequest searchRequest = new SearchRequest();
        searchRequest.indices(excelIndex);
        searchRequest.source(searchSourceBuilder);

        SearchResponse response = esClient.search(searchRequest, RequestOptions.DEFAULT );
        SearchHits excelList =  response.getHits();

        List<Map<String,Object>> result = new ArrayList<>();

        excelList.forEach(
                excel ->{
                    Map<String, Object> excelInfo = excel.getSourceAsMap();
                    excelInfo.put("id",excel.getId());
                    result.add(excelInfo);
                }
        );

        return result;
    }

    public ArrayList<HashMap<String,HashMap<String,Object>>> getExcelMainText(String indexId) throws IOException {
        GetRequest getRequest = new GetRequest(excelIndex,indexId);
        GetResponse getResponse = esClient.get(getRequest, RequestOptions.DEFAULT);
        ArrayList<HashMap<String,HashMap<String,Object>>> data = (ArrayList<HashMap<String, HashMap<String, Object>>>) getResponse.getSourceAsMap().get("excelData");

        return data;
    }

}