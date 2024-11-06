package com.example.SERVER.repository;

import com.example.SERVER.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
	public User findByEmail(String email);
}
