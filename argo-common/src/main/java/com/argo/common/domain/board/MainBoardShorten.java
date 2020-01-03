package com.argo.common.domain.board;

import lombok.Builder;
import lombok.Data;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
public class MainBoardShorten {
    private Long boardId; //;
    private String userEmail; //
    private String post;
    private String title; //
    private Date createdAt; //
    private Date updatedAt;
}
