package com.example.SERVER.service.user;

import com.example.SERVER.domain.entity.user.MyUserDetails;
import com.example.SERVER.domain.entity.user.User;
import com.example.SERVER.repository.user.UserRepository;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Data
@NoArgsConstructor
@Service
public class UserDetailServiceImpl implements UserDetailsService {
	private UserRepository userRepository;

	@Autowired
	public UserDetailServiceImpl(UserRepository userRepository) {
		this.userRepository = userRepository;
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		User user = userRepository.findByEmail(username);

		if (user == null) {
			throw new UsernameNotFoundException("Could not find user");
		}
		
		
		
		return new MyUserDetails(user);
	}
}