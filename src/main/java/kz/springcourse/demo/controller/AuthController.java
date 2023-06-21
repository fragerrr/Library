package kz.springcourse.demo.controller;

import jakarta.validation.Valid;
import kz.springcourse.demo.model.Person;
import kz.springcourse.demo.model.Users;
import kz.springcourse.demo.service.PersonService;
import kz.springcourse.demo.service.UsersDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;

@Controller
@RequestMapping("/auth")
public class AuthController {
    private final UsersDetailsService usersDetailsService;
    private final PersonService personService;

    @Autowired
    public AuthController(UsersDetailsService usersDetailsService, PersonService personService) {
        this.usersDetailsService = usersDetailsService;
        this.personService = personService;
    }

    @GetMapping("/login")
    public String login(){
        return "auth/login";
    }

    @GetMapping("/registration")
    public String registration(@ModelAttribute(name = "person") Person person){
        return "auth/registration";
    }

    @PostMapping("/registration")
    public String addPerson(@ModelAttribute(name = "person") @Valid Person person,
                            BindingResult bindingResult,
                            @RequestParam(name = "password") String pass,
                            @RequestParam(name = "email") String email,
                            Model model){

        if(!email.matches("(\\w+)@(\\w+)\\.(\\w+)") || usersDetailsService.findByEmail(email) != null){
            model.addAttribute("bademail", true);
            return "auth/registration";
        }

        if(!pass.matches("\\w{6,}")){
            model.addAttribute("badpass", true);
            return "auth/registration";
        }
        if(bindingResult.hasErrors()){
            return "auth/registration";
        }

        person.setUser(usersDetailsService.register(new Users(null, email, pass, "ROLE_CLIENT")));

        personService.save(person);

        return "redirect:/auth/login";
    }
}
