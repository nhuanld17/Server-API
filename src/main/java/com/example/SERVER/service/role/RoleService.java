package com.example.SERVER.service.role;

import com.example.SERVER.domain.entity.user.Role;
import com.example.SERVER.repository.role.RoleRepository;
import org.springframework.stereotype.Service;

@Service
public class RoleService {
	private final RoleRepository roleRepository;
	
	public RoleService(RoleRepository roleRepository) {
		this.roleRepository = roleRepository;
	}
	
	public Role getRoleByName(String name) {
		return roleRepository.findByRoleName(name);
	}
	
	public String getRoleNameByUserName(String userName) {
		return roleRepository.getRoleNameByUserName(userName);
	}
}
