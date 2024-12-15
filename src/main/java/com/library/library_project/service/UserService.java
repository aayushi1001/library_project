package com.library.library_project.service;

import com.library.library_project.dto.UserRequest;
import com.library.library_project.model.User;
import com.library.library_project.model.UserType;
import com.library.library_project.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public User addStudent(UserRequest userRequest) {
        User user = userRequest.toUser();
        user.setUserType(UserType.STUDENT);
        return userRepository.save(user);
    }
}
