package com.argo.api.service;


import com.argo.api.domain.excel.ChannelExcelMapping;
import com.argo.api.domain.excel.ChannelExcelMappingService;
import com.argo.api.domain.excel.ExcelToCassandraDto;
import com.argo.common.configuration.ArgoBizException;
import com.argo.common.domain.raw.RawEvent;
import com.argo.common.domain.raw.RawEventService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.elasticsearch.action.ActionListener;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.core.CountRequest;
import org.elasticsearch.client.core.CountResponse;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.rest.RestStatus;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.FetchSourceContext;
import org.elasticsearch.search.sort.FieldSortBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import reactor.core.publisher.Mono;

import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Timestamp;
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

    @Autowired
    HttpSession httpSession;

    @Autowired
    ObjectMapper objectMapper;

    private RestHighLevelClient esClient;

    @Autowired
    ChannelExcelMappingService channelExcelMappingService;

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

//        if(destinationFile.exists()){
//            String milliSecondLong = String.valueOf(Instant.now().getEpochSecond());
//            destinationFile = new File(path+"/" + sourceFileBaseName+"_"+milliSecondLong + "."+sourceFileNameExtension);
//        }

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
        List<List<String>> ret = new ArrayList<>();

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

                    if(cell==null){
                        rowDataList.add("");
                        continue;
                    }

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

                        }catch (Exception e){
                            stringCellValue = cell.getCellFormula();
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
        }

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

                    if(columnCount> dataRow.size()){
                        int addCount = columnCount-dataRow.size();
                        for(int addCol = 0; addCol < addCount; addCol++){
                            dataRow.add("");
                        }
                    }


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


    public void saveToCassandra(ArrayList<HashMap<String,Object>> jsonData, Long channelId, Long vendorId) {
        SimpleDateFormat transFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        HashMap<String, ExcelToCassandraDto> cassandraJsonData = new HashMap<>();

        jsonData.forEach(jsonRaw ->{
            String key = jsonRaw.get("orderId").toString();
            if(cassandraJsonData.containsKey(key)){
                cassandraJsonData.get(key).getRaw().add(jsonRaw);
            }else {
                ExcelToCassandraDto excelRow = new ExcelToCassandraDto();
                String publishedAtString = jsonRaw.get("publishedAt").toString();

                Date publishedAt = null;

                if(publishedAtString.split(" ").length==1){
                    publishedAtString += " 00:00:00";
                }

                try {
                    publishedAt = transFormat.parse(publishedAtString);
                } catch (ParseException e) {
                    throw new ArgoBizException("published date 형식이 일치하지 않습니다.");
                }

                excelRow.setPublishedAt(publishedAt);
                excelRow.getRaw().add(jsonRaw);
                cassandraJsonData.put(key,excelRow);
            }

            jsonRaw.remove("orderId");
            jsonRaw.remove("publishedAt");
        });

        cassandraJsonData.forEach((order,excelToCassandraDto) ->{
            HashMap<String,List> datas = new HashMap<>();
            datas.put("datas",excelToCassandraDto.getRaw());

            RawEvent rawEvent
                    = RawEvent.builder()
                    .vendorId(vendorId)
                    .channelId(channelId)
                    .orderId(order)
                    .publishedAt(excelToCassandraDto.getPublishedAt())
                    .format("JSON")
                    .auto(false)
                    .data(gson.toJson(datas))
                    .event(null)
                    .createdAt(new Date())
                    .build();
            rawEventService.save(rawEvent);

        });
    }

    public RestStatus saveToEs(String id, List<HashMap<String, Object>> jsonData,Long vendorId,Long channelId) throws IOException {

        String dateTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

        IndexRequest request = new IndexRequest(excelIndex);

        HashMap<String, Object> data = new HashMap<>();
        data.put("upLoadDate", dateTime);
        data.put("@timeStamp", Instant.now().getEpochSecond());
        data.put("excelData", jsonData);
        data.put("fileName", id);
        data.put("vendorId", vendorId);
        data.put("channelId", channelId);


        request.source(gson.toJson(data), XContentType.JSON).id(id);

        IndexResponse response = esClient.index(request, RequestOptions.DEFAULT);
        CountRequest countRequest = new CountRequest(excelIndex);
        CountResponse countResponse = esClient.count(countRequest, RequestOptions.DEFAULT);
        log.info(String.valueOf(countResponse.getCount()));
        return response.status();
    }

//    public RestStatus saveToEs(String id, List<HashMap<String, Object>> jsonData,Long vendorId,Long channelId) throws IOException {
//
//        String dateTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
//
//        IndexRequest request = new IndexRequest(excelIndex);
//
//        HashMap<String, Object> data = new HashMap<>();
//        data.put("upLoadDate",dateTime);
//        data.put("@timeStamp",Instant.now().getEpochSecond());
//        data.put("fileName",id);
//
//        List<HashMap<String,Object>> rowDatakeyList = new ArrayList<>();
//        HashMap<String,Boolean> dupChk = new HashMap<>();
//
//        jsonData.forEach(sheetData ->{
//            List<HashMap<String,String>> rowData = (List<HashMap<String, String>>) sheetData.get("sheetData");
//            rowData.forEach(row-> {
//                HashMap<String,Object> rowDatakey = new HashMap<>();
//                rowDatakey.put("vendorId",vendorId);
//                rowDatakey.put("channelId",channelId);
//
//                row.forEach( (key,value)->{
//                    if("orderId".equals(key)){
//                        rowDatakey.put("orderId",value);
//                    }else if("publishedAt".equals(key)){
//                        rowDatakey.put("publishedAt",value);
//                    }
//                });
//
//                if(!dupChk.containsKey(rowDatakey.get("orderId"))){
//                    dupChk.put(rowDatakey.get("orderId").toString(),true);
//                    rowDatakeyList.add(rowDatakey);
//                }
//            });
//        });
//
//        //key
//        data.put("rowDatakeyList",rowDatakeyList);
//        data.put("sheetHeader",jsonData.get(0).get("sheetHeader"));
//        data.put("sheetName",id.split(".")[0]);
//
//        request.source(gson.toJson(data), XContentType.JSON).id(id);
//
//        IndexResponse response = esClient.index(request, RequestOptions.DEFAULT);
//        CountRequest countRequest = new CountRequest(excelIndex);
//        CountResponse countResponse =  esClient.count(countRequest, RequestOptions.DEFAULT);
//        log.info(String.valueOf(countResponse.getCount()));
//        return response.status();
//    }

    public List<Map<String,Object>> getExcelList(String userId) throws IOException {

        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();

        QueryBuilder matchAll = QueryBuilders.matchAllQuery();

        //TODO 사용자 ID에 해당하는 데이터 조회
        searchSourceBuilder.query(matchAll);

        searchSourceBuilder.size(1000);
        searchSourceBuilder.fetchSource(new String[]{"upLoadDate", "fileName","channelId","vendorId"},new String[]{""});
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
                    excelInfo.put("channelId",excelInfo.get("channelId").toString());
                    excelInfo.put("vendorId",excelInfo.get("vendorId").toString());
                    result.add(excelInfo);
                }
        );

        return result;
    }

    public boolean existsExcel(String id) throws IOException {
        GetRequest getRequest = new GetRequest(
                excelIndex,
                id);
        getRequest.fetchSourceContext(new FetchSourceContext(false));
        getRequest.storedFields("_none_");
        boolean exists = esClient.exists(getRequest, RequestOptions.DEFAULT);

        return exists;
    }

    public ArrayList<HashMap<String,HashMap<String,Object>>> getExcelMainText(String indexId) throws IOException {
        GetRequest getRequest = new GetRequest(excelIndex,indexId);
        GetResponse getResponse = esClient.get(getRequest, RequestOptions.DEFAULT);
        ArrayList<HashMap<String,HashMap<String,Object>>> data = (ArrayList<HashMap<String, HashMap<String, Object>>>) getResponse.getSourceAsMap().get("excelData");


        return data;
    }


//    public Map getExcelMainText(String indexId) throws IOException {
//        GetRequest getRequest = new GetRequest(excelIndex,indexId);
//        GetResponse getResponse = esClient.get(getRequest, RequestOptions.DEFAULT);
//        Map data =  getResponse.getSourceAsMap();
//        List<HashMap> rowDatakeyList = (List<HashMap>) data.get("rowDatakeyList");
//        List<HashMap> rowDataList = new ArrayList<>();
//        rowDatakeyList.forEach(row ->{
//
//            Long vandorId = Long.parseLong(row.get("vendorId").toString());
//            Long channelId = Long.parseLong(row.get("channelId").toString());
//            String orderId = row.get("orderId").toString();
//            Date publishedAt = ArgoDateUtil.getDateBy24H(row.get("publishedAt").toString());
//
//            RawEvent rawEvent = rawEventService.getRawEvent(vandorId,channelId,orderId,publishedAt);
//
//            String rawData = null;
//
//            if(rawEvent != null){
//                rawData = rawEvent.getData();
//            }
//            Map raw = null;
//            try {
//                raw = objectMapper.readValue(rawData, Map.class);
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//
//            rowDataList.addAll((Collection<? extends HashMap>) raw.get("datas"));
//        });
//
//        Map result = new HashMap();
//        result.put("sheetData",rowDataList);
//        result.put("sheetHeader",data.get("sheetHeader"));
//        result.put("sheetName",data.get("sheetName"));
//
//        return result;
//    }


    public ArrayList<HashMap<String, Object>> addExcelFactor(ArrayList<HashMap<String, Object>> excelData,Long ChannelId) throws ArgoBizException{
        List<ChannelExcelMapping> channelExcelMappings = channelExcelMappingService.getChannelExcelMapping(ChannelId);

        excelData.forEach(row -> {
            channelExcelMappings.forEach(factor -> {
                StringBuilder factorValue = new StringBuilder();
                factor.getExcelFactorInfo().forEach(excelFactorInfo -> {
                    //factor를 만들 정보가 없는 엑셀은 UUID 를 사용한다.
                    if (excelFactorInfo.getColumnNo() == 0) {
                        factorValue.append(UUID.randomUUID().toString());
                    } else {
                        factorValue.append(row.get(excelFactorInfo.getColumnName()));
                    }
                });
                row.put(factor.getFactorId(), factorValue.toString());
            });
        });

        return excelData;
    }


    public List<HashMap<String, String>> addExcelFactorCheck(List<HashMap<String, Object>> convertExcelData,Long ChannelId) throws ArgoBizException{
        List<HashMap<String,String>> sheetData = (List<HashMap<String, String>>) convertExcelData.get(0).get("sheetData");
        List<ChannelExcelMapping> channelExcelMappings = channelExcelMappingService.getChannelExcelMapping(ChannelId);

        if(channelExcelMappings.size()==0){
            throw new ArgoBizException("채널 맵핑 정보가 없습니다.");
        }

        SimpleDateFormat transFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        HashMap<String,String> checkMap = new HashMap<>();

        sheetData.forEach(row -> {
            channelExcelMappings.forEach(factor -> {
                StringBuilder factorValue = new StringBuilder();
                factor.getExcelFactorInfo().forEach(excelFactorInfo -> {
                    factorValue.append(row.get(excelFactorInfo.getColumnName()));
                });
                checkMap.put(factor.getFactorId(), factorValue.toString());
            });

            //엑셀에 추가된 rawdata key 속성 검증
            String publishedAtString = checkMap.get("publishedAt");

            Date publishedAt = null;
            if(publishedAtString.split(" ").length==1){
                publishedAtString += " 00:00:00";
            }
            try {
                publishedAt = transFormat.parse(publishedAtString);

            } catch (Exception e) {
                throw new ArgoBizException("엑셀 형식이 일치하지 않습니다. 업로드 채널정보를 확인해주세요.");
            }
        });

        return sheetData;
    }


    public Mono<RestStatus> deleteIndex(String docId) {

        DeleteRequest request = new DeleteRequest(excelIndex,docId);

        return Mono.create(sink -> {
            esClient.deleteAsync(request, RequestOptions.DEFAULT, new ActionListener<DeleteResponse>() {
                @Override
                public void onResponse(DeleteResponse deleteIndexResponse) {
                    sink.success(deleteIndexResponse.status());
                }

                @Override
                public void onFailure(Exception e) {
                    log.error("index delete error: {}",e);
                    sink.error(e);
                }
            });

        });
    }

    public ArrayList<HashMap<String, Object>> getEventList(String docId) throws IOException {
        GetRequest request = new GetRequest(excelIndex,docId);

        GetResponse getResponse = esClient.get(request, RequestOptions.DEFAULT);
        ArrayList<HashMap<String,ArrayList<HashMap<String,Object>>>> data = (ArrayList<HashMap<String, ArrayList<HashMap<String, Object>>>>) getResponse.getSourceAsMap().get("excelData");
        ArrayList<HashMap<String, Object>> result =  data.get(0).get("sheetData");

        return result;
    }
}