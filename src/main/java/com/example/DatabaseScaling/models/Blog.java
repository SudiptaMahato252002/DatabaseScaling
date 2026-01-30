package com.example.DatabaseScaling.models;

import java.time.Instant;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Blog 
{
    private String id;
    private String userId;
    private String title;
    private String content;
    private Instant createdAt;
    private Instant updatedAt;

    
}
