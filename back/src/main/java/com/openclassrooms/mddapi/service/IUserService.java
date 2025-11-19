package com.openclassrooms.mddapi.service;

import com.openclassrooms.mddapi.model.User;

public interface IUserService {
    User findById(Long id);
    User update(Long id, User userDetails);
}