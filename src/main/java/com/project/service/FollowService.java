package com.project.service;

import com.project.utils.JedisAdapter;
import com.project.utils.RedisKeyUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.Transaction;

import java.util.*;

/**
 * Created by ql on 2019/5/4.
 */
@Service
public class FollowService {
    @Autowired
    JedisAdapter jedisAdapter;

    /**
     * 用户关注了某个实体，可以是问题，关注用户，关注评论等任何实体
     * @return
     */
    public boolean follow(int userId,int entityType,int entityId){
        String followerKey= RedisKeyUtil.getFollowerKey(entityType,entityId);
        String followeeKey=RedisKeyUtil.getFolloweeKey(userId,entityType);
        Date date=new Date();
        Jedis jedis=jedisAdapter.getJedis();
        Transaction tx=jedisAdapter.multi(jedis);
        //实体的粉丝增加当前的用户
        tx.zadd(followerKey,date.getTime(),String.valueOf(userId));
        //当前用户对该类实体关注+1
        tx.zadd(followeeKey,date.getTime(),String.valueOf(entityId));
        List<Object>ret=jedisAdapter.exec(tx,jedis);
        //jedisAdapter.lpush()
       return ret.size()==2&&(Long)ret.get(0)>0&&(Long)ret.get(1)>0;
    }

    public boolean unfollow(int userId,int entityType,int entityId){
        String followerKey= RedisKeyUtil.getFollowerKey(entityType,entityId);
        String followeeKey=RedisKeyUtil.getFolloweeKey(userId,entityType);
        //Date date=new Date();
        Jedis jedis=jedisAdapter.getJedis();
        Transaction tx=jedisAdapter.multi(jedis);
        //实体的粉丝增加当前的用户
        tx.zrem(followerKey,String.valueOf(userId));
        //当前用户对该类实体关注+1
        tx.zrem(followeeKey,String.valueOf(entityId));
        List<Object>ret=jedisAdapter.exec(tx,jedis);
        //jedisAdapter.lpush()
        return ret.size()==2&&(Long)ret.get(0)>0&&(Long)ret.get(1)>0;
    }
    public List<Integer>getFollowers(int entityType,int entityId,int offset,int count){
        String followerKey=RedisKeyUtil.getFollowerKey(entityType,entityId);
        return getIdsFromSet(jedisAdapter.zrevrange(followerKey,offset,count));
    }

    public List<Integer>getFollowees(int userId,int entityType,int offset,int count){
        String followeeKey=RedisKeyUtil.getFolloweeKey(userId,entityType);
        return getIdsFromSet(jedisAdapter.zrange(followeeKey,offset,count));
    }
    public long getFollowersCount(int entityId,int entityType){
        String followerKey=RedisKeyUtil.getFollowerKey(entityType,entityId);
        return jedisAdapter.zcard(followerKey);
    }
    public long getFolloweesCount(int userId,int entityType){
        String followeeKey=RedisKeyUtil.getFolloweeKey(userId,entityType);
        return jedisAdapter.zcard(followeeKey);
    }

    /**
     * 判断用户是否关注了某个实体
     * */
    public boolean isFollower(int entityId,int entityType,int userId){
        String followerKey=RedisKeyUtil.getFollowerKey(entityId,entityType);
        return jedisAdapter.zscore(followerKey,String.valueOf(userId))!=null;
    }


    private static List<Integer> getIdsFromSet(Set<String>idset){
        List<Integer>ids=new ArrayList<>();
        for (String temp:idset){
            ids.add(Integer.parseInt(temp));
        }
        return ids;
    }
}
