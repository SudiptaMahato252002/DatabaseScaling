package com.example.DatabaseScaling.repo;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.example.DatabaseScaling.models.Blog;

@Repository
public class BlogRepository 
{
    private JdbcTemplate primaryTemplate;
    private JdbcTemplate replicaTemplate;

    public BlogRepository(
        @Qualifier("shard0PrimaryTemplate")JdbcTemplate primaryTemplate,
        @Qualifier("shard1ReplicaTemplate")JdbcTemplate replicaTemplate
    )
    {
        this.primaryTemplate=primaryTemplate;
        this.replicaTemplate=replicaTemplate;
        System.out.println("UserRepository created with Shard 0 templates");
    }

    public void createBlog(Blog blog)
    {
        System.out.println("Writing Blog to PRIMARY: " + blog);
        primaryTemplate.update("""
            INSERT INTO blogs 
            (id,userId,title,content,createdAt,updatedAt)
            VALUES (?,?,?,?,?,?)
            """,
            blog.getId(),
            blog.getUserId(),
            blog.getTitle(),
            blog.getContent(),
            blog.getCreatedAt(),
            blog.getUpdatedAt()
        );
        System.out.println("  ✓ Blog written to shard0 primary");
    }

    public Blog findBlogById(String blogId)
    {
        return replicaTemplate.queryForObject("""
                SELECT id,userId,title,content,createdAt,updatedAt
                FROM blogs 
                WHERE id=?
                """, (rs,rownum)->{
                    Blog blog=Blog.builder()
                                .id(rs.getString("id"))
                                .userId(rs.getString("userId"))
                                .title(rs.getString("title"))
                                .content(rs.getString("content"))
                                .createdAt(rs.getTimestamp("createdAt").toInstant())
                                .updatedAt(rs.getTimestamp("updatedAt").toInstant())
                                .build();
                    return blog;
                },blogId);
    }
    
}
