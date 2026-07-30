package com.farm2biz.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.farm2biz.entities.User;

public interface UserRepository extends JpaRepository<User,Long>{
        // findById, findAll, save, deleteById all come free from JpaRepository.

		// NEW in Phase 2: login happens by EMAIL, not by userId (nobody
		// memorizes their database row number). Spring Data JPA reads this
		// method's name and auto-generates:
		//   SELECT * FROM user WHERE email = ?
		// - we never write that SQL ourselves.
	Optional<User> findByEmail(String email);
	
}
