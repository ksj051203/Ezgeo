package com.example.board.service;

import com.example.board.domain.Comment.Comment;
import com.example.board.repository.BoardRepository;
import com.example.board.repository.CommentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.thymeleaf.exceptions.ParserInitializationException;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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

        System.out.println(board_id);

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

}
