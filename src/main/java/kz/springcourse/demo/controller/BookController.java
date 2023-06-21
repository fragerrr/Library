package kz.springcourse.demo.controller;

import jakarta.validation.Valid;
import kz.springcourse.demo.model.Book;


import kz.springcourse.demo.model.Person;
import kz.springcourse.demo.model.Users;
import kz.springcourse.demo.security.UsersDetails;
import kz.springcourse.demo.service.BookService;
import kz.springcourse.demo.service.PersonService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

import org.springframework.web.bind.annotation.*;

import java.util.Objects;


@Controller
@RequestMapping("/book")
public class BookController {

    private Person person;
    private final BookService bookService;
    private final PersonService personService;

    @Autowired
    public BookController(BookService bookService, PersonService personService) {
        this.personService = personService;
        this.bookService = bookService;
    }


    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/new")
    public String create(@ModelAttribute("book") Book book){
        return "book/new";
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/new")
    public String create(@ModelAttribute("book") @Valid Book book,
                         BindingResult bindingResult){
        if(bindingResult.hasErrors())
            return "book/new";

        bookService.save(book);

        return "redirect:/home";
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/{id}/edit")
    public String edit(@PathVariable(name = "id") Integer id, Model model){
        model.addAttribute("book", bookService.findById(id));

        return "book/edit";
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("/{id}/edit")
    public String update(@ModelAttribute @Valid Book book,
                         BindingResult bindingResult){
        if(bindingResult.hasErrors())
            return "book/edit";

        bookService.update(book);

        return "redirect:/home";
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/{id}/delete")
    public String delete(@PathVariable(name = "id") Integer id){

        bookService.deleteBook(id);
        return "redirect:/home";

    }

    @GetMapping("/{id}/free")
    public String free(@PathVariable(name = "id") Integer id){

        bookService.personTakeBook(bookService.findById(id), null);
        return "redirect:/home";
    }

    @PreAuthorize("hasAnyRole('CLIENT')")
    @GetMapping("/{id}/take")
    public String take(@PathVariable(name = "id") Integer id){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        UsersDetails user = (UsersDetails) authentication.getPrincipal();
        Users users = user.getUser();

        person = personService.findByUserId(users.getId());

        bookService.personTakeBook(bookService.findById(id), person);
        return "redirect:/person/book-list";
    }

}
