package br.com.icaropinhoe;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import javax.validation.ConstraintViolationException;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

import br.com.icaropinhoe.model.Student;
import br.com.icaropinhoe.repository.StudentRepository;

@RunWith(SpringRunner.class)
@DataJpaTest
//@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class StudentRepositoryTest {

	@Autowired
	private StudentRepository rep;
	
	@Rule
	public ExpectedException thrown = ExpectedException.none();	
	
	@Test
	public void createShouldPersistDate() {
		Student student = new Student("Willian", "willian@devdojo.com.br");
		this.rep.save(student);
		
		assertThat(student.getId()).isNotNull();
		assertThat(student.getName()).isEqualTo("Willian");
		assertThat(student.getEmail()).isEqualTo("willian@devdojo.com.br");
	}
	
	@Test
	public void deleteShouldRemoveDate() {
		Student student = new Student("Willian", "willian@devdojo.com.br");
		this.rep.save(student);
		this.rep.delete(student);
		
		assertThat(this.rep.findOne(student.getId())).isNull();
	}
	
	@Test
	public void updateShouldChangeAndPersistData() {
		Student student = new Student("Willian", "willian@devdojo.com.br");
		this.rep.save(student);
		
		student.setName("Willian 222");
		student.setEmail("willian222@devdojo.com.br");
		student = this.rep.save(student);

		assertThat(student.getName()).isEqualTo("Willian 222");
		assertThat(student.getEmail()).isEqualTo("willian222@devdojo.com.br");
	}
	
	@Test
	public void findByNameIgnoreCaseContainingShouldIgnoreCase() {
		Student student = new Student("Willian", "willian@devdojo.com.br");
		Student student2 = new Student("willian", "willian222@devdojo.com.br");
		this.rep.save(student);
		this.rep.save(student2);
		
		List<Student> studentList = this.rep.findByNameIgnoreCaseContaining("willian");
		
		assertThat(studentList.size()).isEqualTo(2);
	}
	
	@Test
	public void createWhenNameIsNullShouldThrowConstraintViolationException() {
		thrown.expect(ConstraintViolationException.class);

		this.rep.save(new Student());
	}
	
	@Test
	public void createWhenEmailIsNullShouldThrowConstraintViolationException() {
		thrown.expect(ConstraintViolationException.class);

		Student student = new Student();
		student.setName("Willian");
		this.rep.save(student);
	}
	
	@Test
	public void createWhenEmailIsNotValidShouldThrowConstraintViolationException() {
		thrown.expect(ConstraintViolationException.class);

		Student student = new Student();
		student.setName("Willian");
		student.setEmail("12344");
		this.rep.save(student);
	}
	
}
