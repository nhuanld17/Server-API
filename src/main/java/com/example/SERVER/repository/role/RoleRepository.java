package com.example.SERVER.repository.role;

import com.example.SERVER.domain.entity.user.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
	Role findByRoleName(String name);
}
