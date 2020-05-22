package com.cedar.service;

import com.cedar.po.User;

public interface UserService {

    User CheckUser(String username,String password);

}
