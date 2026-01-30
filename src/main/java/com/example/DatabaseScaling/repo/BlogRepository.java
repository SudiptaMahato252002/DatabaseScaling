package com.example.DatabaseScaling.repo;

import java.util.Map;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.example.DatabaseScaling.config.DBGroup;
import com.example.DatabaseScaling.enums.Shard;
import com.example.DatabaseScaling.models.Blog;
import com.example.DatabaseScaling.utils.ShardResolver;

@Repository
public class BlogRepository 
{
    // private JdbcTemplate primaryTemplate;
    // private JdbcTemplate replicaTemplate;

    private Map<Shard,DBGroup> shardMap;
    private ShardResolver shardResolver;

    BlogRepository(Map<Shard,DBGroup> shardMap,ShardResolver shardResolver)
    {
        this.shardMap=shardMap;
        this.shardResolver=shardResolver;
    }

    // public BlogRepository(
    //     @Qualifier("shard0PrimaryTemplate")JdbcTemplate primaryTemplate,
    //     @Qualifier("shard1ReplicaTemplate")JdbcTemplate replicaTemplate
    // )
    // {
    //     this.primaryTemplate=primaryTemplate;
    //     this.replicaTemplate=replicaTemplate;
    //     System.out.println("UserRepository created with Shard 0 templates");
    // }

    public void createBlog(Blog blog)
    {
        Shard shard=shardResolver.getShard(blog.getUserId());
        DBGroup dbGroup=shardMap.get(shard);
        JdbcTemplate primaryTemplate=new JdbcTemplate(dbGroup.getPrimaryDB());
        System.out.println("Writing Blog to PRIMARY: " + blog);
        primaryTemplate.update("""
            INSERT INTO blogs 
            (id,user_id,title,content)
            VALUES (?,?,?,?)
            """,
            blog.getId(),
            blog.getUserId(),
            blog.getTitle(),
            blog.getContent()
            // blog.getCreatedAt(),
            // blog.getUpdatedAt()
        );
        System.out.println("  ✓ Blog written to shard0 primary");
    }

    public Blog findBlogById(String blogId,String userId)
    {
        Shard shard=shardResolver.getShard(userId);
        DBGroup dbGroup=shardMap.get(shard);
        JdbcTemplate replicaTemplate=new JdbcTemplate(dbGroup.getReplicaDB());

        return replicaTemplate.queryForObject("""
                SELECT id,user_id,title,content,created_at,updated_at
                FROM blogs 
                WHERE id=?
                """, (rs,rownum)->{
                    Blog blog=Blog.builder()
                                .id(rs.getString("id"))
                                .userId(rs.getString("user_id"))
                                .title(rs.getString("title"))
                                .content(rs.getString("content"))
                                .createdAt(rs.getTimestamp("created_at").toInstant())
                                .updatedAt(rs.getTimestamp("updated_at").toInstant())
                                .build();
                    return blog;
                },blogId);
    }
    
}
