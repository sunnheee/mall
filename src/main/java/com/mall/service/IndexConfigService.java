package com.mall.service;

import com.mall.constants.GlobalConfig;
import com.mall.dao.IndexConfigMapper;
import com.mall.model.IndexConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
public class IndexConfigService {

    @Resource
    private IndexConfigMapper indexConfigMapper;

    @Resource
    private RedisTemplate<String, List<IndexConfig>> redisTemplate;

    public List<IndexConfig> selectByTypeAndCount(int type,int count){
        String key = createKey(type,count);

        List<IndexConfig> p = redisTemplate.opsForValue().get(key);
        if(p != null){
            log.info("{}从缓存中读取数据成功",key);
            return p;
        }
        List<IndexConfig> res = indexConfigMapper.selectByTypeAndCount(type, count);
        redisTemplate.opsForValue().set(key,res,30, TimeUnit.SECONDS);
        return res;
    }

    public String createKey(int type,int count){
        return "indexConfig_" + type + "_" + count + "_" + GlobalConfig.version;
    }
}
