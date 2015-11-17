package model;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

@Entity
@Table(name = "project")
public class Project {

	private String title;
	private ProjectType projectType;

	public enum ProjectType {
		FIXED, TIME_AND_MATERIAL
	}

	private static final String EnumType = null;

	@Id
	@GeneratedValue

	private Long id;

	@Column(length = 10, unique = true)
	private String number;

	@ManyToMany(cascade = CascadeType.ALL)
	@JoinTable(name = "Students_Projects", joinColumns = @JoinColumn(name = "Project_ID") , inverseJoinColumns = @JoinColumn(name = "Student_ID") )

	private Set<Student> students = new HashSet<Student>(0);

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
	}

	public Set<Student> getStudents() {
		return students;
	}

	public void setStudents(Set<Student> student) {
		this.students = student;
	}

	@Column(name = "TITLE")
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	// @Enumerated(EnumType.STRING)
	public ProjectType getProjectType() {
		return projectType;
	}

	public void setProjectType(ProjectType projectType) {
		this.projectType = projectType;
	}

}