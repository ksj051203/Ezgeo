package com.example.board.service;

import com.example.board.domain.Board;
import com.example.board.domain.BoardDto;
import com.example.board.repository.BoardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class BoardService {
    @Autowired
    public BoardRepository boardRepository;
    @PersistenceContext
    private EntityManager entityManager;

    private final int tabSize=5;
    //하단의 tabSize를 5로 고침

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

    public Map<String, Object> makePaging(Integer nowPage){
        Map<String, Object> rtnMap = new HashMap<String, Object>();
        int target = (nowPage-1) * 10;

        // 게시판 레코드 10개 가져오는 로직
        List<Board> getList = entityManager.createQuery("select m from Board as m order by m.board_id", Board.class)
                .setFirstResult(target)
                .setMaxResults(10)
                .getResultList();

        int record = Integer.valueOf(entityManager.createQuery("select count(*) as TTT from Board").getResultList().get(0).toString());
        int totalPage = (record/10) + ((record % 10) > 0 ? 1 : 0 );

//        int endPage = nowPage * 10; // 끝 페이지
//        int startPage = endPage - 10 + 1; //시작 페이지
//        System.out.println("startPage" + startPage);

        int pagenation = 0; // 몇번째 페이지네이션인지
        if(nowPage%tabSize == 0){
            pagenation = nowPage/tabSize;
        }else {
            pagenation = nowPage/tabSize + 1;
        }

        int endTabSize = pagenation * tabSize -1; // 끝 페이지
        int startTabSize = endTabSize - tabSize + 1; // 첫 페이지
        //pagenation = pagenation * tabSize;

        String makePagingg = "";

        makePagingg += "<a href='/list_new?nowPage=1'> <<< </a>"; //맨처음
        makePagingg += "<a href='/list_new?nowPage=" +(startTabSize+1 - tabSize )+ "'>" + "<<" + " </a>";   // 이전 사이클
        makePagingg += "<a href='/list_new?nowPage=" + (nowPage-1) + "'> " +"<"+ " </a>";

        int[] makePaging = new int[5];
        for(int i = startTabSize; i <= endTabSize; i++) {
            if(i+1 == nowPage){
                makePagingg += "<strong><a href='/list_new?nowPage=" + (i+1) + "' > " +  (i+1) + " </a></strong>";
            }else{
                makePagingg += "<a href='/list_new?nowPage=" + (i+1) + "' > " +  (i+1) + " </a>";
            }

        }

        makePagingg += "<a href='/list_new?nowPage=" + (nowPage+1) + "'> ></a>";
        makePagingg += "<a href='/list_new?nowPage=" +  (startTabSize+1+ tabSize)+ "'> >> </a>";
        makePagingg += "<a href='/list_new?nowPage=" + totalPage + "'> >>> " +"</a>";


        rtnMap.put("getList", getList);
        rtnMap.put("makePagingg", makePagingg);
        rtnMap.put("nowPage", nowPage);
        rtnMap.put("startTabSize", startTabSize);
        rtnMap.put("endTabSize", endTabSize);
        return rtnMap;
    }
    public List<Board> findAllBoard(){
        return boardRepository.findAll();
    }

    public void deleteBoard(Integer board_id){
        boardRepository.deleteById(board_id);
    }

}
