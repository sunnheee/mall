package com.mall.controller.admin;

import com.mall.constants.Constants;
import com.mall.dao.AdminUserMapper;
import com.mall.dao.UserMapper;
import com.mall.model.AdminUser;
import com.mall.model.User;
import com.mall.util.MD5Util;
import com.mall.vo.JsonResult;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController("adminUserController")
@RequestMapping("/admin")
public class UserController {

    @Resource
    private UserMapper userMapper;

    @Resource
    private AdminUserMapper adminUserMapper;

    @GetMapping("users/list")
    public JsonResult list(int page, int limit){

        Map<String,Object> res = new HashMap<>();
        int offset = (page-1)*limit;
        int count = userMapper.selectCount();
        int totalPage = count/limit;
        if(count%limit>0){
            totalPage++;
        }
        List<User> users = userMapper.selectByPage(offset, limit);
        res.put("list",users);
        res.put("totalCount",count);
        res.put("totalPage",totalPage);
        return JsonResult.success(res);

    }

    @PostMapping("users/lock/{lockStatus}")
    @ResponseBody
    public JsonResult lock(@PathVariable("lockStatus") int lockStatus,@RequestBody List<Long> ids){
        userMapper.lockByIds(lockStatus,ids);
        return JsonResult.success();
    }

    @PostMapping("profile/name")
    @ResponseBody
    public JsonResult rename(HttpSession session,String loginUserName,String nickName){
        AdminUser curUser = (AdminUser) session.getAttribute(Constants.ADMIN_LOGIN_USER_KEY);
        curUser.setNickName(nickName);
        curUser.setLoginUserName(loginUserName);
        adminUserMapper.updateByPrimaryKeySelective(curUser);
        return JsonResult.success();
    }

    @PostMapping("profile/password")
    @ResponseBody
    public String repassword(HttpSession session,String originalPassword,String newPassword){
        AdminUser curUser = (AdminUser) session.getAttribute(Constants.ADMIN_LOGIN_USER_KEY);
        System.out.println(originalPassword);
        System.out.println(newPassword);
        String oriPw = MD5Util.MD5Encode(originalPassword,"UTF-8");
        if(oriPw.equals(curUser.getLoginPassword())){
            String newPw = MD5Util.MD5Encode(newPassword,"UTF-8");
            curUser.setLoginPassword(newPw);
            adminUserMapper.updateByPrimaryKeySelective(curUser);
            return "success";
        }
        return "fail";
    }
}
