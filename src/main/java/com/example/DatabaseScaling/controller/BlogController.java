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

    @PostMapping("/create-blog")
    public ResponseEntity<String> createBlog(@RequestBody BlogRequestDTO dto) 
    {
        try 
        {
            String blogId = blogService.createBlog(dto);
            return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(blogId);
        } 
        catch (Exception e) 
        {
            System.err.println("Error: " + e.getMessage());
            return ResponseEntity.status(500).body("Error: " + e.getMessage());
        }
        
    }

    @GetMapping("/{blogId}/{userId}")
    public ResponseEntity<?> getBlog(@PathVariable String blogId,@PathVariable String userId) 
    {
        try 
        {
            Blog blog = blogService.getBlog(blogId,userId);
            return ResponseEntity.ok(blog);    
        } 
        catch (Exception e) 
        {
            System.err.println("Blog not found: " + e.getMessage());
            return ResponseEntity.status(404).body("Blog not found");
        }
        
    }
}
