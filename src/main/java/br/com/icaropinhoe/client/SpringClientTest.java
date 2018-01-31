package br.com.icaropinhoe.client;

import java.util.Arrays;
import java.util.List;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import br.com.icaropinhoe.model.PageableResponse;
import br.com.icaropinhoe.model.Student;

public class SpringClientTest {

	
	public static void main(String[] args) {
		RestTemplate restTemplate = new RestTemplateBuilder()
				.rootUri("http://localhost:8080/v1/protected/students")
				.basicAuthorization("administrador", "123")
				.build();
		
		Student student = restTemplate.getForObject("/{id}", Student.class, 2);
		System.out.println(student);
		
		ResponseEntity<PageableResponse<Student>> responseEntity = 
				restTemplate.exchange("/?sort=id,desc&sort=name,asc", HttpMethod.GET, null, 
				new ParameterizedTypeReference<PageableResponse<Student>>() {});
		System.out.println(responseEntity.getBody());
	}
	
}
