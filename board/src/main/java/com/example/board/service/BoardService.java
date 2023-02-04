package com.example.board.service;


import com.example.board.domain.Board;
import com.example.board.domain.BoardDto;
import com.example.board.repository.BoardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.data.web.SpringDataWebProperties;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.CriteriaBuilder;
import java.util.List;
import java.util.Optional;

@Service
public class BoardService {
    @Autowired
    public BoardRepository boardRepository;

    public BoardService(BoardRepository boardRepository) {
        this.boardRepository = boardRepository;
    }

    public void createFinish(Board board){
        boardRepository.save(board);
    };
    public Board findBoard(Integer board_id){
        return boardRepository.findById(board_id).get();
    }

    public void update(Integer board_id, BoardDto boardDto){
        Board update = boardRepository.findById(board_id).orElse(null);
        update.setBoard_title(boardDto.getBoard_title());
        update.setBoard_writer(boardDto.getBoard_writer());
        update.setBoard_content(boardDto.getBoard_content());
        boardRepository.save(update);
    }
    public List<Board> findAllBoard(){
        return boardRepository.findAll();
    }

    public void deleteBoard(Integer board_id){
        boardRepository.deleteById(board_id);
    }

}
