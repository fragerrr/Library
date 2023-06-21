package kz.springcourse.demo.service;

import kz.springcourse.demo.model.Book;
import kz.springcourse.demo.model.Person;
import kz.springcourse.demo.repository.BookRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;



@Service
@Transactional
public class BookService {
    private final BookRepository bookRepository;

    @Autowired
    public BookService(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }


    public List<Book> findAll(){
        return bookRepository.findAll();
    }
    public List<Book> getBooks(Boolean sort){
        if(sort){
            return bookRepository.findAll(Sort.by("year"));
        } else{
            return bookRepository.findAll();
        }

    }

    public List<Book> getBooksBySearch(String search){
        return bookRepository.findByNameStartingWith(search);
    }

    public List<Book> getBookPagination(Integer page, Integer books_number, Boolean sort){
        if(sort){
            return bookRepository.findAll(PageRequest.of(page, books_number, Sort.by("year"))).toList();
        } else{
            return bookRepository.findAll(PageRequest.of(page, books_number)).toList();
        }

    }

    public Book findById(Integer id){
        return bookRepository.findById(id).orElse(null);
    }


    public void save(Book book){
        bookRepository.save(book);
    }


    public void update(Book book){
        Book bookToBeUpdated = bookRepository.findById(book.getId()).orElse(null);

        if(bookToBeUpdated != null){
            bookToBeUpdated.setYear(book.getYear());
            bookToBeUpdated.setName(book.getName());
            bookToBeUpdated.setAuthor(book.getAuthor());
            bookToBeUpdated.setOwner(book.getOwner());
        }
    }


    public void personTakeBook(Book book, Person owner){
        Book bookToBeUpdated = bookRepository.findById(book.getId()).orElse(null);
        assert bookToBeUpdated != null : "Book to be updated is null";
        bookToBeUpdated.setOwner(owner);
    }

    public void deleteBook(Integer id){
        bookRepository.delete(bookRepository.findById(id).orElse(null));
    }

}
