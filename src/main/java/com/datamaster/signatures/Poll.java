package com.datamaster.signatures;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "polls")
public class Poll {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;

    @Column(name = "name", unique = true, nullable = false)
    private String name;

    @Column(name = "start")
    private LocalDate start;

    @Column(name = "end")
    private LocalDate end;

    @Column(name = "active")
    private boolean active;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "poll")
    @OrderBy("number ASC")
    @Column(name = "questions")
    private List<Question> questions;

    public Poll(String name, LocalDate start, LocalDate end, boolean active, List<Question> questions) {
        this.name = name;
        this.start = start;
        this.end = end;
        this.active = active;
        this.questions = questions;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public LocalDate getStart() {
        return start;
    }
    public void setStart(LocalDate start) {
        this.start = start;
    }

    public LocalDate getEnd() {
        return end;
    }
    public void setEnd(LocalDate end) {
        this.end = end;
    }

    public boolean isActive() {
        return active;
    }
    public void setActive(boolean active) {
        this.active = active;
    }

    public List<Question> getQuestions() {
        return questions;
    }
    public void setQuestions(List<Question> questions) {
        this.questions = questions;
    }
}
