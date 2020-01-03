package com.argo.common.domain.board;

import lombok.Builder;
import lombok.Data;

import java.util.Date;

@Data
@Builder
public class MainBoardReply {
    private Long boardId;
    private String userEmail;
    private String post;
    private Date createdAt;

    public static MainBoardReply from(MainBoard data) {
        return MainBoardReply.builder()
                .boardId(data.getBoardId())
                .userEmail(data.getUserEmail())
                .post(data.getPost())
                .createdAt(data.getCreatedAt())
                .build();
    }
}
