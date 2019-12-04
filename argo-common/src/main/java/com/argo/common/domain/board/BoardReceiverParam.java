package com.argo.common.domain.board;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
/** we will be erasing unnecesssary paramters : Replied / Deleted / created_at / updated_at (will be updated in different section" **/
public class BoardReceiverParam {
    private Long boardId;
    private String user_email; //NEED
    private String title; //NEED
    private String post; //NEED
    private Long parent; //DEFAULT NULL FOR REPLIES
    private boolean admin_reply; //DEFAULT false // FOR REPLIES
}
