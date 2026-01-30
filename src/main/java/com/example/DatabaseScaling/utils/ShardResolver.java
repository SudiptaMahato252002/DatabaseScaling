package com.example.DatabaseScaling.utils;

import java.nio.charset.StandardCharsets;
import java.util.zip.CRC32;

import org.springframework.stereotype.Component;

import com.example.DatabaseScaling.enums.Shard;

@Component
public class ShardResolver 
{
    private static final int TOTAL_SHARDS=2;

    public Shard getShard(String userId)
    {
        CRC32 crc32=new CRC32();
        crc32.update(userId.getBytes(StandardCharsets.UTF_8));
        long hashValue=crc32.getValue();
        int shardIndex=Math.floorMod(hashValue,TOTAL_SHARDS );
        Shard shard=Shard.fromValue(shardIndex);
        return shard;

    }
    
}
