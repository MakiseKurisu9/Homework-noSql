package com.example.articleManagement.service;

import com.example.articleManagement.entity.User;

public interface UserService {

    //update info
    void update(User user);

    //sign up
    void register(String username, String password);

    //find user if not null
    User findUserByUsername(String username);

    void updateAvatar(String avatarUrl);

    void updatePwd(String newPwd);
}
