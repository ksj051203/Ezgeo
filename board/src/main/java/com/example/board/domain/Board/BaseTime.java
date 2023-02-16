package com.example.board.domain.Board;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import java.time.LocalDateTime;


@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public class BaseTime {
    @CreatedDate
    private LocalDateTime board_write_date;
    @LastModifiedDate
    private LocalDateTime board_modify_date;

    public LocalDateTime getBoard_write_date() {
        return board_write_date;
    }

    public void setBoard_write_date(LocalDateTime board_write_date) {
        this.board_write_date = board_write_date;
    }

    public LocalDateTime getBoard_modify_date() {
        return board_modify_date;
    }

    public void setBoard_modify_date(LocalDateTime board_modify_date) {
        this.board_modify_date = board_modify_date;
    }

}
