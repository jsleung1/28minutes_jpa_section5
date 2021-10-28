package com.in28minutes.jpa.hibernate.demo;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Optional;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;

import com.in28minutes.jpa.hibernate.demo.entity.Course;
import com.in28minutes.jpa.hibernate.demo.entity.Review;
import com.in28minutes.jpa.hibernate.demo.repository.CourseRepository;
import com.in28minutes.jpa.hibernate.demo.repository.CourseSpringDataRepository;

@RunWith(SpringRunner.class)
@SpringBootTest( classes=DemoApplication.class )
class CourseSpringDataRepositoryTest {

	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	CourseSpringDataRepository repository;
	
	
	@Test
	void findById_CoursePresent() {
		Optional<Course> courseOptional = repository.findById( 10001L );
		assertTrue( courseOptional.isPresent() );
		logger.info("{}", courseOptional.isPresent() );
	}
	
	@Test
	void findById_CourseNotPresent() {
		Optional<Course> courseOptional = repository.findById( 20001L );
		assertFalse( courseOptional.isPresent() );
		logger.info("{}", courseOptional.isPresent() );
	}
	
	@Test
	void playingAroundWithSpringDataRepository() {
//		Course course = new Course("Microservices in 100 Steps");
//		repository.save( course );
//	
//		course.setName("Microservices in 100 Steps - Updated" );
//		repository.save( course );
		logger.info("Course -> {} ", repository.findAll() );
		logger.info("Count -> {} ", repository.count() );
		
	}
	
	@Test
	void pagination() {
		
		PageRequest pageRequest = PageRequest.of(0, 3);
		
		Sort sort = Sort.by( Sort.Direction.DESC, "name" ); //.and( new Sort 
		
		Page<Course> firstPage = repository.findAll( pageRequest );
		logger.info("First Page -> {} ", firstPage.getContent() );
		
		Pageable secondPageable = firstPage.nextPageable();
		Page<Course> secondPage = repository.findAll( secondPageable );
		logger.info("Second Page -> {} ", secondPage.getContent() );	
	}
	
	@Test
	public void findUsingName() {
		logger.info("FindByName -> {} ", repository.findByName("JPA in 50 Steps") );
	}
}
