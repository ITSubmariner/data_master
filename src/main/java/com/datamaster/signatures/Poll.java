package com.datamaster.signatures;

import com.datamaster.exceptions.BadDates;
import com.datamaster.exceptions.BadQuestions;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Entity
@Data
@NoArgsConstructor
public class Poll {

    public Poll(String name, LocalDate start, LocalDate end, boolean active, Set<Question> questions) {
        this.name = name;
        this.start = start;
        this.end = end;
        this.active = active;
        this.questions.addAll(questions);
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonIgnore
    private long id;

    @Column(unique = true)
    private String name;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    private LocalDate start;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    private LocalDate end;
    private boolean active;

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "poll_id")
    @OrderBy("number ASC")
    private Set<Question> questions = new HashSet<>();

    public void setQuestions(Set<Question> questions) {
        this.questions.clear();
        this.questions.addAll(questions);
    }

    public void check() throws BadQuestions, BadDates {
        if (this.bad_questions()) throw new BadQuestions();
        if (this.bad_dates()) throw new BadDates();
    }

    private boolean bad_questions() {
        if (this.questions.isEmpty()) return false;
        else return this.questions.stream().map(Question::getNumber).collect(Collectors.toSet()).size() != this.questions.size();
    }

    private boolean bad_dates() {
        return start.isAfter(end);
    }
}
