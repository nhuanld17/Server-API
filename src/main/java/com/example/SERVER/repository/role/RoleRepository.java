package com.example.SERVER.repository.role;

import com.example.SERVER.domain.entity.user.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
	Role findByRoleName(String name);
	
	@Query(value = "SELECT roles.role_name " +
			"FROM roles " +
			"INNER JOIN user_role ON roles.role_id = user_role.role_id " +
			"INNER JOIN user ON user_role.user_id = user.user_id " +
			"WHERE user.email = :email",
			nativeQuery = true)
	String getRoleNameByUserName(@Param("email") String userName);

}
