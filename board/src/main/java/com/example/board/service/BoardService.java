package com.example.board.service;

import com.example.board.domain.Board.Board;
import com.example.board.domain.Board.BoardDto;
import com.example.board.domain.Comment.Comment;
import com.example.board.repository.BoardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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
@RequiredArgsConstructor
public class BoardService {
    @Autowired
    public BoardRepository boardRepository;
    @PersistenceContext
    private EntityManager entityManager;

    private final int TAB_SIZE = 5;
    //하단의 tabSize를 5로 고침

    private final int RECORD_LENGTH = 10;
    // 한 페이지에 보여줄 게시글 갯수

    public void createFinish(Board board) {
        boardRepository.save(board);
    }

    public Board findBoard(Integer board_id) {
        return boardRepository.findById(board_id).get();
    }

    public void update(Integer board_id, BoardDto boardDto) {
        Board update = boardRepository.findById(board_id).orElse(null);
        update.setBoard_title(boardDto.getBoard_title());
        update.setBoard_writer(boardDto.getBoard_writer());
        update.setBoard_content(boardDto.getBoard_content());
        boardRepository.save(update);
    }

    public Map<String, Object> pagingBoard(Map<String, Object> rtnMap) {

        // 검색, 게시글 리스트, 페이징 전체를 반환할 변수
        Map<String, Object> result = new HashMap<String, Object>();

        String searchType = rtnMap.get("searchType").toString();
        String searchKeyword = rtnMap.get("searchKeyword").toString();
        int nowPage = Integer.valueOf(rtnMap.get("nowPage").toString());

        // 모든 게시물 가져오기
        String jpql = "select m from Board m ";

        // 저장된 게시물 개수
        String jpql1 = "select count(*) from Board m ";
        String whereSql = "where";
        List<String> whereCondition = new ArrayList<>();

        //검색 시 보여질 게시글
        if (!StringUtils.isEmpty(searchType)) {
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

        if (!StringUtils.isEmpty(searchType))
            getRecordTypedQuery.setParameter("searchKeyword", "%" + searchKeyword + "%");

        //게시물 10개 가져오기
        List<Board> getRecord = getRecordTypedQuery
                .setFirstResult((nowPage - 1) * RECORD_LENGTH)
                .setMaxResults(RECORD_LENGTH)
                .getResultList();

        Query allRecordCntTypedQuery = entityManager.createQuery(jpql1);

        if (!StringUtils.isEmpty(searchType))
            allRecordCntTypedQuery.setParameter("searchKeyword", "%" + searchKeyword + "%");

        //저장된 게시물 개수
        int allRecordCnt = Integer.parseInt(allRecordCntTypedQuery
                .getResultList().get(0).toString());


        int totalPage = (allRecordCnt / RECORD_LENGTH) + ((allRecordCnt % RECORD_LENGTH > 0) ? 1 : 0); // 전체 페이지


        int lastPagination = (totalPage / TAB_SIZE) + ((totalPage % TAB_SIZE > 0) ? 1 : 0); // 마지막 페이지네이션


        int pagination = (nowPage / TAB_SIZE) + ((nowPage % TAB_SIZE > 0) ? 1 : 0); // 몇번째 페이지네이션인지


        int endPage = pagination * TAB_SIZE - 1; // 끝 페이지


        int startPage = endPage - TAB_SIZE + 1; // 시작 페이지

        // 페이징 파라미터
        String search = "<a href='list?searchType=" + (searchType) + "&searchKeyword=" + (searchKeyword) + "&nowPage=";

        String paging = "";

        paging += makePagingTag(search, 1, (nowPage == 1), "<<<"); // 맨 처음으로
        paging += makePagingTag(search, (startPage - 4) , (pagination == 1), "<<"); // 이전 사이클 첫번째 페이지
        paging += makePagingTag(search, (nowPage - 1) , (nowPage == 1), "<"); // 이전 페이지


        // 페이징 번호 생성 및 현재 페이지 표시
        for (int i = startPage; i <= endPage; i++) {
            int index = i + 1;
            paging += ((index == nowPage) ? "<strong>" : "") + search + (index) + "' > " + index + " </a>" + ((index == nowPage) ? "</strong>" : "");
        }

        paging += makePagingTag(search, (nowPage + 1) , (nowPage == totalPage), ">"); // 다음 페이지
        paging += makePagingTag(search, (startPage + 1 + TAB_SIZE) , (pagination == lastPagination), ">>");// 다음 사이클 첫번째 페이지
        paging += makePagingTag(search, (totalPage) , (totalPage == nowPage), ">>>"); // 맨 끝으로


        result.put("getRecord", getRecord); // 게시글 10개 가져오기
        result.put("searchKeyword", searchKeyword); // 검색 키워드
        result.put("searchType", searchType); //  검색 타입
        result.put("paging", paging); // 페이징

        return result;
    }


    /**
     *
     * @param search
     * @param page
     * @param condition
     * @param content
     * @return
     */

    // 반복되는 페이징 태그 한번에 처리하기
    private String makePagingTag(String search, int page, boolean condition, String content) {
        return search + page + " ' " + (condition ? "style='display:none'" : "") + "> " + content + " </a>";
    }

    //게시글 삭제(form)
    @Transactional
    public void deleteBoard(Integer board_id) {
        entityManager.createNativeQuery("DELETE FROM Comment WHERE comment_sequence = :board_id", Comment.class)
                .setParameter("board_id", board_id)
                .executeUpdate();

        boardRepository.deleteById(board_id);
    }
}
