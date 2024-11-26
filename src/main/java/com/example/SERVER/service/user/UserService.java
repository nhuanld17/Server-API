package com.example.SERVER.service.user;

import com.example.SERVER.domain.entity.user.User;
import com.example.SERVER.repository.user.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {
	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;
	
	public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
		this.userRepository = userRepository;
		this.passwordEncoder = passwordEncoder;
	}
	
	public User handleGetUserByUsername(String username) {
		return this.userRepository.findByEmail(username);
	}
	
	@Transactional
	public void updateUserToken(String refreshToken, String username) {
		User currentUser = this.userRepository.findByEmail(username);
		if (currentUser != null ) {
			currentUser.setRefreshToken(refreshToken);
			this.userRepository.save(currentUser);
			System.out.println("Updated refresh token for  " + username);
		}
	}
	
	@Transactional
	public void handleRegisterUser(User newUser) {
		newUser.setPassword(passwordEncoder.encode(newUser.getPassword()));
		this.userRepository.save(newUser);
	}
	
	public User getUserByRefreshTokenAndEmail(String refreshToken, String email) {
		return this.userRepository.findByRefreshTokenAndEmail(refreshToken, email);
	}
}
