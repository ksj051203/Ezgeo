package com.example.board.controller;

import com.example.board.domain.Board;
import com.example.board.service.BoardService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.persistence.criteria.CriteriaBuilder;
import java.awt.print.Pageable;
import java.util.List;


@Controller
public class BoardController {
    public final BoardService boardService;

    public BoardController(BoardService boardService) {
        this.boardService = boardService;
    }

    @GetMapping("/create")
    public String createBoard(Model model, Board board){
        model.addAttribute("board", board);
        return "create";
    };

    @PostMapping("/createSuccess")
    public String createSuccess(Board board){
        boardService.createFinish(board);
        return "createSuccess";
    }

    @GetMapping("/list")
    public String findAllBoard(Model model){
        model.addAttribute("board", boardService.findAllBoard());
        return "list";
    }

    @GetMapping("/list/{board_id}")
    public String findBoard(@PathVariable("board_id") Integer board_id, Model model) throws Exception{
        model.addAttribute("board", boardService.findBoard(board_id));
        return "view";
    };

    @GetMapping ("/delete")
    public void deleteBoard(Integer board_id){
        boardService.deleteBoard(board_id);
    };

}
