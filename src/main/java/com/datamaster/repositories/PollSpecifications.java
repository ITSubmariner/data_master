package com.datamaster.repositories;

import com.datamaster.signatures.Poll;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class PollSpecifications {

    public static Specification<Poll> filter_name(String name) {
        return new Specification<Poll>() {
            @Override
            public Predicate toPredicate(Root<Poll> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                if (name.isEmpty()) return criteriaQuery.getRestriction();
                return criteriaBuilder.equal(root.get("name"), name);
            }
        };
    }

    public static Specification<Poll> filter_date(String date) {
        return new Specification<Poll>() {
            @Override
            public Predicate toPredicate(Root<Poll> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                if (date.isEmpty()) return criteriaQuery.getRestriction();
                LocalDate a = LocalDate.parse(date, DateTimeFormatter.ofPattern("dd-MM-yyyy"));
                return criteriaBuilder.and(criteriaBuilder.greaterThanOrEqualTo(root.get("end"), a), criteriaBuilder.lessThanOrEqualTo(root.get("start"), a));
            }
        };
    }

    public static Specification<Poll> filter_active(String active) {
        return new Specification<Poll>() {
            @Override
            public Predicate toPredicate(Root<Poll> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                if (active.isEmpty()) return criteriaQuery.getRestriction();
                return criteriaBuilder.equal(root.get("active"), Boolean.getBoolean(active));
            }
        };
    }



}
