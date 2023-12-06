package com.Shop.Security.Model;

import java.util.List;

import org.springframework.core.annotation.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
@Order(1)
public interface UserLoginRepository extends JpaRepository<User, Long> {
	@Query(value = "select r from User r where r.username=?1")
	List<User> findByUsername(String username);

}
