package com.example.DatabaseScaling.config;

import javax.sql.DataSource;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class DBGroup 
{
    private DataSource primaryDB;
    private DataSource replicaDB;

    @Override
    public String toString() 
    {
        return "DBGroup{primary=" + primaryDB + ", replica=" + replicaDB + "}";
    }

}
