package com.datamaster.controllers;

import com.datamaster.exceptions.*;
import com.datamaster.repositories.PollRepository;
import com.datamaster.signatures.Poll;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.datamaster.repositories.PollSpecifications.*;

@RestController
@RequestMapping("poll")
@Api(value = "/poll", tags = {"Операции с опросами"})
public class PollController {

    @Autowired private PollRepository pollRepository;

    @ApiOperation(value = "Вывод опросов", notes = "Отображает все возможные опросы, исходя из заданных фильтров, пагинации и сортировки", response = Poll[].class)
    @ApiResponses({
            @ApiResponse(code = 200, message = "Все возможные опросы отображены", response = Poll[].class),
            @ApiResponse(code = 400, message = "Недопустимое значение сортировки")
    })
    @GetMapping
    public Page<Poll> get_polls(
            @ApiParam(value = "Фильтр: наименование опроса")
            @RequestParam(required = false, defaultValue = "") String name,
            @ApiParam(value = "Фильтр: дата")
            @RequestParam(required = false, defaultValue = "") String date,
            @ApiParam(value = "Фильтр: активность опроса", allowableValues = "true, false")
            @RequestParam(required = false, defaultValue = "") String active,
            @ApiParam(required = true, value = "Пагинация: номер страницы", defaultValue = "0")
            @RequestParam int page,
            @ApiParam(required = true, value = "Пагинация: количество строк на странице")
            @RequestParam int size,
            @ApiParam(required = true, value = "Сортировка", allowableValues = "name, start")
            @RequestParam String sort) throws BadSort {
        if (sort.isEmpty()||!sort.equals("name")&&!sort.equals("start")) throw new BadSort();
        return pollRepository.findAll(
                filter_name(name).and(filter_date(date).and(filter_active(active))),
                PageRequest.of(page, size, Sort.by(Sort.Direction.ASC, sort))
                );
    }

    @ApiOperation(value = "Создание опроса", notes = "Создает новый опрос")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Опрос создан"),
            @ApiResponse(code = 400, message = "Введенное наименование опроса уже существует"),
            @ApiResponse(code = 409, message = "Получен пустой опрос")
    })
    @PostMapping
    public ResponseEntity create_poll(
            @ApiParam(value = "Данные по опросу и все входящие вопросы")
            @RequestBody Poll message) throws BadDates, BadQuestions, PollExists, PollIsNull {
        if (message == null) throw new PollIsNull();
        message.check();
        if (pollRepository.findByName(message.getName())!=null) throw new PollExists();
        pollRepository.save(message);
        return ResponseEntity.ok().build();
    }

    @ApiOperation(value = "Изменение опроса", notes = "Изменяет существующий опрос")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Опрос изменен"),
            @ApiResponse(code = 400, message = "Неверно заданы даты начала и окончания опроса", responseContainer = "Dates are incorrect"),
            @ApiResponse(code = 400, message = "Номера вопросов повторяются", responseContainer = "Message numbers are repeated"),
            @ApiResponse(code = 404, message = "Опрос с заданным наименованием не найден"),
            @ApiResponse(code = 409, message = "Получен пустой опрос")
    })
    @PutMapping
    public ResponseEntity edit_poll(
            @ApiParam(value = "Наименование опроса, который необходимо изменить")
            @RequestParam String name,
            @ApiParam(value = "Данные по опросу и все входящие вопросы")
            @RequestBody Poll message) throws PollNotFound, BadDates, BadQuestions, PollIsNull {
        if (message == null) throw new PollIsNull();
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

    @ApiOperation(value = "Удаление опроса", notes = "Удаляет заданный опрос и все входящие в него вопросы")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Опрос успешно удален"),
            @ApiResponse(code = 404, message = "Опрос с заданным наименованием не найден")
    })
    @DeleteMapping
    public ResponseEntity delete_poll(
            @ApiParam(value = "Наименование опроса, который необходимо удалить")
            @RequestParam String name) throws PollNotFound {
        Poll poll = pollRepository.findByName(name);
        if (poll==null) throw new PollNotFound();
        pollRepository.delete(poll);
        return ResponseEntity.ok().build();
    }

}
