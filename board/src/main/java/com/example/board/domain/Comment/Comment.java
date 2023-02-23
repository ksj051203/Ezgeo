package com.example.board.domain.Comment;

import com.example.board.domain.Board.BaseTime;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "Comment")
public class Comment extends BaseTime {
    @Column
    private Integer comment_sequence;
    @Id
    @Generated
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer comment_id;
    @Column
    private Integer parent_id;
    @Column
    private Integer depth;
    @Column
    private String comment_writer;
    @Column
    private String comment_content;
    @Column
    private LocalDateTime comment_write_date;
    @Column
    private LocalDateTime comment_modify_date;


}
