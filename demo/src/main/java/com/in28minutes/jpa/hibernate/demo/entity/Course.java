package com.in28minutes.jpa.hibernate.demo.entity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.NamedNativeQueries;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.PreRemove;
import javax.persistence.Table;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.annotations.Where;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
// @NamedQuery(name="query_get_all_courses", query="Select c From Course")
@NamedQueries( 
	value= {
		@NamedQuery(name="query_get_all_courses", query="Select c From Course c"),
		@NamedQuery(name="query_get_all_courses_join_fetch", query="Select c From Course c JOIN FETCH c.students s"), // also retreive student for each cause
		@NamedQuery(name="query_get_100_Step_courses", query="Select c From Course c where name like '%100 Steps'")
	}
)
@Cacheable
@SQLDelete(sql="update course set is_delete=true where id=?")
@Where(clause="is_delete = false")
public class Course {

	private static Logger LOGGER = LoggerFactory.getLogger(Course.class);
	
	@Id
	@GeneratedValue
	private Long id;
	
	@Column(nullable = false)
	private String name;
	
	// @OneToMany(mappedBy="course", fetch=FetchType.EAGER)
	@OneToMany(mappedBy="course")
	private List<Review> reviews = new ArrayList<>();
	
	@ManyToMany(mappedBy="courses") // students owning side of the relationship.
	@JsonIgnore
	private List<Student> students = new ArrayList<>();
	
	@UpdateTimestamp
	private LocalDateTime lastUpdatedDate;
	
	@CreationTimestamp
	private LocalDateTime createdDate;
	
	private boolean is_delete; 
	
	@PreRemove
	private void preRemove() {
		LOGGER.info("Setting is_delete = true");
		this.is_delete = true;
	}
	
	protected Course() {
	}
	
	public Course (String name ) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Long getId() {
		return id;
	}

	
	public List<Review> getReviews() {
		return reviews;
	}

	public void addReview(Review review) {
		this.reviews.add( review );
	}
	
	public void removeReview(Review review) {
		this.reviews.remove( review );
	}

	
	public List<Student> getStudents() {
		return students;
	}

	public void addStudent( Student student) {
		this.students.add( student );
	}

	@Override
	public String toString() {
		// return String.format("Course [%s] Review[%s]", name, reviews ); // reviews result in select * from reviews;
		return String.format("Course [%s]", name );
	}
	
	
}
