package com.example.board.service;

import com.example.board.domain.Board.Board;
import com.example.board.domain.Comment.Comment;
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
        Map<String, Object> result = new HashMap<>();
        int board_id = Integer.parseInt(reqMap.get("board_id").toString());

        // 댓글 작성 순서대로 댓글 출력하는  query
        String query = "  SELECT comment_sequence" +
                "              , comment_id, parent_id, depth, comment_writer, comment_content, comment_write_date, comment_modify_date " +
                "           FROM ( " +
                "                  SELECT c.comment_sequence, c.comment_id, c.parent_id, c.depth, c.comment_writer, c.comment_content, c.comment_write_date, c.comment_modify_date " +
                "                       , (CASE WHEN c.depth = 0 THEN c.comment_id ELSE c.parent_id END) AS ttt " +
                "                    FROM Comment c " +
                "                ) T " +
                "          WHERE comment_sequence = :board_id " +
                "       ORDER BY ttt " +
                "              , comment_id ";


        Query getComment = entityManager.createNativeQuery(query);
        List<Object[]> resultList = getComment.setParameter("board_id", board_id).getResultList();

        List<Map<String, Object>> rtnList = new ArrayList<Map<String, Object>>();
        int resultListSize = resultList.size();
        for (int i = 0; i < resultListSize; i++) {
            Object[] item = resultList.get(i);
            Map<String, Object> rtnMap = new HashMap<String, Object>();

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


    public int insertComment(Map<String, Object> reqMap) {
        int board_id = Integer.parseInt(reqMap.get("board_id").toString());
        int depth = Integer.parseInt(reqMap.get("depth").toString());
        System.out.println("depth : " + depth);
        int parent_id = Integer.parseInt(reqMap.get("parent_id").toString());
        String comment_writer = reqMap.get("comment_writer").toString();
        String comment_content = reqMap.get("comment_content").toString();


        String query = "INSERT INTO Comment(depth, comment_sequence , parent_id, comment_writer, comment_content) VALUES(:depth, :board_id, :parent_id, :comment_writer, :comment_content)";

        entityManager.createNativeQuery(query, Comment.class)
                .setParameter("board_id", board_id)
                .setParameter("depth", depth)
                .setParameter("parent_id", parent_id)
                .setParameter("comment_writer", comment_writer)
                .setParameter("comment_content", comment_content)
                .executeUpdate();

        if (depth == 0) {
            String query1 = "UPDATE Comment SET parent_id = LAST_INSERT_ID() ORDER BY comment_id DESC LIMIT 1";
            entityManager.createNativeQuery(query1, Comment.class)
                    .executeUpdate();
        }
        return 1;
    }

    @Transactional
    public int deleteAxios(Map<String, Object> reqMap){
        List<Integer> idList = (List<Integer>) reqMap.get("ids");
//        System.out.println(reqMap);
//        String list = reqMap.values().toString();
//        String idList = list.substring(2, list.length()-2);
//        System.out.println("idList : " + idList);

        String tsql = "";
        tsql += " DELETE FROM Comment WHERE comment_sequence in ( ";
        int idListSize = idList.size();
        tsql += idList.get(0);
        for (int i = 1; i < idListSize; i++) tsql += ", " + idList.get(i);
        tsql += ")";

        System.out.println("tsql : " + tsql);

        entityManager.createNativeQuery(tsql, Comment.class)
                .executeUpdate();

        entityManager.createNativeQuery("DELETE FROM Board WHERE board_id in (:idList)", Board.class)
                .setParameter("idList", idList)
                .executeUpdate();
        return 1;
    }


}