package com.example.notice_board.domain;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.sql.Date;
import java.sql.Timestamp;

@Entity
@Data
public class Board {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer boardId;
    String boardTitle;
    String boardContent;
    String boardWriter;
    Timestamp boardWriteDate;
    Timestamp boardModifyDate;
}
