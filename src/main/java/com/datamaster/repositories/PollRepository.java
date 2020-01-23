package com.datamaster.repositories;

import com.datamaster.signatures.Poll;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface PollRepository extends JpaRepository<Poll, Long>, JpaSpecificationExecutor<Poll> {
    Poll findByName(String name);
    Page<Poll> findAll(Specification<Poll> specification, Pageable page);

}
