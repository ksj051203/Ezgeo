package com.example.board.service;

import com.example.board.domain.Board;
import com.example.board.domain.BoardDto;
import com.example.board.repository.BoardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.thymeleaf.util.StringUtils;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class BoardService {
    @Autowired
    public BoardRepository boardRepository;
    @PersistenceContext
    private EntityManager entityManager;

    private final int TAB_SIZE=5;
    //하단의 tabSize를 5로 고침

    private final int RECORD_LENGTH = 10;

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

    public Map<String, Object> pagingBoard(Map<String, Object> rtnMap){
        Map<String, Object> result = new HashMap<String, Object>();

//         System.out.println("searchKeyword" + searchKeyword);
//         System.out.println("searchType" + searchType);

        String searchType = rtnMap.get("searchType").toString();
        String searchKeyword = rtnMap.get("searchKeyword").toString();
        int nowPage = Integer.valueOf(rtnMap.get("nowPage").toString());

        String search = "<a href='list?searchType=" + (searchType)+ "&searchKeyword="+(searchKeyword) +"&nowPage=";

        String jpql = "select m from Board m ";
        String jpql1 = "select count(*) from Board m ";
        String whereSql = "where";
        List<String> whereCondition = new ArrayList<>();

        if(!StringUtils.isEmpty(searchType)){
            switch (searchType) {
                case "1":
                    whereCondition.add(" m.board_title like :searchKeyword ");
                    break;
                case "2":
                    whereCondition.add(" m.board_content like :searchKeyword ");
                    break;
                case "3":
                    whereCondition.add(" m.board_writer like :searchKeyword ");
                    break;
                case "4":
                    whereCondition.add(" (m.board_title like :searchKeyword or m.board_content like :searchKeyword) ");
                    break;
                default:
                    break;
            }
            jpql += whereSql + whereCondition.get(0);
            jpql1 += whereSql + whereCondition.get(0);
        }


  
        TypedQuery<Board> getRecordTypedQuery = entityManager.createQuery(jpql, Board.class);

        if(!StringUtils.isEmpty(searchType)) getRecordTypedQuery.setParameter("searchKeyword", "%"+searchKeyword+"%");

        //게시물 10개 가져오기
        List<Board> getRecord = getRecordTypedQuery
                .setFirstResult((nowPage-1) * RECORD_LENGTH)
                .setMaxResults(RECORD_LENGTH)
                .getResultList();

        Query allRecordCntTypedQuery = entityManager.createQuery(jpql1);

        if(!StringUtils.isEmpty(searchType)) allRecordCntTypedQuery.setParameter("searchKeyword", "%"+searchKeyword+"%");

        int allRecordCnt =  Integer.valueOf(allRecordCntTypedQuery
                .getResultList().get(0).toString()); //저장된 게시물 개수


        int totalPage = (allRecordCnt/RECORD_LENGTH) + ((allRecordCnt%RECORD_LENGTH>0)? 1: 0); //전체 페이지

        int lastPagination = (totalPage/TAB_SIZE) + ((totalPage%TAB_SIZE>0)? 1:0); // 마지막 페이지네이션

        int pagination = (nowPage/TAB_SIZE) + ((nowPage%TAB_SIZE>0)? 1: 0); // 몇번째 페이지네이션인지

        int endPage =  pagination * TAB_SIZE -1; //끝 페이지
        int startPage = endPage - TAB_SIZE +1; //시작 페이지

        String paging = "";

        paging += search+"1" + "'"+((nowPage==1)? " style='display:none'":"")+"> <<< </a>" ; // 맨 처음페이지
//          if(nowPage != 1) paging += "<a href='list? nowPage=1'> <<< </a>";
//          else paging += "<a href='list?nowPage=1' style='display:none'> <<< </a>";

        paging += search + (startPage-4) + " ' " + ((pagination==1)? "style='display:none'":"")+"> << </a>"; // 이전 사이클 첫페이지
//          if(nowPage==1) paging += "<a href='/list?nowPage=" +(startPage-4) +"' style='display:none'> << </a>";
//          else paging += "<a href='list?nowPage=" + (startPage-4)+ "'> << </a>";

        paging += search + (nowPage-1) + " ' " + ((nowPage==1)? "style='display:none'":"") + "> < </a>"; // 이전 페이지
//          if(nowPage==1) paging += "<a href='list?nowPage=" + (nowPage -1) + "' style='display:none'> < </a>";
//          else paging += "<a href='list?nowPage=" + (nowPage -1) + "'> < </a>";


        for(int i=startPage; i<=endPage; i++){
            int index=i+1;
            paging += ((index == nowPage)? "<strong>" : "")+ search+ (index)  + "' > "+  index + " </a>" + ((index == nowPage)? "</strong>" : "");
//            if(i+1 == nowPage) paging += "<strong><a href='/list?nowPage=" + (i+1)  + "' > "+  (i+1) + " </a></strong>"; // 현재 페이지 표시
//            else paging += "<a href='/list?nowPage=" + (i+1) + "' > " +  (i+1) + " </a>"; // 페이지 표시
        }
                                        

        paging += search + (nowPage+1) +" ' " + ((nowPage==totalPage)? "style='display:none'": "") + "> ></a>"; // 다음 페이지
        paging += search +  (startPage+1+TAB_SIZE)+ " ' " + ((pagination==lastPagination)? "style='display:none'": "") +"> >> </a>"; // 다음 사이클 첫번째 페이지
        paging += search + totalPage + " ' " + ((totalPage==nowPage)? "style='display:none'": "") +"> >>> </a>"; //맨 끝으로

        result.put("getRecord", getRecord);
        result.put("paging", paging);
        result.put("nowPage", nowPage);
//        result.put("searchKeyword", searchKeyword);
        result.put("searchType", searchType);

        return result;
    }

    public void deleteBoard(Integer board_id){
        boardRepository.deleteById(board_id);
    }

}
