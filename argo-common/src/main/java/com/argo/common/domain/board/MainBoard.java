package com.argo.common.domain.board;
import com.argo.common.domain.common.jpa.CreatedAtListener;
import com.argo.common.domain.common.jpa.SystemMetadata;
import com.argo.common.domain.common.jpa.UpdatedAtListener;
import lombok.*;

import javax.persistence.*;
import java.util.Date;

@Data
@Builder
@Entity
@Table(name = "main_board", schema = "public")
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor
@ToString
@EntityListeners( { CreatedAtListener.class, UpdatedAtListener.class } )
public class MainBoard implements SystemMetadata {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator="main_board_seq")
    @SequenceGenerator(name="main_board_seq", sequenceName="main_board_seq", allocationSize=1)
    @Column(name = "board_id", nullable = false)
    private Long boardId;

    @Column(name = "user_email")
    private String userEmail;

//    @Column(name = "writer")
//    private String writer;

    @Column(name = "title")
    private String title;

    @Column(name = "post")
    private String post;

    @Column(name = "parent")
    private Long parent;

    @Column(name = "admin_reply")
    private boolean adminReply;

//    @Column(name = "user_reply")
//    private String userReply;

    @Column(name = "replied")
    private boolean replied;

    /** need to declare the column as deleted or else it doesn't detect the thing**/
    @Column(name = "deleted")
    private boolean deleted;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_at", nullable = false)
    private Date createdAt;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "updated_at", nullable = false)
    private Date updatedAt;

    public void deleteBoard(Long boardId) {
        this.deleted = true;
    }

    public void replied(Long boardId) {
        this.replied = true;
    }

    // need implementation along with the replyboard
    public void newQuestion(Long boardId) {}
}
