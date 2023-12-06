package com.Shop.Security.Model;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;


@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {

	
	@Query(value = "select r from Role r where r.nameRole=?1")
	 Role findRoleByName(String name);
}
