package com.datamaster.repositories;

import com.datamaster.signatures.Poll;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface PollRepository extends JpaRepository<Poll, Long> {
    List<Poll> findAllByActiveTrue();
    List<Poll> findAllByName(String name);
    Poll findByName(String name);

    /*@Query("select p from Poll p where p.start >= ?1 and p.end <= ?1")
    List<Poll> findAllSatisfiedDate(LocalDate date);*/

    /*@Query("select p from Poll p where (:name is empty or p.name = :name) and (:date is empty or (p.start >= :date and p.end <= :date)) and (:active is empty or p.active = :active)")
    Page<Poll> findAllWithParameters(
            @Param("name") String name,
            @Param("date") String date,
            @Param("active") String active,
            Pageable pageable,
            Sort sort);*/

}
