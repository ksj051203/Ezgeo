package com.example.board.domain;

import javax.persistence.*;

import com.example.board.domain.BaseTime;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Transactional
@Entity
@Data
@Getter
@Table(name="board")
public class Board extends BaseTime {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer board_id;
    @Column
    private String board_title;
    @Column
    private String board_content;
    @Column
    private String board_writer;
    @Column
    private LocalDateTime board_write_date;
    @Column
    private LocalDateTime board_modify_date;




}
