package com.jmp.gestion_notes.service;


import com.jmp.gestion_notes.model.UserAction;
import com.jmp.gestion_notes.repo.UserActionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class UserActionService {
    @Autowired
    private UserActionRepository userActionRepository;

    public void logUserAction(Long userId, String action) {
        UserAction userAction = new UserAction();
        userAction.setUserId(userId);
        userAction.setAction(action);
        userAction.setTimestamp(LocalDateTime.now());
        userActionRepository.save(userAction);
    }

    public List<UserAction> getUserActions(Long userId) {
        return userActionRepository.findByUserId(userId);
    }
}

