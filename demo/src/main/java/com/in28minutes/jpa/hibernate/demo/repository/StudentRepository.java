package com.in28minutes.jpa.hibernate.demo.repository;

import javax.persistence.EntityManager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.in28minutes.jpa.hibernate.demo.entity.Course;
import com.in28minutes.jpa.hibernate.demo.entity.Passport;
import com.in28minutes.jpa.hibernate.demo.entity.Student;

@Repository
@Transactional
public class StudentRepository {

	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	EntityManager em;
	
	public Student findById( Long id ) {
		return em.find( Student.class, id);
	}
	
	public Student save( Student student ) {
		if ( student.getId() == null ) {
			// insert
			em.persist( student );
		} else {
			// update
			em.merge( student );
		}
		return student;
	}
	
	public void deleteById (Long id) {
		Student student = findById(id);
		em.remove(student);
	}
	
	public void saveStudentWithPassport() {
		Passport passport = new Passport("Z123456");
		em.persist(passport);
		
		Student student = new Student("Mike");
		student.setPassport(passport);
		em.persist(student);
	}
	
	/*
	public void playWithEntityManager() {
		Student student = new Student("Web Service in 100 Steps");
		
		em.persist( student );
		student.setName("Web Service in 100 Steps - Updated");
		// em.merge
	}
	*/
	
	/*
	public void playWithEntityManager() {
		Student student1 = new Student("Web Service in 100 Steps");
		em.persist( student1 );
		em.flush();
		
		Student student2 = new Student("Angular Js in 100 Steps");
		em.persist( student2 );
		
		em.flush();
		// em.clear() - clear all entities tracking by entity manager.
		
		// em.detach( student1 );
		em.detach( student2 ); // only student 2 detach, student 1 still tracked by em

		student1.setName("Web Service in 100 Steps - Updated");
		em.flush();
		
		student2.setName("Angular Js in 100 Stepss - Updated");
		em.flush();
	}
	*/
	/*
	public void playWithEntityManager() {
		Student student1 = new Student("Web Service in 100 Steps");
		em.persist( student1 );
		em.flush();
		
		Student student2 = new Student("Angular Js in 100 Steps");
		em.persist( student2 );
		
		em.flush();
		
		student1.setName("Web Service in 100 Steps - Updated");
		student2.setName("Angular Js in 100 Stepss - Updated");
		
		em.refresh(student1);
		
		em.flush();
		
	}
	*/
	
	public void someOperationToUnderstandPersistenceContext() {
		//DB Operation 1
		Student student = em.find(Student.class, 20001L);
		// Persistence Context (student)
		// logger.info("student -> {}", student);
		// logger.info("passport -> {}", student.getPassport() );

		// DB Operation 2
		Passport passport = student.getPassport();
		// Persistence Context (student, passport)

		// DB Operation 3
		passport.setNumber("E123457");
		// Persistence Context (student, passport++)

		// DB Operation 4
		student.setName("Ranga - updated");
		// Persistence Context (student++, passport++)
	}
	
	public void insertStudentAndCourse( Student student, Course course ) {


		
		student.addCourse(course);
		course.addStudent(student);
		
		em.persist(student);
		em.persist(course);
	}
}
