package com.example.board.domain.Board;

import lombok.*;
import java.time.LocalDateTime;

@Getter
@Setter
@RequiredArgsConstructor
public class BoardDto{
    private String board_title;
    private String board_content;
    private String board_writer;
    private LocalDateTime board_write_date;
    private LocalDateTime board_modify_date;

}
