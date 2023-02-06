package com.example.board.domain;

import javax.persistence.*;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Data
@Getter
@Table(name="board")
public class Board extends BaseTime{
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
