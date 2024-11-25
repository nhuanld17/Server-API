package com.example.SERVER.repository.user;


import com.example.SERVER.domain.entity.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
	public User findByEmail(String email);
	
	User findByRefreshTokenAndEmail(String refreshToken, String email);
}
