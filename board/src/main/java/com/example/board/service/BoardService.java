package com.example.board.service;


import com.example.board.domain.Board;
import com.example.board.domain.BoardDto;
import com.example.board.repository.BoardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    public Map<String, Object> makePaging(Board board, EntityManager em){
        Map<String, Object> rtnMap = new HashMap<String, Object>();
            List<Board> getList = em.createQuery("select m from Board as m order by m.board_id", Board.class)
                    .setFirstResult(0)
                    .setMaxResults(10)
                    .getResultList();
//        // 게시판 레코드 10개 가져오는 로직
        
//            Pageable makePaging = PageRequest.of(0,5);
//            Page<Board> page = boardRepository.findAll(makePaging);
////        // 하단 페이지네이션 만들어주는 로직
////        makePaging;
//        int tabSize = 5;  //한 페이지 당 보일 개수
//        int element = "select count(1) as cnt from Board"; // 전체 데이터 수 : 100
//
//        int totalPage = 0; // 전체 페이지
//        if(element%10 == 0){
//            totalPage = element/10;
//        }
//        else{
//            totalPage = (element/10)+1; //list.getTotalPages(); 총 페이지 수 : 10개
//        }
//
//        int nowPage = board.getPageable().getPageNumber() + 1; // 현재 페이지 0부터 시작
//        int endPage = nowPage * 10; // 끝 페이지
//        int startPage = endPage - 10 + 1; //시작 페이지
//
//        int pagenation = 0; // 몇번째 페이지네이션인지
//
//        if(board.getNumber()%tabSize == 0){
//            pagenation = board.getNumber()/tabSize;
//        }else {
//            pagenation = board.getNumber()/tabSize + 1;
//        }
//        rtnMap.put("getList", "getList");
//        rtnMap.put("makePaging", "makePaging");

        return rtnMap;
    }
    public Page<Board> findAllBoard(){
        return boardRepository.findAll();
    }

    public void deleteBoard(Integer board_id){
        boardRepository.deleteById(board_id);
    }

}
