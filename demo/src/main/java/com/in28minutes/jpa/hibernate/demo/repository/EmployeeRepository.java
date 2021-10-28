package com.in28minutes.jpa.hibernate.demo.repository;

import java.util.List;

import javax.persistence.EntityManager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.in28minutes.jpa.hibernate.demo.entity.Course;
import com.in28minutes.jpa.hibernate.demo.entity.Employee;
import com.in28minutes.jpa.hibernate.demo.entity.Review;
import com.in28minutes.jpa.hibernate.demo.entity.ReviewRating;

@Repository
@Transactional
public class EmployeeRepository {

	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	EntityManager em;

	//insert an employee
	public void insert( Employee employee ) {
		em.persist(employee);
	}
	
	//retrieve all employees 
	public List<Employee> retrieveAllEmployees() {
		return em.createQuery(" select e from Employee e", Employee.class).getResultList();
	}
	
	public Course findById( Long id ) {
		return em.find( Course.class, id);
	}
	
	public Course save( Course course ) {
		if ( course.getId() == null ) {
			// insert
			em.persist( course );
		} else {
			// update
			em.merge( course );
		}
		return course;
	}
	
	public void deleteById (Long id) {
		Course course = findById(id);
		em.remove(course);
	}
	
	public void playWithEntityManager() {
		Course course1 = new Course("Web Service in 100 Steps");
		em.persist( course1 );
		
		Course course2 = findById(10001L);
		course2.setName("JPA in 50 steps - Updated");
		// em.merge
	}
	
	
	public void addReviewsForCourse( Long courseId, List<Review> reviews ) {
		
		Course course = findById(courseId);
		logger.info("course.getReviews() -> {}",course.getReviews());
	
		for ( Review review : reviews) {
			course.addReview(review);
			review.setCourse(course);
			em.persist(review);
		}
	
	}
	
	public void addReviewsForCourse() {
		//get the coruse 10003
		//add 2 reviews to it
		//save it to the database
		Course course = findById(10003L);
		logger.info("course.getReviews() -> {}",course.getReviews());
		
		Review review1 = new Review(ReviewRating.FIVE, "Great Hands-on Stuff.");
		Review review2 = new Review(ReviewRating.FIVE, "Hatsoff.");
		review1.setCourse(course);
		review2.setCourse(course);
		
		course.addReview(review1);	
		course.addReview(review2);
		
		em.persist(review1);
		em.persist(review2);
		
	}
	/*
	public void playWithEntityManager() {
		Course course = new Course("Web Service in 100 Steps");
		
		em.persist( course );
		course.setName("Web Service in 100 Steps - Updated");
		// em.merge
	}
	*/
	
	/*
	public void playWithEntityManager() {
		Course course1 = new Course("Web Service in 100 Steps");
		em.persist( course1 );
		em.flush();
		
		Course course2 = new Course("Angular Js in 100 Steps");
		em.persist( course2 );
		
		em.flush();
		// em.clear() - clear all entities tracking by entity manager.
		
		// em.detach( course1 );
		em.detach( course2 ); // only course 2 detach, course 1 still tracked by em

		course1.setName("Web Service in 100 Steps - Updated");
		em.flush();
		
		course2.setName("Angular Js in 100 Stepss - Updated");
		em.flush();
	}
	*/
	/*
	public void playWithEntityManager() {
		Course course1 = new Course("Web Service in 100 Steps");
		em.persist( course1 );
		em.flush();
		
		Course course2 = new Course("Angular Js in 100 Steps");
		em.persist( course2 );
		
		em.flush();
		
		course1.setName("Web Service in 100 Steps - Updated");
		course2.setName("Angular Js in 100 Stepss - Updated");
		
		em.refresh(course1);
		
		em.flush();
		
	}
	*/
}
