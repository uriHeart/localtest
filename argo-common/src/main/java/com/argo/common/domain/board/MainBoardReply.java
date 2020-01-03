package com.argo.common.domain.board;

import lombok.Data;

import java.util.Date;

@Data
public class MainBoardReply {
    private Long boardId; //;
    private String userEmail; //
    private String post;
    private Date createdAt;
}
