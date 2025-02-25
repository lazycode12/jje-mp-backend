package com.jmp.gestion_notes.service;

import com.jmp.gestion_notes.configuration.JwtUtil;
import com.jmp.gestion_notes.dto.LoginRequest;
import com.jmp.gestion_notes.repo.UtilisateurRepository;
//import com.jmp.gestion_notes.service.UserLoginService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final UserDetailsService userDetailsService;
    private final JwtUtil jwtUtil;
    private final UserLoginService userLoginService;
    private final UtilisateurRepository userRepository;
    
    

    public AuthService(AuthenticationManager authenticationManager, UserDetailsService userDetailsService,
			JwtUtil jwtUtil, UserLoginService userLoginService, UtilisateurRepository userRepository) {
		super();
		this.authenticationManager = authenticationManager;
		this.userDetailsService = userDetailsService;
		this.jwtUtil = jwtUtil;
		this.userLoginService = userLoginService;
		this.userRepository = userRepository;
	}



	public String login(LoginRequest loginRequest) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getLogin(), loginRequest.getPassword())
        );
        final UserDetails userDetails = userDetailsService.loadUserByUsername(loginRequest.getLogin());
        userLoginService.logUserLogin( userRepository.findByLogin(loginRequest.getLogin()).get().getId(), "0.0.0.0");
        return jwtUtil.generateToken(userDetails);
    }

}
