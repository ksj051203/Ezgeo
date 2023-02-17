package com.example.board.domain.Comment;

import lombok.Data;
import lombok.Generated;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Data
@Table(name = "comment")
public class Comment extends BaseTime2 {
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
