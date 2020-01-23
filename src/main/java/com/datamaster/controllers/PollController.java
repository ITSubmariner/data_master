package com.datamaster.controllers;

import com.datamaster.exceptions.*;
import com.datamaster.repositories.PollRepository;
import com.datamaster.signatures.Poll;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.datamaster.repositories.PollSpecifications.*;

@RestController
@RequestMapping("/poll")
public class PollController {

    @Autowired private PollRepository pollRepository;

    @GetMapping
    public Page<Poll> get_polls(
            @RequestParam(required = false, defaultValue = "") String name,
            @RequestParam(required = false, defaultValue = "") String date,
            @RequestParam(required = false, defaultValue = "") String active,
            @RequestParam int page,
            @RequestParam int size,
            @RequestParam String sort) throws BadSort {
        if (sort.isEmpty()||!sort.equals("name")&&!sort.equals("start")) throw new BadSort();
        Page<Poll> response = pollRepository.findAll(
                filter_name(name).and(filter_date(date).and(filter_active(active))),
                PageRequest.of(page, size, Sort.by(Sort.Direction.ASC, sort))
                );
        return response;
    }

    @PostMapping
    public ResponseEntity create_poll(@RequestBody Poll message) throws BadDates, BadQuestions, PollExists {
        message.check();
        if (pollRepository.findByName(message.getName())!=null) throw new PollExists();
        pollRepository.save(message);
        return ResponseEntity.ok().build();
    }

    @PutMapping
    public ResponseEntity edit_poll(@RequestParam String name, @RequestBody Poll message) throws PollNotFound, BadDates, BadQuestions {
        message.check();
        Poll poll = pollRepository.findByName(name);
        if (poll==null) throw new PollNotFound();
        poll.setName(message.getName());
        poll.setStart(message.getStart());
        poll.setEnd(message.getEnd());
        poll.setActive(message.isActive());
        poll.setQuestions(message.getQuestions());
        pollRepository.save(poll);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping
    public ResponseEntity delete_poll(@RequestParam String name) throws PollNotFound {
        Poll poll = pollRepository.findByName(name);
        if (poll==null) throw new PollNotFound();
        pollRepository.delete(poll);
        return ResponseEntity.ok().build();
    }

}
