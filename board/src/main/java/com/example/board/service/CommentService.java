package com.example.board.service;

import com.example.board.domain.Comment.Comment;
import com.example.board.repository.BoardRepository;
import com.example.board.repository.CommentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    public Comment commentFinish(Integer board_id, Comment comment) {
        Comment cm = commentRepository.save(comment);
        return cm;
    }

    public Map<String, Object> findComment(Map<String, Object> reqMap){
        Map<String, Object> result = new HashMap<>();
        int board_id = Integer.valueOf(reqMap.get("board_id").toString());

        String query = "select * from (select *,(case when c.depth = 0 then c.comment_id else c.parent_id end)as ttt from Comment as c where comment_sequence = :board_id)as T order by ttt, comment_id";

        Query getComment = entityManager.createNativeQuery(query, Comment.class)
                .setParameter("board_id", board_id);

        List<Comment> resultList = getComment.getResultList();

        result.put("resultList", resultList);
        return result;

    };
}
