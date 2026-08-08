package com.farm2biz.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.farm2biz.entities.Category;
import com.farm2biz.entities.Role;
import com.farm2biz.entities.User;
import com.farm2biz.repository.CategoryRepository;
import com.farm2biz.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class DataSeeder implements CommandLineRunner {

	private final UserRepository userRepository;
	private final CategoryRepository categoryRepository;
	private final PasswordEncoder passwordEncoder;

	@Override
	public void run(String... args) {
		// Self-registration is blocked for ADMIN (see UserServiceImpl), so the
		// platform admin is created here at startup instead.
		if (userRepository.findByEmail("admin@farm2biz.com").isEmpty()) {
			User admin = new User();
			admin.setName("Platform Admin");
			admin.setEmail("admin@farm2biz.com");
			admin.setPassword(passwordEncoder.encode("admin123"));
			admin.setRole(Role.ADMIN);
			userRepository.save(admin);
		}

		// Products require a categoryId, so seed a few categories on a fresh DB.
		if (categoryRepository.count() == 0) {
			categoryRepository.save(new Category(null, "Vegetables", "Fresh seasonal vegetables"));
			categoryRepository.save(new Category(null, "Fruits", "Fresh seasonal fruits"));
			categoryRepository.save(new Category(null, "Grains", "Cereals and pulses"));
		}
	}
}
