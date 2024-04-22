package com.mall.service;

import com.mall.constants.GlobalConfig;
import com.mall.dao.GoodsInfoMapper;
import com.mall.model.GoodsInfo;
import com.mall.model.IndexConfig;
import com.mall.vo.PageResult;
import com.mall.vo.SearchCondition;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Slf4j
@Service
public class GoodsInfoService {

    @Resource
    private IndexConfigService indexConfigService;

    @Resource
    private GoodsInfoMapper goodsInfoMapper;

    @Resource
    private RedisTemplate<String,PageResult> redisTemplate;

    public List<GoodsInfo> indexGoods(int type,int count){
        List<IndexConfig> configList = indexConfigService.selectByTypeAndCount(type, count);

        List<Long> idList = configList.stream().map(IndexConfig::getGoodsId).collect(Collectors.toList());

        List<GoodsInfo> list = goodsInfoMapper.selectByIds(idList);
        return list;
    }

    public GoodsInfo getGoodsById(long id){
        return goodsInfoMapper.selectByPrimaryKey(id);

    }

    public PageResult<GoodsInfo> search(SearchCondition sc){
        int offset = (sc.getPage()-1)*30;

        String key = createKey(sc.getKeyword(),sc.getGoodsCategoryId(),sc.getPage());
        PageResult p = redisTemplate.opsForValue().get(key);
        if(p != null){
            log.info("{}从缓存中读取数据成功",key);
            return p;
        }
        int count = goodsInfoMapper.selectCountByCondition(sc);

        System.out.println("总数量为:"+count);

        int totalPage = count / 30;
        if(count % 30 != 0){
            totalPage++;
        }
        List<GoodsInfo> goodsInfos = goodsInfoMapper.selectByCondition(sc,offset,30);

        for(GoodsInfo g : goodsInfos){
            if(g.getGoodsName().length()>20){
                String name = g.getGoodsName().substring(0,20)+ "...";
                g.setGoodsName(name);
            }
            if(g.getGoodsIntro().length()>30){
                String intro = g.getGoodsIntro().substring(0,30) + "...";
                g.setGoodsIntro(intro);
            }
        }
        PageResult<GoodsInfo> pr = new PageResult<>();
        pr.setCurrentPage(sc.getPage());
        pr.setTotalPage(totalPage);
        System.out.println(totalPage);
        pr.setList(goodsInfos);

        redisTemplate.opsForValue().set(key,pr,30, TimeUnit.SECONDS);
        return pr;
    }

    //goods_手机__1_version
    //goods__2_1
    public String createKey(String keyword,String catId,int page){
        return "goods_" + keyword + "_" + catId + "_" + page + "_" + GlobalConfig.version;
    }
}
