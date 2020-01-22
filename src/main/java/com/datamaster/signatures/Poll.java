package com.datamaster.signatures;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "polls")
@Data
@NoArgsConstructor
public class Poll {

    public Poll(String name, LocalDate start, LocalDate end, boolean active, List<Question> questions) {
        this.name = name;
        this.start = start;
        this.end = end;
        this.active = active;
        this.questions = questions;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(unique = true)
    private String name;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    private LocalDate start;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    private LocalDate end;
    private boolean active;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "poll")
    @OrderBy("number ASC")
    private List<Question> questions;
}
