package com.demo.demo.api;

import com.demo.demo.entity.Book;
import com.demo.demo.repository.BookRepository;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController


public class BookAPI {
   List<Book> books = new ArrayList<>();
   @PostMapping("/api/book")
    public ResponseEntity createBook(@Valid @RequestBody Book book){
       books.add(book);
       return  ResponseEntity.ok(book);
   }
    @GetMapping("/api/book")
    public ResponseEntity getallBook(){
       return ResponseEntity.ok(books);
    }
    @GetMapping("/api/book/id")
    public void getBookById(){




    }

    //200 : thanh cong
    //400: bad request =>  request body ko chinh xac
}

