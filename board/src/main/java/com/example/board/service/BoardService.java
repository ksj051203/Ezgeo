package com.example.board.service;


import com.example.board.domain.Board;
import com.example.board.repository.BoardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.awt.print.Pageable;
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
    public Board findBoard(Integer boardId){
        return boardRepository.findById(boardId).get();
    }

    public List<Board> findAllBoard(){
        return boardRepository.findAll();
    }

    public void deleteBoard(Integer board_id){
        boardRepository.deleteById(board_id);
    }

}
