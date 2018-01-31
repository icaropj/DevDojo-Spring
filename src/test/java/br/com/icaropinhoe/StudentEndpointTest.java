package br.com.icaropinhoe;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.embedded.LocalServerPort;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import br.com.icaropinhoe.model.Student;
import br.com.icaropinhoe.repository.StudentRepository;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class StudentEndpointTest {

	@Autowired
	private TestRestTemplate restTemplate;
	
	@LocalServerPort
	private int port;
	
	@MockBean
	private StudentRepository studentRepository; //alterar os dados somente no mock, nao os dados reais
	
	@Autowired
	private MockMvc mockMvc; //Alternativa ao rest template
	
	@TestConfiguration
	static class Config{
		@Bean
		public RestTemplateBuilder restTemplateBuilder() {
			return new RestTemplateBuilder().basicAuthorization("administrador", "123");
		}
	}
	
	@Test
	public void listStudentWhenUsernameAndPasswordAreIncorrectShouldReturnStatusCode401() {
		TestRestTemplate restTemplate2 = restTemplate.withBasicAuth("1", "1");
		ResponseEntity<Student> entity = restTemplate2.getForEntity("/v1/protected/students/", Student.class);
		
		assertThat(entity.getStatusCodeValue()).isEqualTo(401);
	}
	
	@Test
	public void getStudentByIdWhenUsernameAndPasswordAreIncorrectShouldReturnStatusCode401() {
		TestRestTemplate restTemplate2 = restTemplate.withBasicAuth("1", "1");
		ResponseEntity<Student> entity = restTemplate2.getForEntity("/v1/protected/students/{id}", Student.class, "1");
		
		assertThat(entity.getStatusCodeValue()).isEqualTo(401);
	}
	
	@Test
	public void listStudentWhenUsernameAndPasswordAreCorrectShouldReturnStatusCode200() {
		List<Student> students = Arrays.asList(
				new Student(1L, "s1", "s1@gmail.com"),
				new Student(2L, "s2", "s2@gmail.com")
		);
		BDDMockito.when(studentRepository.findAll()).thenReturn(students);

		ResponseEntity<Student> entity = restTemplate.getForEntity("/v1/protected/students/", Student.class);
		
		assertThat(entity.getStatusCodeValue()).isEqualTo(200);
	}
	
	@Test
	public void getStudentByIdWhenUsernameAndPasswordAreCorrectShouldReturnStatusCode200() {
		Student student = new Student(1L, "s1", "s1@gmail.com");
		BDDMockito.when(studentRepository.findOne(student.getId())).thenReturn(student);
		
		ResponseEntity<Student> entity = restTemplate.getForEntity("/v1/protected/students/{id}", Student.class, student.getId());
		
		assertThat(entity.getStatusCodeValue()).isEqualTo(200);
	}
	
	@Test
	public void getStudentByIdWhenUsernameAndPasswordAreCorrectAndStudentDoesNotExistShouldReturnStatusCode404() {
		ResponseEntity<Student> entity = restTemplate.getForEntity("/v1/protected/students/{id}", Student.class, -1L);
		
		assertThat(entity.getStatusCodeValue()).isEqualTo(404);
	}
	
}
