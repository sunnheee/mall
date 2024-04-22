package com.mall.service;

import com.mall.dao.CarouselMapper;
import com.mall.model.Carousel;
import com.mall.vo.CarouselVo;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Service
public class CarouselService {

    @Resource
    private CarouselMapper carouselMapper;

    public List<CarouselVo> selectForIndex(){
        List<Carousel> carouselList = carouselMapper.selectForIndex();

        List<CarouselVo> list = new ArrayList<>();
        for(Carousel carousel : carouselList){
            CarouselVo vo = new CarouselVo();
            BeanUtils.copyProperties(carousel,vo);
            list.add(vo);
        }
        return list;
    }

}
