package com.api.manager.people.service;


import com.api.manager.people.domain.user.User;
import com.api.manager.people.model.dto.user.RegisterRequest;

import java.util.List;

public interface UserService {
    void create(RegisterRequest dto);
    List<User> list();
}
