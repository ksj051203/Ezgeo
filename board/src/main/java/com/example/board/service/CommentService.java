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
import javax.persistence.TypedQuery;
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


    public Comment commentFinish(Comment comment) {
        Comment cm = commentRepository.save(comment);
        return cm;
    }

    @Transactional
    public Map<String, Object> findComment(Map<String, Object> reqMap){
        Map<String, Object> result = new HashMap<>();
        int board_id = Integer.valueOf(reqMap.get("board_id").toString());

//        // 댓글 작성 순서대로 댓글 출력하는  query
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


        entityManager.flush();
        entityManager.clear();

//        TypedQuery<Comment> getComment =  (TypedQuery<Comment>) entityManager.createNativeQuery(query, Comment.class);
//        List<Comment> resultList =  getComment//.setParameter("board_id", board_id)
//                .getResultList();
        Query getComment =  entityManager.createNativeQuery(query);
        List<Object[]> resultList =  getComment.setParameter("board_id", board_id).getResultList();

//        System.out.println("resultList" + resultList.toString());

        List<String> rtnList = new ArrayList<String>();


        int resultListSize = resultList.size();
        for(int i = 0; i < resultListSize; i++) {
//            Comment item = (Comment)resultList.get(i);
            Object[] item = resultList.get(i);
//
            String depthChar = "";

//            int depth = getCommentList.get(i).getDepth();
            int depth = (int) item[3];      //  depth를 가져와라
            for (int j = 0; j < depth; j++) depthChar += "ㄴ";

            rtnList.add(depthChar + item[5].toString());    // content 를 가져오롸
//
        }

        result.put("resultList", rtnList);

        return result;

    };

    public Comment insertComment(Map<String, Object> reqMap, Comment comment){
        int board_id = Integer.valueOf(reqMap.get("board_id").toString());
        int depth = Integer.valueOf(reqMap.get("depth").toString());

        String comment_writer = reqMap.get("comment_writer").toString();
        String comment_content = reqMap.get("comment_content").toString();

        String query = "INSERT INTO Comment(depth, comment_sequence , parent_id, comment_writer, comment_content) VALUES(:depth, :board_id, CASE WHEN depth = 0 THEN comment_id ELSE parent_id END, :comment_writer, :comment_content)";

        entityManager.createNativeQuery(query, Comment.class)
                .setParameter("board_id", board_id)
                .setParameter("depth", depth)
                .setParameter("comment_writer", comment_writer)
                .setParameter("comment_content", comment_content)
                .executeUpdate();

        String query1 = "UPDATE Comment SET parent_id = LAST_INSERT_ID() ORDER BY comment_id DESC LIMIT 1";

        entityManager.createNativeQuery(query1, Comment.class)
                .executeUpdate();


        return comment;
    }

}
