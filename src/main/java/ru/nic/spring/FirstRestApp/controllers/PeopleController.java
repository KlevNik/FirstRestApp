package ru.nic.spring.FirstRestApp.controllers;


import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import ru.nic.spring.FirstRestApp.models.Person;
import ru.nic.spring.FirstRestApp.services.PeopleService;
import ru.nic.spring.FirstRestApp.util.PersonErrorResponse;
import ru.nic.spring.FirstRestApp.util.PersonNotCreatedException;
import ru.nic.spring.FirstRestApp.util.PersonNotFoundException;

import java.util.List;

@RestController    // @Controller + @ResponseBody над каждым методом
@RequestMapping("/people")
public class PeopleController {  // Список из объектов класса Person

   private final PeopleService peopleService;

   @Autowired
    public PeopleController(PeopleService peopleService) {
        this.peopleService = peopleService;
    }
    @GetMapping()
    public List<Person> getPeople() {
        return peopleService.findAll(); //Jackson конвертирует эти объекты в JSON
    }

    @GetMapping("/{id}")
    public Person getPerson(@PathVariable("id") int id) {
       //Cтатус 200 - все пошло хорошо (не было ошибок на сервере)
       return peopleService.findOne(id); //Jackson конвертинует в JSON
    }

    @PostMapping
    public ResponseEntity<HttpStatus> create(@RequestBody @Valid Person person,
                                             BindingResult bindingResult){ //проверяем на валидность с помощью BindingResult
        if (bindingResult.hasErrors()) {
            //Проходимся по всем ошибкам что бы отправить ответ клиенту об неправильности ввода
            StringBuilder errorMessage = new StringBuilder();

           List<FieldError> errors = bindingResult.getFieldErrors();
            for (FieldError error : errors){
                errorMessage.append(error.getField())
                        .append(" - ").append(error.getDefaultMessage())
                        .append(";");
            }

            throw new PersonNotCreatedException(errorMessage.toString());
        }
        peopleService.save(person);
        // отправляем HTTP ответ с пустым телом и со статусом 200
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @ExceptionHandler
    private ResponseEntity<PersonErrorResponse> handleException(PersonNotFoundException e){
        PersonErrorResponse response = new PersonErrorResponse(
                "Person with this id wasn't found!",
                System.currentTimeMillis()
        );
        // В HTTP ответе тело ответа (response) и статус в заголовке
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND); //NOT_FOUND - 404 статус
    }

    @ExceptionHandler
    private ResponseEntity<PersonErrorResponse> handleException(PersonNotCreatedException e){
        PersonErrorResponse response = new PersonErrorResponse(
                e.getMessage(),
                System.currentTimeMillis()
        );
        // В HTTP ответе тело ответа (response) и статус в заголовке
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST); //BAD_REQUEST - ответ с сервера 400 статус
    }
}
