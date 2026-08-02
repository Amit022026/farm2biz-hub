package com.farm2biz.dtos;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class CategoryDTO {
	private Long categoryId; // ignored on create and populated on response

	@NotBlank(message = "Category name is required")
	private String name;

	private String description;
}
