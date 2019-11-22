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
    @Column(name = "writer_id", nullable = false)
    private Long writerId;

    @Column(name = "board_number")
    private int boardNumber;

    @Column(name = "user_email")
    private String userEmail;

//    @Column(name = "writer")
//    private String writer;

    @Column(name = "title")
    private String title;

    @Column(name = "post")
    private String post;

//    @Column(name = "admin_reply")
//    private String adminReply;
//
//    @Column(name = "user_reply")
//    private String userReply;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_at", nullable = false)
    private Date createdAt;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "updated_at", nullable = false)
    private Date updatedAt;
}
