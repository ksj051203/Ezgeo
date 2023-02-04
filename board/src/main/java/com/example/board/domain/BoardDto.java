package com.example.board.domain;

import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import javax.persistence.Column;
@RequiredArgsConstructor
public class BoardDto{

    public String getBoard_title() {
        return board_title;
    }

    public String getBoard_content() {
        return board_content;
    }

    public String getBoard_writer() {
        return board_writer;
    }

    public void setBoard_title(String board_title) {
        this.board_title = board_title;
    }

    public void setBoard_content(String board_content) {
        this.board_content = board_content;
    }

    public void setBoard_writer(String board_writer) {
        this.board_writer = board_writer;
    }

    private String board_title;
    private String board_content;
    private String board_writer;

}
