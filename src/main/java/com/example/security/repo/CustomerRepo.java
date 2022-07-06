package com.example.security.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.security.entity.Customer;

@Repository
public interface CustomerRepo extends JpaRepository<Customer, Long> {

	Customer findByEmail(String email);

}

