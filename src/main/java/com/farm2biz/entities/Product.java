package com.farm2biz.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;

// @Entity        -> "This class represents a database table"
// @Getter/@Setter -> Lombok auto-generates getX()/setX() methods for every field
//                    (saves us writing ~40 lines of boilerplate by hand)
// @NoArgsConstructor / @AllArgsConstructor -> Lombok generates a blank constructor
//                    AND one that takes every field, both of which JPA/Hibernate need internally
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Product {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY) // MySQL auto-increments it: 1, 2, 3...
	private Long productId;

	private String name;

	private String category;

	private BigDecimal price;

	private BigDecimal quantityAvailable;

	private String unit; // e.g. "kg", "dozen", "litre"
}
