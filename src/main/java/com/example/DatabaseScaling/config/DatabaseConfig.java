package com.example.DatabaseScaling.config;

import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;

import com.example.DatabaseScaling.enums.Shard;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

@Configuration
public class DatabaseConfig 
{

    @Bean
    public Map<Shard,DBGroup> shards()
    {
        Map<Shard,DBGroup> shardMap=new HashMap<>();
        DataSource shard0Primary=createDataSource("localhost", 5433,"myDb0","user","password");
        DataSource shard0Replica=createDataSource("localhost", 5434,"myDb0","user","password");

        shardMap.put(Shard.SHARD_0, new DBGroup(shard0Primary, shard0Replica));

        DataSource shard1Primary=createDataSource("localhost", 5435,"myDb1","user","password");
        DataSource shard1Replica=createDataSource("localhost", 5436,"myDb1","user","password");

        shardMap.put(Shard.SHARD_1, new DBGroup(shard1Primary, shard1Replica));

        return shardMap;

        
    }

    // @Bean
    // public DataSource shard0Primary()
    // {
    //     return createDataSource("localhost",5433);
    // }

    // @Bean
    // public DataSource shard0Replica()
    // {   
    //     return createDataSource("localhost",5434);
    // }

    // @Bean
    // public JdbcTemplate shard0PrimaryTemplate()
    // {
    //     return new JdbcTemplate(shard0Primary());
    // }

    // @Bean
    // public JdbcTemplate shard0ReplicaTemplate()
    // {
    //     return new JdbcTemplate(shard0Replica());
    // }

    private DataSource createDataSource(String host,int port,String dbName,String user,String password)
    {
        HikariConfig config=new HikariConfig();
        config.setJdbcUrl(String.format("jdbc:postgresql://%s:%d/%s",host,port,dbName));
        config.setUsername(user);
        config.setPassword(password);
        config.setDriverClassName("org.postgresql.Driver");

        config.setMaximumPoolSize(10);
        config.setMinimumIdle(2);
        config.setConnectionTimeout(30000);
        config.setIdleTimeout(600000);
        config.setMaxLifetime(1800000);

        System.out.println("  → Connected to: " + host + ":" + port);
        return new HikariDataSource(config);
    }
}
