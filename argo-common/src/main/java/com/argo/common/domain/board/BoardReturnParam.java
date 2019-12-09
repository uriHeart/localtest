package com.argo.common.domain.board;

import com.google.gson.JsonArray;
import lombok.*;
import org.json.simple.JSONArray;

import java.util.List;

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
    private boolean deleted;
    private boolean admin_reply; //DEFAULT false
    private List<MainBoardShorten> rowData;
    private List<String> replies;
    private String reply;

}
