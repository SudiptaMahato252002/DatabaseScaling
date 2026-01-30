package com.example.DatabaseScaling.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.DatabaseScaling.models.Blog;
import com.example.DatabaseScaling.requests.BlogRequestDTO;
import com.example.DatabaseScaling.service.BlogService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping
@RequiredArgsConstructor
public class BlogController 
{
    private final BlogService blogService;

    @PostMapping
    public ResponseEntity<String> createBlog(@RequestBody BlogRequestDTO dto) 
    {
        String blogId = blogService.createBlog(dto);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(blogId);
    }

    @GetMapping("/{blogId}")
    public ResponseEntity<Blog> getBlog(@PathVariable String blogId) 
    {
        Blog blog = blogService.getBlog(blogId);
        return ResponseEntity.ok(blog);
    }
}
