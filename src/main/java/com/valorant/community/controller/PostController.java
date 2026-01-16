package com.valorant.community.controller;

import com.valorant.community.entity.Post;
import com.valorant.community.entity.SiteUser;
import com.valorant.community.service.PostService;
import com.valorant.community.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RequestMapping("/post")
@RequiredArgsConstructor
@Controller
public class PostController {

    private final PostService postService;
    private final UserService userService;

    @GetMapping("/list")
    public String list(Model model) {
        List<Post> postList = this.postService.getList();
        model.addAttribute("postList", postList);
        return "post_list";
    }

    @GetMapping(value = "/detail/{id}")
    public String detail(Model model, @PathVariable("id") Long id) {
        Post post = this.postService.getPost(id);
        model.addAttribute("post", post);
        return "post_detail";
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/create")
    public String createPost() {
        return "post_form";
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/create")
    public String createPost(@RequestParam(value="title") String title, @RequestParam(value="content") String content, Principal principal) {
        SiteUser siteUser = this.userService.getUser(principal.getName());
        this.postService.create(title, content, siteUser);
        return "redirect:/post/list";
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/modify/{id}")
    public String modifyPost(Model model, @PathVariable("id") Long id, Principal principal) {
        Post post = this.postService.getPost(id);
        if(!post.getAuthor().getUsername().equals(principal.getName())) {
            throw new RuntimeException("수정권한이 없습니다.");
        }
        model.addAttribute("post", post);
        return "post_form";
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/modify/{id}")
    public String modifyPost(@PathVariable("id") Long id, @RequestParam(value="title") String title, @RequestParam(value="content") String content, Principal principal) {
        Post post = this.postService.getPost(id);
        if(!post.getAuthor().getUsername().equals(principal.getName())) {
            throw new RuntimeException("수정권한이 없습니다.");
        }
        this.postService.modify(post, title, content);
        return String.format("redirect:/post/detail/%s", id);
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/delete/{id}")
    public String deletePost(@PathVariable("id") Long id, Principal principal) {
        Post post = this.postService.getPost(id);
        if(!post.getAuthor().getUsername().equals(principal.getName())) {
            throw new RuntimeException("삭제권한이 없습니다.");
        }
        this.postService.delete(post);
        return "redirect:/";
    }
}
