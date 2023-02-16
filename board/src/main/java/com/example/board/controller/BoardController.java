package com.example.board.controller;
import com.example.board.domain.Board.Board;
import com.example.board.domain.Board.BoardDto;
import com.example.board.domain.Comment.Comment;
import com.example.board.service.BoardService;
import com.example.board.service.CommentService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Controller
@RequestMapping
@AllArgsConstructor
public class BoardController {
    @Autowired
    public final BoardService boardService;

    @Autowired
    public final CommentService commentService;

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


    @GetMapping(value="/list")
    public String pagingBoard( @RequestParam Map<String, Object> reqMap, Model model){

        Integer nowPage = 1;
        if(!ObjectUtils.isEmpty(reqMap.get("nowPage"))) nowPage = Integer.valueOf(reqMap.get("nowPage").toString());

        String searchKeyword = "";
        if(!ObjectUtils.isEmpty(reqMap.get("searchKeyword"))) searchKeyword = reqMap.get("searchKeyword").toString();

        String searchType = "";
        if(!ObjectUtils.isEmpty(reqMap.get("searchType"))) searchType = reqMap.get("searchType").toString();


        reqMap.put("nowPage", nowPage);
        reqMap.put("searchType", searchType);
        reqMap.put("searchKeyword", searchKeyword);

        model.addAttribute("board", boardService.pagingBoard(reqMap));
        model.addAttribute("nowPage", nowPage);

        return "list";
    }

    @RequestMapping(value="/list/{board_id}", method={RequestMethod.GET})
    public String findBoard(@PathVariable("board_id") Integer board_id, Map<String, Object> reqMap, Model model, Comment comment) throws  Exception{
        reqMap.put("board_id", board_id);
        model.addAttribute("board", boardService.findBoard(board_id));
        model.addAttribute("comment", commentService.findComment(reqMap));
        return "view";
    };

    @RequestMapping(value="/list/addComment/{board_id}", method={RequestMethod.POST })
    public String addComment(@PathVariable("board_id") Integer board_id, Model model, Map<String, Object> reqMap, Comment comment) throws  Exception{
        reqMap.put("board_id", board_id);
        model.addAttribute("board", boardService.findBoard(board_id));
        commentService.commentFinish(board_id, comment);
        model.addAttribute("comment", commentService.findComment(reqMap));
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
