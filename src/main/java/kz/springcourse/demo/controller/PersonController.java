package kz.springcourse.demo.controller;

import jakarta.validation.Valid;
import kz.springcourse.demo.model.Book;
import kz.springcourse.demo.model.Person;
import kz.springcourse.demo.model.Users;
import kz.springcourse.demo.security.UsersDetails;
import kz.springcourse.demo.service.BookService;
import kz.springcourse.demo.service.PersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;


import java.util.List;
import java.util.Objects;

@Controller
@RequestMapping("/person")
public class PersonController {
    private Person person;
    private final PersonService personService;
    private final BookService bookService;

    @Autowired
    public PersonController(PersonService personService, BookService bookService) {
        this.personService = personService;
        this.bookService = bookService;
    }

    @GetMapping()
    public String index(Model model){

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        UsersDetails user = (UsersDetails) authentication.getPrincipal();
        Users users = user.getUser();

        person = personService.findByUserId(users.getId());

        model.addAttribute("owner", true);
        model.addAttribute("bookList", person.getBooks());

        return "person/index";
    }

    @GetMapping("/book-list")
    public String bookList(Model model){
        List<Book> bookList = bookService.findAll();

        bookList.removeIf(book -> book.getOwner()!=null);

        model.addAttribute("bookList", bookList);
        model.addAttribute("taker", true);
        return "person/all-books";
    }



    @GetMapping("/search-book")
    public String search(@RequestParam("name") String name,
                         Model model){

        List<Book> bookList = bookService.getBooksBySearch(name);
        bookList.removeIf(book -> book.getOwner()!=null);

        model.addAttribute("bookList", bookList);
        model.addAttribute("taker", true);

        return "person/all-books";
    }
}
