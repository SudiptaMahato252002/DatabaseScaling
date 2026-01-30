package com.example.DatabaseScaling.service;

import java.time.Instant;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.example.DatabaseScaling.models.Blog;
import com.example.DatabaseScaling.repo.BlogRepository;
import com.example.DatabaseScaling.requests.BlogRequestDTO;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BlogService 
{
    private final BlogRepository blogRepo;

    public String createBlog(BlogRequestDTO dto) 
    {

        // BUSINESS LOGIC
        if (dto.getContent().length() < 50) {
            throw new IllegalArgumentException("Blog content too short");
        }

        Blog blog = new Blog();
        blog.setId(UUID.randomUUID().toString());
        blog.setTitle(dto.getTitle());
        blog.setContent(dto.getContent());
        blog.setUserId(dto.getUserId());
        blog.setCreatedAt(Instant.now());
        blog.setUpdatedAt(Instant.now());

        blogRepo.createBlog(blog);

        return blog.getId();
    }

    public Blog getBlog(String blogId,String userId)
    {
        Blog blog=blogRepo.findBlogById(blogId,userId);
        if(blog==null)
        {
            throw new IllegalArgumentException("No blog with that id exists");
        }
        return blog;
    }
    
}
