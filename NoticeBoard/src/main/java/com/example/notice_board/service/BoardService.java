package com.example.notice_board.service;

import com.example.notice_board.domain.Board;
import com.example.notice_board.repository.BoardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BoardService {
    @Autowired
    public final BoardRepository boardRepository;

    public BoardService(BoardRepository boardRepository) {
        this.boardRepository = boardRepository;
    }

    public void createBoard(Board board){
        boardRepository.save(board);
    }

    public Board findBoard(Integer boardId){
        Board board = boardRepository.findById(boardId).get();
        return board;
    }

    public List<Board> findAllBoard(Board board){
        return boardRepository.findAll(board);
    }

    public void deleteBoard(Integer boardId){
        boardRepository.deleteById(boardId);
    }

    public void deleteAllBoard(){
        boardRepository.deleteAll();
    }
}
