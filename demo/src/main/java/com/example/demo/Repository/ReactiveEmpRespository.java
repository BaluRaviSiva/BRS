package com.example.demo.Repository;


import java.util.UUID;

import org.springframework.data.cassandra.repository.ReactiveCassandraRepository;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.dao.Emp;

@Repository
public interface ReactiveEmpRespository extends ReactiveCassandraRepository<Emp,Integer> {

}
