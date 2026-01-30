package com.example.DatabaseScaling.requests;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BlogRequestDTO 
{
    private String title;

    private String content;

    private String userId;
    
}
