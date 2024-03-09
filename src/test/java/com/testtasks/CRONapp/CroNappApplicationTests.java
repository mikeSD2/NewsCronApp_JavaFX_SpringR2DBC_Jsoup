package com.testtasks.CRONapp;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.Date;

import org.joda.time.DateTime;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.testtasks.CRONapp.entities.News;
import com.testtasks.CRONapp.repos.NewsRepository;
import com.testtasks.CRONapp.services.NewsServiceImpl;

import reactor.core.publisher.Flux;

@SpringBootTest
class CroNappApplicationTests {
	
	@Autowired
	NewsServiceImpl newsServiceImpl;
	
	@Autowired
	NewsRepository newsRepository;
	
	@Test
	void contextLoads() {
		
		System.out.println(new Date());
		Assertions.assertEquals(2, newsRepository.save(new News("fhhhhhhhhh","ewqfewrqwerq",Instant.now())).block());
	}

}
