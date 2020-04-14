package com.example.demo.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.Repository.ReactiveEmpRespository;
import com.example.demo.dao.Emp;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping(value = "/emp")
@Slf4j
public class UserController {

	@Autowired
	ReactiveEmpRespository empRepository;
	
	
	@GetMapping(path = "/msg")
	public String getMsg() {
		return "helloWorld";
	}
	
	@GetMapping(path="/all")
	public Flux<Emp> getAll(){
		return empRepository.findAll();
	}
	
	@PostMapping(path="/add")
	public Mono<ResponseEntity <Emp>> saveEmp(@RequestBody Emp emp){
		return empRepository.save(emp).log().map(e -> {
			return ResponseEntity.ok(e);
		}).defaultIfEmpty(
				new ResponseEntity<>(HttpStatus.NOT_FOUND));
	}
	
	@GetMapping(path="/{id}")
	public Mono<ResponseEntity<Emp>> getEmp(@PathVariable(value ="id") int id){
		return empRepository.findById(id).log().map(emp -> {
			return ResponseEntity.ok(emp);
		}).defaultIfEmpty(
				 new ResponseEntity<>(HttpStatus.NOT_FOUND));
	}
	
	@PutMapping(path="/{id}")
	public  Mono<ResponseEntity<Emp>> updateEmp(@PathVariable(value ="id") int id , @RequestBody Emp emp) {
		return empRepository.findById(id).flatMap(e -> {
			e.setName(emp.getName());
			return empRepository.save(e);
		}).map(updatedEmp -> {
			return ResponseEntity.ok(updatedEmp);
		}).defaultIfEmpty(
			new ResponseEntity<>(HttpStatus.NOT_FOUND)
		);
	}
	
	@DeleteMapping(value = "/{id}")
	public Mono<ResponseEntity<Void>> deleteEmp(@PathVariable(value = "id") int id) {
		return empRepository.findById(id).flatMap(emp ->
		empRepository.delete(emp).then(Mono.just(new ResponseEntity<Void>(HttpStatus.OK)))
		).defaultIfEmpty(
			new ResponseEntity<>(HttpStatus.NOT_FOUND)
		);
	}
}

