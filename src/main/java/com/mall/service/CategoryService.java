package com.mall.service;

import com.mall.dao.CategoryDao;
import com.mall.model.Category;
import com.mall.vo.CategoryVo;
import com.mall.vo.SearchPageCategoryVO;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class CategoryService {
    @Resource
    private CategoryDao categoryDao;

    public List<CategoryVo> getCategoryMenu(){
        List<Category> list = categoryDao.selectAll();

        Map<Integer, List<Category>> levelMap = list.stream().collect(Collectors.groupingBy(Category::getCategoryLevel));
        List<Category> level1 = levelMap.get(1).stream().sorted((c1, c2) -> c2.getCategoryRank() - c1.getCategoryRank()).collect(Collectors.toList());
        Map<Integer, List<Category>> parentMap = levelMap.get(2).stream().collect(Collectors.groupingBy(Category::getParentId));
        Map<Integer, List<Category>> thirdMap = levelMap.get(3).stream().sorted((c1, c2) -> c2.getCategoryRank() - c1.getCategoryRank()).collect(Collectors.groupingBy(Category::getParentId));

        List<CategoryVo> voList = new ArrayList<>();


        for(int i=0; i<9; i++){
            Category c = level1.get(i);
            List<Category> secondCat = parentMap.get(c.getCategoryId()).stream().sorted((c1, c2) -> c2.getCategoryRank() - c1.getCategoryRank()).collect(Collectors.toList());
            List<CategoryVo> tmp = secondCat.stream().map(cat -> {
                CategoryVo c2 = new CategoryVo();
                c2.setCategoryName(cat.getCategoryName());
                List<Category> t = thirdMap.get(cat.getCategoryId());
                if(t != null){
                    List<CategoryVo> thirdList = thirdMap.get(cat.getCategoryId()).stream().map(third -> {
                        CategoryVo c3 = new CategoryVo();
                        c3.setCategoryName(third.getCategoryName());
                        c3.setCategoryId(third.getCategoryId());
                        return c3;
                    }).collect(Collectors.toList());
                    c2.setSecondCategory(thirdList);
                }
                return c2;
            }).collect(Collectors.toList());
            CategoryVo vo = new CategoryVo();
            vo.setCategoryName(c.getCategoryName());
            vo.setSecondCategory(tmp);
            voList.add(vo);
        }
        return voList;
    }

    public SearchPageCategoryVO getSearchCategory(int categoryId){
        SearchPageCategoryVO result = new SearchPageCategoryVO();

        Category category = categoryDao.selectById(categoryId);
        if(category == null){
            return null;
        }

        Category second = categoryDao.selectById(category.getParentId());

        List<Category> thirdList = categoryDao.selectByParentAndLevel(category.getParentId(), 3, 8);

        List<SearchPageCategoryVO> voList = new ArrayList<>();
        for(Category c : thirdList){
            SearchPageCategoryVO vo = new SearchPageCategoryVO();
            vo.setCategoryId(c.getCategoryId());
            vo.setCurrentCategoryName(c.getCategoryName());
            vo.setSecondLevelCategoryName(second.getCategoryName());
            voList.add(vo);
        }
        result.setSecondLevelCategoryName(second.getCategoryName());
        result.setCurrentCategoryName(category.getCategoryName());
        result.setCategoryId(second.getCategoryId());
        result.setThirdLevelCategoryList(voList);
        return result;
    }

}
