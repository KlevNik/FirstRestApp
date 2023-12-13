package ru.nic.spring.FirstRestApp.util;

public class PersonNotCreatedException extends RuntimeException{
    public PersonNotCreatedException(String message) {
        super(message);  //передаем это сообщение в RuntimeException
    }
}
