package com.example.board.domain.Comment;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import net.bytebuddy.asm.Advice;

import java.time.LocalDateTime;

@Getter
@Setter
@RequiredArgsConstructor
public class CommentDto {
    private int comment_id;
    private int comment_sequence;
    private String comment_writer;
    private String comment_content;
    private String comment_password;
    private LocalDateTime comment_write_date;
    private LocalDateTime comment_modify_date;

}
