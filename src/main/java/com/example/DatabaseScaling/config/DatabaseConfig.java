package com.example.DatabaseScaling.config;

import javax.sql.DataSource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

@Configuration
public class DatabaseConfig 
{
    @Bean
    public DataSource shard0Primary()
    {
        return createDataSource("localhost",5433);
    }

    @Bean
    public DataSource shard0Replica()
    {   
        return createDataSource("localhost",5434);
    }

    @Bean
    public JdbcTemplate shar0PrimaryTemplate()
    {
        return new JdbcTemplate(shard0Primary());
    }

    @Bean
    public JdbcTemplate shard0ReplicaTemplate()
    {
        return new JdbcTemplate(shard0Replica());
    }

    @Bean
    private DataSource createDataSource(String host,int port)
    {
        HikariConfig config=new HikariConfig();
        config.setJdbcUrl(String.format("jdbc:postgresql://%s:%d/myDb0",host,port));
        config.setUsername("user");
        config.setPassword("password");
        config.setDriverClassName("org.postgresql.Driver");

        config.setMaximumPoolSize(5);
        config.setMinimumIdle(2);
        config.setConnectionTimeout(30000);

        System.out.println("  → Connected to: " + host + ":" + port);
        return new HikariDataSource(config);
    }
}
