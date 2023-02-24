package com.example.board.domain.Board;

import javax.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name="board")
@EntityListeners(AuditingEntityListener.class)
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

}
