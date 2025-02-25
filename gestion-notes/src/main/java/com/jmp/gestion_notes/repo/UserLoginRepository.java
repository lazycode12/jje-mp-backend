package com.jmp.gestion_notes.repo;

import com.jmp.gestion_notes.model.UserLogin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserLoginRepository extends JpaRepository<UserLogin, Long> {
    List<UserLogin> findByUserId(Long userId);
}
