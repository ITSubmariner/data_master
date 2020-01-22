package com.datamaster.signatures;

import javax.persistence.*;

@Entity
@Table(name = "questions")
public class Question {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String text;
    private long number;
    @ManyToOne(optional = false, cascade = CascadeType.ALL)
    @JoinColumn(name = "poll_id")
    private Poll poll;

    public Question(String text, int number) {
        this.text = text;
        this.number = number;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public long getNumber() {
        return number;
    }

    public void setNumber(long number) {
        this.number = number;
    }
}
