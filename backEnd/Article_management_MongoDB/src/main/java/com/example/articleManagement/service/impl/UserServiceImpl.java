package com.example.articleManagement.service.impl;

import com.example.articleManagement.entity.User;

import com.example.articleManagement.repository.UserRepository;
import com.example.articleManagement.service.UserService;

import com.example.articleManagement.util.PasswordUtils;
import com.example.articleManagement.util.ThreadLocalUtil;
import jakarta.annotation.Resource;
import net.bytebuddy.implementation.bytecode.Throw;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Map;

@Service
public class UserServiceImpl implements UserService {


    @Autowired
    private UserRepository repository;

    //更新用户信息
    @Override
    public void update(User user) {
        User existingUser = repository.findUserById(user.getId());
        if(user.getNickname() != null) {
            existingUser.setNickname(user.getNickname());
        }
        if(user.getUserPic() != null) {
            existingUser.setUserPic(user.getUserPic());
        }
        if(user.getEmail() != null) {
            existingUser.setEmail(user.getEmail());
        }
        if(user.getPassword() != null) {
            existingUser.setPassword(user.getPassword());
        }
        existingUser.setUpdateTime(LocalDateTime.now());
        repository.save(existingUser);
    }

    //注册
    @Override
    public void register(String username, String password) {
        String encodePassword = PasswordUtils.encodePassword(password);
        User user = new User();
        user.setUsername(username);
        user.setPassword(encodePassword);
        user.setCreateTime(LocalDateTime.now());
        user.setUpdateTime(LocalDateTime.now());
        user.setId(null);
        repository.save(user);
    }

    //注册用户时查询用户是某已经存在
    @Override
    public User findUserByUsername(String username) {
        return repository.findByUsername(username);
    }

    //更新头像
    @Override
    public void updateAvatar(String avatarUrl) {
        Map<String,Object> map = ThreadLocalUtil.get();
        String id = (String) map.get("id");
        User user = repository.findById(id).orElseThrow(()->new RuntimeException("user cannot find"));
        user.setUserPic(avatarUrl);
        user.setUpdateTime(LocalDateTime.now());
        repository.save(user);
    }

    //更新密码
    @Override
    public void updatePwd(String newPwd) {
        Map<String,Object> map = ThreadLocalUtil.get();
        String id = (String) map.get("id");
        User user = repository.findById(id).orElseThrow(()->new RuntimeException("user cannot find"));
        user.setPassword(PasswordUtils.encodePassword(newPwd));
        user.setUpdateTime(LocalDateTime.now());
        repository.save(user);
    }
}
