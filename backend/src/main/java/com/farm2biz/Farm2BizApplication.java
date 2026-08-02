package com.farm2biz;

import org.modelmapper.Conditions;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class Farm2BizApplication {
	public static void main(String[] args) {
		SpringApplication.run(Farm2BizApplication.class, args);
		
	}
	
	@Bean
	 ModelMapper modelMapper()
	{
		ModelMapper mapper=new ModelMapper();
		// STRICT + skip nulls so partial updates never wipe fields
		mapper.getConfiguration()
		.setMatchingStrategy(MatchingStrategies.STRICT)
		.setPropertyCondition(Conditions.isNotNull());
		return mapper;
	}
	
}
