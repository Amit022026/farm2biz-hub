package com.farm2biz.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.farm2biz.entities.Category;

public interface CategoryRepository extends JpaRepository<Category, Long>{

}
