package com.argo.common.domain.board;

import lombok.Builder;
import lombok.Data;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
public class MainBoardShorten {
    public Long boardId; //;
    public String user_email; //
    public String post;
    public String title; //
    public Date createdAt; //
    public Date updatedAt;
}
