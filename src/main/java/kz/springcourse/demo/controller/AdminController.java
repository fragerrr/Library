package kz.springcourse.demo.controller;

import kz.springcourse.demo.model.Book;
import kz.springcourse.demo.model.Person;
import kz.springcourse.demo.model.Users;
import kz.springcourse.demo.service.BookService;
import kz.springcourse.demo.service.PersonService;
import kz.springcourse.demo.service.UsersDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminController {
    private final BookService bookService;
    private final PersonService personService;
    private final UsersDetailsService usersDetailsService;
    @Autowired
    public AdminController(BookService bookService, PersonService personService, UsersDetailsService usersDetailsService) {
        this.bookService = bookService;
        this.personService = personService;
        this.usersDetailsService = usersDetailsService;
    }

    @GetMapping
    public String index(Model model){
        model.addAttribute("personList", personService.findAll());

        return "admin/index";
    }

    @GetMapping("/free-list")
    public String freeList(Model model){
        List<Book> bookList = bookService.findAll();

        bookList.removeIf(book -> book.getOwner()!=null);

        model.addAttribute("bookList", bookList);

        return "admin/free-list";
    }

    @GetMapping("/taken-list")
    public String takenList(Model model){
        List<Book> bookList = bookService.findAll();

        bookList.removeIf(book -> book.getOwner()==null);

        model.addAttribute("bookList", bookList);
        model.addAttribute("owner", true);

        return "admin/free-list";
    }

    @GetMapping("/{id}/delete")
    public String deletePerson(@PathVariable(name = "id")Integer id){
        Person person = personService.findById(id);
        Users users = person.getUser();

        personService.delete(id);

        usersDetailsService.delete(users);

        return "redirect:/admin";
    }

    @GetMapping("/{id}/books")
    public String books(@PathVariable(name = "id")Integer id,
                        Model model){
        Person person = personService.findById(id);

        model.addAttribute("bookList", person.getBooks());
        model.addAttribute("owner", true);

        return "admin/free-list";
    }
}
