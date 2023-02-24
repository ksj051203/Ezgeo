package com.example.board.service;

import com.example.board.domain.Board.Board;
import com.example.board.domain.Comment.Comment;
import com.example.board.domain.Comment.CommentDto;
import com.example.board.repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.*;

@Service
@RequiredArgsConstructor
public class CommentService {
    @Autowired
    public CommentRepository commentRepository;
    @PersistenceContext
    private EntityManager entityManager;

    public CommentService(CommentRepository commentRepository) {
        this.commentRepository = commentRepository;
    }

    public List<Map<String, Object>> findComment(Map<String, Object> reqMap) {
        int board_id = Integer.parseInt(reqMap.get("board_id").toString());

        // 댓글 작성 순서대로 댓글 출력하는 query
        String query = "  SELECT comment_sequence" +
                "              , comment_id, parent_id, depth, comment_writer, comment_content, comment_write_date, comment_modify_date " +
                "           FROM ( " +
                "                  SELECT c.comment_sequence, c.comment_id, c.parent_id, c.depth, c.comment_writer, c.comment_content, c.comment_write_date, c.comment_modify_date " +
                "                       , (CASE WHEN c.depth = 0 THEN c.comment_id ELSE c.parent_id END) AS ttt " +
                "                    FROM Comment c " +
                "                ) T " +
                "          WHERE comment_sequence = :board_id " +
                "       ORDER BY ttt " +
                "              , comment_id";


        Query getComment = entityManager.createNativeQuery(query);
        List<Object[]> resultList = getComment.setParameter("board_id", board_id).getResultList();

        // 게시글 번호, 내용, depth를 반환하는 변수
        List<Map<String, Object>> rtnList = new ArrayList<Map<String, Object>>();

        int resultListSize = resultList.size();
        for (int i = 0; i < resultListSize; i++) {
            Object[] item = resultList.get(i);
            Map<String, Object> rtnMap = new HashMap<String, Object>();

            // depth만큼 답글 구분하기
            String depthChar = "";
            int depth = item[3] == null ? 0 : (Integer) item[3];
            for (int j = 0; j < depth; j++) {
                depthChar += "ㄴ";
            }

            rtnMap.put("content", depthChar + item[5].toString());
            rtnMap.put("id", item[1]);
            rtnMap.put("depth", item[3]);
            rtnList.add(rtnMap);
        }
        return rtnList;
    }


    @Transactional
    public int insertComment(Map<String, Object> reqMap) {
        int board_id = Integer.parseInt(reqMap.get("board_id").toString());
        int depth = Integer.parseInt(reqMap.get("depth").toString());
        int parent_id = Integer.parseInt(reqMap.get("parent_id").toString());
        String comment_writer = reqMap.get("comment_writer").toString();
        String comment_content = reqMap.get("comment_content").toString();
        String comment_password = reqMap.get("comment_password").toString();

        // 댓글(답글) 작성하기
        String query = "INSERT INTO Comment(depth, comment_sequence , parent_id, comment_writer, comment_content, comment_password) VALUES(:depth, :board_id, :parent_id, :comment_writer, :comment_content, :comment_password)";

        entityManager.createNativeQuery(query, Comment.class)
                .setParameter("board_id", board_id)
                .setParameter("depth", depth)
                .setParameter("parent_id", parent_id)
                .setParameter("comment_writer", comment_writer)
                .setParameter("comment_content", comment_content)
                .setParameter("comment_password", comment_password)
                .executeUpdate();

        // 최상단 댓글의 부모키를 자기 자신의 키로 변경
        if (depth == 0) entityManager.createNativeQuery("UPDATE Comment SET parent_id = LAST_INSERT_ID() ORDER BY comment_id DESC LIMIT 1", Comment.class).executeUpdate();

        return 1;
    }

    @Transactional
    public int deleteAxios(Map<String, Object> reqMap){
        List<Integer> idList = (List<Integer>) reqMap.get("ids");

        // checkbox로 선택한 모든 게시물의 댓글 삭제
        String tsql = "";
        tsql += " DELETE FROM Comment WHERE comment_sequence in ( ";
        int idListSize = idList.size();
        tsql += idList.get(0);
        for (int i = 1; i < idListSize; i++) tsql += ", " + idList.get(i);

        tsql += ")";

        entityManager.createNativeQuery(tsql, Comment.class)
                .executeUpdate();

        // checkbox로 선택한 모든 게시글 삭제
        entityManager.createNativeQuery("DELETE FROM Board WHERE board_id in (:idList)", Board.class)
                .setParameter("idList", idList)
                .executeUpdate();

        return 1;
    }

    @Transactional
    public int deletePassword(Map<String, Object> reqMap){
        int comment_sequence = Integer.parseInt(reqMap.get("board_id").toString());
        int comment_id = Integer.parseInt(reqMap.get("idCheck").toString());
        String comment_password = reqMap.get("comment_password").toString();

        // 비밀번호가 일치한다면 선택한 댓글 삭제
        String query = "DELETE FROM Comment WHERE comment_sequence = :comment_sequence and comment_id = :comment_id and comment_password = :comment_password";

        entityManager.createNativeQuery(query, Comment.class)
                .setParameter("comment_sequence", comment_sequence)
                .setParameter("comment_id", comment_id)
                .setParameter("comment_password", comment_password)
                .executeUpdate();

        return 1;
    }

    public int modifyComment(CommentDto commentDto){
        int comment_id = commentDto.getComment_id();
        Comment update = commentRepository.findById(comment_id).orElse(null);
        update.setComment_writer(commentDto.getComment_writer());
        update.setComment_content(commentDto.getComment_content());
        update.setComment_password(commentDto.getComment_password());
        commentRepository.save(update);
        return 1;
    }
}