package com.example.board.controller;

import ch.qos.logback.core.net.SyslogOutputStream;
import com.example.board.domain.Board;
import com.example.board.domain.BoardDto;
import com.example.board.service.BoardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityManager;
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
        return "redirect:/list";
    }

    @GetMapping("/list")
    public String findAllBoard(Model model){
        List<Board> board = boardService.findAllBoard();
        model.addAttribute("board", board);
        return "list";
    }

    @GetMapping("/list_new")
    public String getList(@RequestParam("nowPage") Integer nowPage, Model model){
        model.addAttribute("board", boardService.makePaging(nowPage));
        return "list";
    }

    @GetMapping("/list/{board_id}")
    public String findBoard(@PathVariable("board_id") Integer board_id, Model model) throws Exception{
        model.addAttribute("board", boardService.findBoard(board_id));
        return "view";
    };

    @GetMapping ("/delete")
    public String deleteBoard(Integer board_id){
        boardService.deleteBoard(board_id);
        return "redirect:/list";
    };

    @GetMapping("/modify/{board_id}")
    public String modifyBoard(@PathVariable("board_id") Integer board_id, Model model){
        Board board = boardService.findBoard(board_id);
        model.addAttribute("board", board);
        return "modify";
    }

    @PostMapping("/update/{board_id}")
    public String updateBoard(@PathVariable("board_id") Integer board_id, BoardDto boardDto){
        boardService.update(board_id, boardDto);
        return "redirect:/list";
    };
}
