package com.example.board.domain;

import javax.persistence.*;

import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;

@Entity
@Table(name="board")
public class Board {
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
    private Timestamp board_write_date;
    @Column
    private Timestamp board_modify_date;

    public Integer getBoard_id() {
        return board_id;
    }

    public void setBoard_id(Integer board_id) {
        this.board_id = board_id;
    }

    public String getBoard_title() {
        return board_title;
    }

    public void setBoard_title(String board_title) {
        this.board_title = board_title;
    }

    public String getBoard_content() {
        return board_content;
    }

    public void setBoard_content(String board_content) {
        this.board_content = board_content;
    }

    public String getBoard_writer() {
        return board_writer;
    }

    public void setBoard_writer(String board_writer) {
        this.board_writer = board_writer;
    }

    public Timestamp getBoard_write_date() {
        return board_write_date;
    }

    public void setBoard_write_date(Timestamp board_write_date) {
        this.board_write_date = board_write_date;
    }

    public Timestamp getBoard_modify_date() {
        return board_modify_date;
    }

    public void setBoard_modify_date(Timestamp board_modify_date) {
        this.board_modify_date = board_modify_date;
    }
}
