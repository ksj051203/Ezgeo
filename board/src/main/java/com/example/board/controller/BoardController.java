package com.example.board.controller;
import com.example.board.domain.Board;
import com.example.board.domain.BoardDto;
import com.example.board.service.BoardService;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.persistence.criteria.CriteriaBuilder;

@Controller
@RequestMapping
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



    @RequestMapping(value="/list", method =  {RequestMethod.GET, RequestMethod.POST})
    public String pagingBoard(
            @RequestParam(value = "nowPage", defaultValue = "1") Integer nowPage,
            @RequestParam(value="searchKeyword", required = false) String searchKeyword,
            String searchType,
            Model model
            ){
        model.addAttribute("board", boardService.pagingBoard(nowPage, searchKeyword, searchType));
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
