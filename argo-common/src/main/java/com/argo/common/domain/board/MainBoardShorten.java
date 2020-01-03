package com.argo.common.domain.board;

import lombok.Builder;
import lombok.Data;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
@Builder
public class MainBoardShorten {
    private Long boardId; //;
    private String userEmail; //
    private String post;
    private String title; //
    private Date createdAt; //
    private Date updatedAt;

    public static MainBoardShorten from(MainBoard data) {
        return MainBoardShorten.builder()
                .boardId(data.getBoardId())
                .userEmail(data.getUserEmail())
                .title(data.getTitle())
                .createdAt(data.getCreatedAt())
                .build();
    }
}
