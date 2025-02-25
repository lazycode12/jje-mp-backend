package com.jmp.gestion_notes.service;

import com.jmp.gestion_notes.model.UserLogin;
import com.jmp.gestion_notes.repo.UserLoginRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class UserLoginService {
    @Autowired
    private UserLoginRepository userLoginRepository;

    public void logUserLogin(Long userId, String ipAddress) {
        UserLogin userLogin = new UserLogin();
        userLogin.setUserId(userId);
        userLogin.setIpAddress(ipAddress);
        userLogin.setTimestamp(LocalDateTime.now());
        userLoginRepository.save(userLogin);
    }

    public List<UserLogin> getUserLogins(Long userId) {
        return userLoginRepository.findByUserId(userId);
    }

    public List<UserLogin> getAllUserLogins() {
        return userLoginRepository.findAll();
    }
}

