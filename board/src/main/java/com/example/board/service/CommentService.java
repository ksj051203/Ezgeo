package com.example.board.service;

import com.example.board.domain.Comment.Comment;
import com.example.board.repository.BoardRepository;
import com.example.board.repository.CommentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.*;

@Service
public class CommentService {
    @Autowired
    public CommentRepository commentRepository;

    @Autowired
    public BoardRepository boardRepository;
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

        // 댓글 작성 순서대로 댓글 출력하는  query
        String query = "select * from (select *,(case when c.depth = 0 then c.comment_id else c.parent_id end)as ttt from Comment as c )as T where comment_sequence = :board_id order by ttt, comment_id";

        Query getComment = entityManager.createNativeQuery(query, Comment.class)
                .setParameter("board_id", board_id);

        //
        List<String> rtnList = new ArrayList<String>();


        List<Comment> resultList = getComment.getResultList();

        int resultListSize = resultList.size();
        for (int i = 0; i < resultListSize; i++) {
            Comment item = resultList.get(i);
            int depth = resultList.get(i).getDepth();

            String depthChar = "";
            for (int j = 0; j < depth; j++) depthChar += "ㄴ";

            rtnList.add(depthChar + item.getComment_content());

        }

        result.put("resultList", rtnList);

        return result;

    };

    public Comment insertComment(Map<String, Object> reqMap, Comment comment){
        int board_id = Integer.valueOf(reqMap.get("board_id").toString());
        int depth = Integer.valueOf(reqMap.get("depth").toString());

        String comment_writer = reqMap.get("comment_writer").toString();
        String comment_content = reqMap.get("comment_content").toString();

        String query = "insert into Comment(depth, comment_sequence , parent_id, comment_writer, comment_content) values(:depth, :board_id, case when depth = 0 then comment_id else parent_id end, :comment_writer, :comment_content)";

        entityManager.createNativeQuery(query, Comment.class)
                .setParameter("board_id", board_id)
                .setParameter("depth", depth)
                .setParameter("comment_writer", comment_writer)
                .setParameter("comment_content", comment_content)
                .executeUpdate();

        String query1 = "Update Comment SET parent_id = LAST_INSERT_ID() order by comment_id desc limit 1";

        entityManager.createNativeQuery(query1, Comment.class)
                .executeUpdate();


        return comment;
    }

}
