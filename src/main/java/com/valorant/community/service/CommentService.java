package com.valorant.community.service;

import com.valorant.community.entity.Comment;
import com.valorant.community.entity.Post;
import com.valorant.community.entity.SiteUser;
import com.valorant.community.repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@RequiredArgsConstructor
@Service
public class CommentService {

    private final CommentRepository commentRepository;

    public void create(Post post, String content, SiteUser author) {
        Comment comment = new Comment();
        comment.setContent(content);
        comment.setCreateDate(LocalDateTime.now());
        comment.setPost(post);
        comment.setAuthor(author);
        this.commentRepository.save(comment);
    }
    
    public Comment getComment(Long id) {
        return this.commentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("comment not found"));
    }

    public void modify(Comment comment, String content) {
        comment.setContent(content);
        comment.setModifyDate(LocalDateTime.now());
        this.commentRepository.save(comment);
    }
    
    public void delete(Comment comment) {
        this.commentRepository.delete(comment);
    }
}
