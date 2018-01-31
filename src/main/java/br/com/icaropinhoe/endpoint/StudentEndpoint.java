package br.com.icaropinhoe.endpoint;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.icaropinhoe.error.ResourceNotFoundException;
import br.com.icaropinhoe.model.Student;
import br.com.icaropinhoe.repository.StudentRepository;

@RestController
@RequestMapping("v1")
public class StudentEndpoint {
	
	@Autowired
	private StudentRepository studentRep;
	
	@GetMapping("protected/students")
	public ResponseEntity<?> listAll(Pageable pageable){
		return new ResponseEntity<>(studentRep.findAll(pageable), HttpStatus.OK);
	}
	
	@GetMapping("protected/students/byName/{name}")
	public ResponseEntity<?> findByName(@PathVariable("name") String name){
		return new ResponseEntity<>(studentRep.findByNameIgnoreCaseContaining(name), HttpStatus.OK);
	}
	
	@GetMapping("protected/students/{id}")
	public ResponseEntity<?> getById(@PathVariable("id") Long id, @AuthenticationPrincipal UserDetails userDetails){
		System.out.println(userDetails);
		verifyIfStudentExists(id);
		Student student = studentRep.findOne(id);
		return new ResponseEntity<>(student, HttpStatus.OK);
	}
	
	@PostMapping("admin/students")
	@Transactional
	public ResponseEntity<?> save(@Valid @RequestBody Student student){
		return new ResponseEntity<>(studentRep.save(student), HttpStatus.CREATED);
	}
	
	@DeleteMapping("admin/students/{id}")
//	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<?> remove(@PathVariable("id") Long id){
		verifyIfStudentExists(id);
		studentRep.delete(id);
		return new ResponseEntity<>(HttpStatus.OK);//Sempre o mesmo resultado(idempotent)
	}
	
	@PutMapping("admin/students")
	public ResponseEntity<?> update(@RequestBody Student student){
		verifyIfStudentExists(student.getId());
		studentRep.save(student);
		return new ResponseEntity<>(HttpStatus.OK);//Sempre o mesmo resultado(idempotent)
	}
	
	private void verifyIfStudentExists(Long id) {
		Student student = studentRep.findOne(id);
		if(student == null) {
			throw new ResourceNotFoundException("Student not found for ID: " + id);
//			return new ResponseEntity<>(new CustomErrorType("Student not found"), HttpStatus.NO_CONTENT);
		}
	}
	
}
