package student1;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import model.Project;
import model.Student;

public class SampleTestcase {

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void run() {
		EntityManagerFactory emf = Persistence.createEntityManagerFactory("demo");
		EntityManager entityManager = emf.createEntityManager();

		entityManager.getTransaction().begin();
		Student student = new Student();
		student.setName(Long.toString(new Date().getTime()));
		entityManager.persist(student);
		entityManager.getTransaction().commit();

		entityManager.getTransaction().begin();
		Project project1 = new Project();
		project1.setNumber("100");
		entityManager.persist(project1);
		entityManager.getTransaction().commit();

		Project project2 = null;
		entityManager.getTransaction().begin();
		try {

			project2 = new Project();
			addProjects(entityManager);

			project2.setNumber("101");
			project1.getStudents().add(student);
			project2.getStudents().add(student);
			student.getProjects().add(project1);
			student.getProjects().add(project2);
			student.setFirstName("Tommy");
			student.setLastName("Mary");

			entityManager.persist(project2);

			entityManager.getTransaction().commit();
		} catch (Exception e) {
			entityManager.getTransaction().rollback();
			throw e;
		}
		Student foundStudent = entityManager.find(Student.class, 1L);
		entityManager.close();

		entityManager = emf.createEntityManager();

		updateStudentName(foundStudent, "new name", entityManager);

		// see that the ID of the user was set by Hibernate
		System.err.println("student=" + student + ", student.id=" + student.getId());

		// note that foundUser is the same instance as user and is a concrete
		// class (not a proxy)
		System.err.println("foundStudent=" + foundStudent);

		for (Project project : foundStudent.getProjects()) {
			System.err.println("project number=" + project.getNumber());
		}

		assertEquals(student.getName(), foundStudent.getName());
		entityManager.close();

		emf.close();

	}

	private void assertEquals(String name, String name2) {

		// TODO Auto-generated method stub

	}

	private void addProjects(EntityManager entityManager) {
		// TODO Auto-generated method stub

		CriteriaBuilder builder = entityManager.getCriteriaBuilder();
		CriteriaQuery<Student> query = builder.createQuery(Student.class);
		Root<Student> studentRoot = query.from(Student.class);
		query.where(builder.and(builder.equal(studentRoot.get("firstName"), "Tommy"),
				builder.equal(studentRoot.get("lastName"), "Mary")));
		List<Student> resultList = entityManager.createQuery(query).getResultList();
		for (Student student : resultList) {
			Project project = new Project();
			project.setNumber("1234");
			project.setTitle("Java Project");
			project.setProjectType(Project.ProjectType.TIME_AND_MATERIAL);
			entityManager.persist(project);
			student.getProjects().add(project);
			Set<Student> students = new HashSet<Student>();
			students.add(student);
			project.setStudents(students);
		}
	}

	public Student updateStudentName(Student student, String name, EntityManager em) {
		System.err.println(student.getId());
		student.setName(name);
		return em.merge(student);
	}

}
