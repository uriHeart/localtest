package com.argo.api.domain.excel;

import lombok.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ExcelToCassandraDto {

    private List<HashMap<String,String>> raw = new ArrayList<>();

    private Date publishedAt;

    private String orderId;

}
