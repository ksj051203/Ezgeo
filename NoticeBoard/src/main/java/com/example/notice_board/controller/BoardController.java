package com.example.notice_board.controller;

import com.example.notice_board.domain.Board;
import com.example.notice_board.service.BoardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class BoardController {
    @Autowired
    public final BoardService boardService;

    public BoardController(BoardService boardService) {
        this.boardService = boardService;
    }

    @GetMapping("/create")
    public String createBoard(Model model, Board board){
        return "createForm";
    };

    @PostMapping("/create/finish")
    public void createFinish(Model model, Board board){
        boardService.createBoard(board);
    }

    @GetMapping("/list/{boardId}")
    public String findBoard(@PathVariable Integer boardId, Model model){
        model.addAttribute("board", boardService.findBoard(boardId));
        return "list";
    };

    @GetMapping("/list")
    public String findAllBoard(Model model, Board board){
        model.addAttribute("board", boardService.findAllBoard(board));
        return "view";
    }

    @DeleteMapping("/delete/{boardId}")
    public void deleteBoard(@PathVariable Integer boardId){
        boardService.deleteBoard(boardId);
    };

    @DeleteMapping("/delete")
    public void deleteAllBoard(){
        boardService.deleteAllBoard();
    };
}
