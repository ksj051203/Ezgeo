package com.example.board.controller;
import com.example.board.domain.Board.Board;
import com.example.board.domain.Board.BoardDto;
import com.example.board.domain.Comment.CommentDto;
import com.example.board.service.BoardService;
import com.example.board.service.CommentService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping
@AllArgsConstructor
public class BoardController {
    @Autowired
    public final BoardService boardService;

    @Autowired
    public final CommentService commentService;

    //게시글 생성하기
    @GetMapping("/create")
    public String createBoard(Model model, Board board){
        model.addAttribute("board", board);
        return "create";
    };

    // 게시글 생성 완료
    @PostMapping("/createSuccess")
    public String createSuccess(Board board){
        boardService.createFinish(board);
        return "redirect:/list";
    }

    // 게시글 리스트
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

    // 게시글 상세보기
    @RequestMapping(value="/list/{board_id}", method={RequestMethod.GET})
    public String findBoard(@PathVariable("board_id") Integer board_id, Map<String, Object> reqMap, Model model) throws  Exception{
        reqMap.put("board_id", board_id);
        model.addAttribute("board", boardService.findBoard(board_id));
        model.addAttribute("comment", commentService.findComment(reqMap));
        return "view";
    };

    // 게시글 수정하기
    @GetMapping("/modify/{board_id}")
    public String modifyBoard(@PathVariable("board_id") Integer board_id, Model model){
        Board board = boardService.findBoard(board_id);
        model.addAttribute("board", board);
        return "modify";
    }

    // 게시글 수정 적용하기
    @PostMapping("/update/{board_id}")
    public String updateBoard(@PathVariable("board_id") Integer board_id, BoardDto boardDto){
        boardService.update(board_id, boardDto);
        return "redirect:/list";
    };

    // 댓글 생성하기(form)
    @RequestMapping(value="/list/addComment/{board_id}", method={RequestMethod.POST}, consumes = "application/x-www-form-urlencoded")
    public String addComment(@RequestParam Map<String, Object> reqMap){
        commentService.insertComment(reqMap);
        int board_id = Integer.parseInt(reqMap.get("board_id").toString());
        return "redirect:/list/"+board_id;
    };

    // 답글 생성하기(axios)
    @RequestMapping(value="/list/addComment/axios/{board_id}", method={RequestMethod.POST}, consumes="application/json")
    @ResponseBody
    public Map<String, Object> addAxiosComment(@RequestBody Map<String, Object> reqMap){
        System.out.println("reqMap : " + reqMap.toString());
        Map<String, Object> rtnMap = new HashMap<String, Object>();
        rtnMap.put("add", commentService.insertComment(reqMap));
        return rtnMap;
    };

    // 수정 시, 수정 전 값들 기억하기
    @PostMapping("/modify/remember")
    @ResponseBody
    public Map<String, Object> modifyRemember(@RequestBody Map<String, Object> reqMap){
        Map<String, Object> rtnMap = new HashMap<String, Object>();
        rtnMap.put("modify", commentService.modifyRemember(reqMap));
        return rtnMap;
    }

    // 원하는 댓글 수정
    @PostMapping("/modify/comment/{board_id}")
    @ResponseBody
    public Map<String, Object> modifyComment(@RequestBody CommentDto commentDto){
        Map<String, Object> rtnMap = new HashMap<String, Object>();
        rtnMap.put("modify",commentService.modifyComment(commentDto));
        return rtnMap;
    }


    // 게시글(+댓글) 삭제하기
    @GetMapping("/delete")
    public String deleteBoard(Integer board_id){
        boardService.deleteBoard(board_id);
        return "redirect:/list";
    };

    // 게시글(+댓글) 삭제하기(checkbox)
    @PostMapping("/delete/axios")
    @ResponseBody
    public Map<String, Object> deleteAxiosBoard(@RequestBody Map<String, Object> reqMap){
        Map<String, Object> rtnMap = new HashMap<String, Object>();
        rtnMap.put("deleteAxios", commentService.deleteAxios(reqMap));
        return rtnMap;
    }

    // 특정 댓글 삭제하기(비밀번호 일치)
    @PostMapping("/delete/password")
    @ResponseBody
    public Map<String, Object> deletePassword(@RequestBody Map<String, Object> reqMap){
        Map<String, Object> rtnMap = new HashMap<String, Object>();
        rtnMap.put("delete", commentService.deletePassword(reqMap));
        return rtnMap;
    }
}
