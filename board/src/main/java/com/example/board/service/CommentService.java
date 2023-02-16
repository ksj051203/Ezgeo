package com.example.board.service;

import com.example.board.domain.Comment.Comment;
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
    @PersistenceContext
    private EntityManager entityManager;

    public CommentService(CommentRepository commentRepository) {
        this.commentRepository = commentRepository;
    }

    public Comment commentFinish(Comment comment) {
        Comment cm = commentRepository.save(comment);
        return cm;
    }

    public Map<String, Object> findComment(){
        Map<String, Object> result = new HashMap<>();
        String query = "select * from (select *,(case when depth = 0 then comment_id else parent_id end)as ttt from Comment as c where 1=1)as T  order by ttt, comment_id";

        Query getComment = entityManager.createNativeQuery(query, Comment.class);

        List<Comment> resultList = getComment.getResultList();

        System.out.println("resultList"+ resultList);



        result.put("resultList", resultList);
//        result.put("comment_sequence", comment_sequence);
//        result.put("comment_id", comment_id);
//        result.put("parent_id", parent_id);
//        result.put("depth", depth);
//        result.put("comment_writer", comment_writer);
//        result.put("comment_content", comment_content);

        return result;

    };
}
