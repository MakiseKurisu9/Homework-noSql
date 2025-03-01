package com.example.articleManagement.controller;


import com.example.articleManagement.entity.Result;
import com.example.articleManagement.entity.User;
import com.example.articleManagement.service.UserService;
import com.example.articleManagement.util.JwtUtil;
import com.example.articleManagement.util.PasswordUtils;
import com.example.articleManagement.util.ThreadLocalUtil;
import jakarta.annotation.Resource;
import jakarta.validation.constraints.Pattern;
import org.hibernate.validator.constraints.URL;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/user")
@Validated
public class UserController {
    @Resource
    private UserService userService;

    @Resource
    private StringRedisTemplate template;

    @PostMapping("/register")
    public Result register(@Pattern(regexp = "^\\S{5,16}$") String username,@Pattern(regexp = "^\\S{5,16}$") String password) {
            User user = userService.findUserByUsername(username);
            if (user == null) {
                userService.register(username, password);
                return Result.success();
            } else {
                return Result.error("用户名已被占用");
            }
        }

    @PostMapping("/login")
    public Result<String> login(@Pattern(regexp = "^\\S{5,16}$",message = "请确保用户名为5-16位字符") String username,
                                @Pattern(regexp = "^\\S{5,16}$",message = "请确保密码为5-16位字符") String password) {
        User user = userService.findUserByUsername(username);
        if(user ==null) {
            return Result.error("用户名错误");
        }else {
            if(PasswordUtils.match(password,user.getPassword())) {
                Map<String,Object> claims = new HashMap<>();
                claims.put("id",user.getId());
                claims.put("username",user.getUsername());
                //make token different
                claims.put("uuid", UUID.randomUUID().toString());
                String token = JwtUtil.genToken(claims);
                ValueOperations<String, String> operations = template.opsForValue();
                operations.set(token,token,1, TimeUnit.HOURS);
                return Result.success(token);
            }
        }
        return Result.error("密码错误");
    }

    @GetMapping("/userInfo")
    public Result<User> UserInfo(){
        Map<String,Object> map = ThreadLocalUtil.get();
        String username = (String) map.get("username");
        User user = userService.findUserByUsername(username);
        return Result.success(user);
    }

    @PutMapping("/update")
    public Result update(@RequestBody @Validated User user) {
        userService.update(user);
        return Result.success();
    }

    @PatchMapping("/updateAvatar")
    public Result updateAvatar(@RequestParam @URL String avatarUrl){
        userService.updateAvatar(avatarUrl);
        return Result.success();
    }

    @PatchMapping("/updatePwd")
    public Result updatePwd(@RequestBody Map<String,String> params,@RequestHeader("Authorization") String token){
        String oldPwd = params.get("old_pwd");
        String newPwd = params.get("new_pwd");
        String rePwd = params.get("re_pwd");
        if(!StringUtils.hasLength(oldPwd) || !StringUtils.hasLength(newPwd) || !StringUtils.hasLength(rePwd)){
            return Result.error("参数不能为空");
        }
        Map<String,Object> map = ThreadLocalUtil.get();
        String username = (String) map.get("username");
        User user = userService.findUserByUsername(username);
        if(!PasswordUtils.match(oldPwd,user.getPassword())) {
            return Result.error("原密码错误");
        }

        if(!rePwd.equals(newPwd)) {
            return Result.error("两次填写的密码不同");
        }

        userService.updatePwd(newPwd);

        ValueOperations<String, String> operations = template.opsForValue();
        operations.getOperations().delete(token);

        return Result.success();
    }
}

