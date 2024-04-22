package com.mall.controller.admin;

import com.mall.constants.Constants;
import com.mall.constants.GlobalConfig;
import com.mall.dao.IndexConfigMapper;
import com.mall.model.AdminUser;
import com.mall.model.IndexConfig;
import com.mall.vo.JsonResult;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/admin")
public class ConfigController {

    @Resource
    private IndexConfigMapper indexConfigMapper;

    @GetMapping("indexConfigs/list")
    public JsonResult configType(byte configType, int page, int limit) {

        Map<String,Object> res = new HashMap<>();
        int offset = (page-1)*limit;
        int count = indexConfigMapper.selectCount(configType);
        int totalPage = count/limit;
        if(count%limit>0){
            totalPage++;
        }
        List<IndexConfig> configs = indexConfigMapper.selectByPage(configType, offset, limit);
        res.put("list",configs);
        res.put("totalCount",count);
        res.put("totalPage",totalPage);
        return JsonResult.success(res);
    }

    @PostMapping("indexConfigs/save")
    public JsonResult addConfig(@RequestBody IndexConfig indexConfig, HttpSession session) {
        AdminUser adminUser = (AdminUser) session.getAttribute(Constants.ADMIN_LOGIN_USER_KEY);
        indexConfig.setIsDeleted((byte)0);
        indexConfig.setCreateTime(new Date());
        indexConfig.setUpdateTime(new Date());
        indexConfig.setCreateUser(adminUser.getAdminUserId());
        indexConfigMapper.insert(indexConfig);
        GlobalConfig.version++;
        return JsonResult.success();
    }

    @PostMapping("indexConfigs/update")
    public JsonResult update(@RequestBody IndexConfig indexConfig) {
        indexConfigMapper.updateByPrimaryKeySelective(indexConfig);
        GlobalConfig.version++;
        return JsonResult.success();
    }

    @PostMapping("indexConfigs/delete")
    public JsonResult delete(@RequestBody List<Long> ids){
        for(long id : ids){
            IndexConfig config = new IndexConfig();
            config.setIsDeleted((byte)1);
            config.setConfigId(id);
            indexConfigMapper.updateByPrimaryKeySelective(config);
        }
        GlobalConfig.version++;
        return JsonResult.success();
    }
}
