package com.jmp.gestion_notes.controller;

import com.jmp.gestion_notes.configuration.CustomUserDetails;
import com.jmp.gestion_notes.dto.*;
import com.jmp.gestion_notes.model.Utilisateur;
import com.jmp.gestion_notes.service.AuthService;
import com.jmp.gestion_notes.service.UserActionService;
import com.jmp.gestion_notes.service.UserLoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;
    private UserActionService userActionService;
    private UserLoginService userLoginService;

	@Autowired
    public AuthController(AuthService authService, UserActionService userActionService,UserLoginService userLoginService) {
		this.authService = authService;
		this.userActionService = userActionService;
		this.userLoginService = userLoginService;
	}


    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) throws Exception {
        String token = authService.login(loginRequest);
        return ResponseEntity.ok(
                token != null ? new LoginResponse(token) : Map.of("error", "Invalid credentials")
        );
    }




	@PreAuthorize("hasRole('ADMIN_USER')")
    @GetMapping("/action/{userId}")
    public ResponseEntity<?> getUserActions(@PathVariable Long userId) {
        return ResponseEntity.ok(userActionService.getUserActions(userId));
    }

    @PreAuthorize("hasRole('ADMIN_USER')")
    @GetMapping("/login/{userId}")
    public ResponseEntity<?> getUserLogins(@PathVariable Long userId) {
        return ResponseEntity.ok(userLoginService.getUserLogins(userId));
    }

    @PreAuthorize("hasRole('ADMIN_USER')")
    @GetMapping("/users/logins")
    public ResponseEntity<?> getAllUserLogins() {
        return ResponseEntity.ok(userLoginService.getAllUserLogins());
    }
    
//    @GetMapping("/user")
//    public ResponseEntity<?> getAuthenticatedUser(@AuthenticationPrincipal UserDetails userDetails) {
//        if (userDetails == null) {
//            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User not authenticated");
//        }
//        return ResponseEntity.ok(userDetails);
//    }
//    @GetMapping("/user")
//    public ResponseEntity<CustomUserDetails> getUserInfo(Authentication authentication) {
//    	CustomUserDetails user = (CustomUserDetails) authentication.getPrincipal(); // Cast to your custom User class
//        return ResponseEntity.ok(user);
//      }
    
    @GetMapping("/user")
    public ResponseEntity<?> getUserInfo(Authentication authentication) {
//    	CustomUserDetails user = (CustomUserDetails) authentication.getPrincipal(); // Cast to your custom User class
        return ResponseEntity.ok(authentication.getPrincipal());
      }
}