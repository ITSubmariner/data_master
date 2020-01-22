package com.datamaster.controllers;

import com.datamaster.repositories.PollRepository;
import com.datamaster.signatures.Poll;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/poll")
public class PollController {

    @Autowired private PollRepository pollRepository;

    @GetMapping
    public ResponseEntity get_polls(
            @RequestParam(required = false, defaultValue = "") String name,
            @RequestParam(required = false, defaultValue = "") String date,
            @RequestParam(required = false, defaultValue = "") String active,
            @RequestParam String sort,
            @RequestParam Pageable pageable) {
        Page<Poll> page = pollRepository.findAllWithParameters(name, date, active, pageable, Sort.by(Sort.Direction.ASC, sort));
        if (page.isEmpty()) return ResponseEntity.status(404).build();
        else return ResponseEntity.ok(page);
    }

    @PostMapping
    public ResponseEntity create_poll(@RequestBody Poll message) {
        if (pollRepository.findByName(message.getName())!=null) return ResponseEntity.status(404).build();
        pollRepository.save(message);
        return ResponseEntity.ok().build();
    }

    @PutMapping
    public ResponseEntity edit_poll(@RequestParam String name, @RequestBody Poll message) {
        Poll poll = pollRepository.findByName(name);
        if (poll==null) return ResponseEntity.status(404).build();
        poll.setName(message.getName());
        poll.setStart(message.getStart());
        poll.setEnd(message.getEnd());
        poll.setActive(message.isActive());
        poll.setQuestions(message.getQuestions());
        pollRepository.save(poll);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping
    public ResponseEntity delete_poll(@RequestParam String name) {
        Poll poll = pollRepository.findByName(name);
        if (poll==null) return ResponseEntity.status(404).build();
        pollRepository.delete(poll);
        return ResponseEntity.ok().build();
    }

}
