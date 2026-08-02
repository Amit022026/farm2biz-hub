package com.farm2biz.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;


@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Product {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY) 
	private Long productId;

	private String name;

	private BigDecimal price;

	private BigDecimal quantityAvailable;

	private String unit;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "farmer_id")
	private User farmer;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "category_id")
	private Category category;

}
