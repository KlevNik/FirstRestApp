package ru.nic.spring.FirstRestApp.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController  //каждый метод в этом контроллере имеет аннотацию @ResponsBody(@Controller+@ResponseBody над каждым методом)
@RequestMapping("/api")
public class FirstRESTController {

   // @ResponseBody              //главная аннотация для создания REST приложений
    @GetMapping("/sayHello")
    public String sayHello() {
        return "Hello world";
    }

}
