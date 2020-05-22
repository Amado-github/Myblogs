package com.cedar.service;

import com.cedar.dao.UserRepository;
import com.cedar.po.User;
import com.cedar.util.MD5Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService{

    @Autowired
    private UserRepository userRepository;

    @Override
    public User CheckUser(String username, String password) {

        User user=userRepository.findByUsernameAndPassword(username, MD5Utils.code(password));

        return user;
    }
}
