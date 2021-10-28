package com.in28minutes.jpa.hibernate.demo;

import static org.junit.Assert.assertNull;
import static org.junit.jupiter.api.Assertions.assertEquals;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;

import com.in28minutes.jpa.hibernate.demo.entity.Address;
import com.in28minutes.jpa.hibernate.demo.entity.Course;
import com.in28minutes.jpa.hibernate.demo.entity.Passport;
import com.in28minutes.jpa.hibernate.demo.entity.Student;
import com.in28minutes.jpa.hibernate.demo.repository.CourseRepository;
import com.in28minutes.jpa.hibernate.demo.repository.StudentRepository;

@RunWith(SpringRunner.class)
@SpringBootTest( classes=DemoApplication.class )
class StudentRepositoryTest {

	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	StudentRepository repository;
	
	@Autowired
	EntityManager em;
	
	@Test
	// @Transactional // required for student.getPassport lazy fetch
				   // Persistence Context
	void someOperationToUnderstandPersistenceContext() {
	  
		repository.someOperationToUnderstandPersistenceContext();

	}
	
	@Test
	@Transactional
	void retrieveStudentAndPassportDetails() {
		Student student = em.find(Student.class, 20001L);
		logger.info("student -> {}", student);
		logger.info("passport -> {}", student.getPassport() );
	}

	@Test
	@Transactional
	void setAddressDetails() {
		Student student = em.find(Student.class, 20001L);
		student.setAddress( new Address("No 101", "Some Street", "Hyderabad"));
		em.flush();
		logger.info("student -> {}", student);
		logger.info("passport -> {}", student.getPassport() );
	}
	
	@Test
	@Transactional
	void retrievePassportAndAssociatedStudent() {
		Passport passport = em.find(Passport.class, 40001L);
		logger.info("passport -> {}", passport );
		logger.info("student -> {}", passport.getStudent() );
	}
	
	@Test
	@Transactional
	public void retrieveStudentAndCoruses() {
		Student student = em.find(Student.class, 20001L);
		logger.info("student -> {}", student );
		logger.info("courses -> {}", student.getCourses() ); // lazy fetch
	}
}
