package com.argo.api.controller.board;

import lombok.*;
import org.json.simple.JSONArray;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class BoardReturnParam {
    private boolean success;
    private Long boardId; //;
    private String user_email; //
    private String title; //
    private String post; //
    private String message;  //NEED TO ADD EXCEPTION MESSAGE
    private Long parent; //DEFAULT NULL
    private boolean admin_reply; //DEFAULT false
    private JSONArray rowData;
}
