package com.datamaster.repositories;

import com.datamaster.signatures.Question;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QuestionRepository extends JpaRepository<Question, Long> {

}
