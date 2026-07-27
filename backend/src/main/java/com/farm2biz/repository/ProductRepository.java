package com.farm2biz.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.farm2biz.entities.Product;

public interface ProductRepository extends JpaRepository<Product, Long> {

}
