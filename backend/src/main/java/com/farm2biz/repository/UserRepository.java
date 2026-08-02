package com.farm2biz.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.farm2biz.entities.User;

public interface UserRepository extends JpaRepository<User,Long>{
	Optional<User> findByEmail(String email);
}
