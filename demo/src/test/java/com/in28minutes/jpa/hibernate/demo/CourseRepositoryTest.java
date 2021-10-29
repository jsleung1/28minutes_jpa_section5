package com.in28minutes.jpa.hibernate.demo;

import static org.junit.Assert.assertNull;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import javax.persistence.EntityGraph;
import javax.persistence.EntityManager;
import javax.persistence.Subgraph;
import javax.transaction.Transactional;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;

import com.in28minutes.jpa.hibernate.demo.entity.Course;
import com.in28minutes.jpa.hibernate.demo.entity.Review;
import com.in28minutes.jpa.hibernate.demo.repository.CourseRepository;

@RunWith(SpringRunner.class)
@SpringBootTest( classes=DemoApplication.class )
class CourseRepositoryTest {

	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	CourseRepository repository;
	
	@Autowired
	EntityManager em;
	
	@Test
	void findById_basic() {
		// Course course = repository.findById(10001L);
		// assertEquals("JPA in 50 Steps", course.getName() );
		Course course = repository.findById(10002L);
		assertEquals("Spring in 50 Steps", course.getName() );			  
	}
	
	@Test
	@Transactional
	public void creatingNPlusOneProblem() {
		List<Course> courses = 
			em.createNamedQuery("query_get_all_courses", Course.class).getResultList();
		for (Course course : courses ) {
			// Lazy fetch causes Hibernate N + 1 problem
			logger.info("Course -> {} Students -> {}", course, course.getStudents());
		}
	}
	
	@Test
	@Transactional
	public void solvingNPlusOneProblem_EntityGraph() {
		EntityGraph<Course> entityGraph = em.createEntityGraph(Course.class);
		Subgraph<Object> subGraph = entityGraph.addSubgraph("students");
		
		List<Course> courses = 
			em.createNamedQuery("query_get_all_courses", Course.class)
			.setHint("javax.persistence.loadgraph", entityGraph)
			.getResultList();
		for (Course course : courses ) {
			// Lazy fetch causes Hibernate N + 1 problem
			logger.info("Course -> {} Students -> {}", course, course.getStudents());
		}
	}
	
	@Test
	@Transactional
	public void solvingNPlusOneProblem_joinFetch() {
		
		List<Course> courses = 
			em.createNamedQuery("query_get_all_courses_join_fetch", Course.class)
			.getResultList();
		for (Course course : courses ) {
			// Lazy fetch causes Hibernate N + 1 problem
			logger.info("Course -> {} Students -> {}", course, course.getStudents());
		}
	}
	
	@Test
	// @Transactional
	void findById_firstLevelCacheDemo() {
		// Course course = repository.findById(10001L);
		// assertEquals("JPA in 50 Steps", course.getName() );
		Course course = repository.findById(10002L);
		logger.info("First Course Retrieved {}", course );
	
		Course course1 = repository.findById(10002L);
		logger.info("First Course Retrieved again {}", course );

		assertEquals("Spring in 50 Steps", course.getName() );	
		assertEquals("Spring in 50 Steps", course1.getName() );			  
	}
	
	@Test
	@DirtiesContext //Spring reset the data after this test is run
	public void deleteById_basic() {
		repository.deleteById(10002L);
		assertNull( repository.findById(10002L) );
	}

	@Test
	@DirtiesContext //Spring reset the data after this test is run
	public void save_basic() {
		Course course = repository.findById(10001L);
		assertEquals("JPA in 50 Steps", course.getName() );
		
		course.setName("JPA in 50 Steps - Updated");
		
		repository.save(course);
		
		Course course1 = repository.findById(10001L);
		assertEquals("JPA in 50 Steps - Updated", course1.getName() );
	}
	
	@Test
	@DirtiesContext 
	public void playWithEntityManager() {
		repository.playWithEntityManager();
		int foo = 0;
	}
	
	@Test
	@Transactional
	public void retrieveReviewsForCourse() {
		Course course = repository.findById(10001L);
		logger.info("{}", course.getReviews() );
	}
	
	@Test
	@Transactional
	public void retrieveCourseForReview() {
		Review review = em.find( Review.class, 50001L);
		logger.info("{}", review.getCourse() );
	}
}
