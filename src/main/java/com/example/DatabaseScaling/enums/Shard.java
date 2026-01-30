package com.example.DatabaseScaling.enums;

public enum Shard 
{
    SHARD_0(0),
    SHARD_1(1);
    
    private final int value;

    Shard(int value)
    {
        this.value=value;
    }

    public int getValue()
    {
        return value;
    }

    public static Shard fromValue(int value)
    {
        for(Shard shard:Shard.values())
        {
            if(shard.value==value)
            {
                return shard;
            }
        }
        throw new IllegalArgumentException("Invalid shard value: " + value);
    }

    @Override
    public String toString() 
    {
        return "Shard-" + value;
        
    }

}
