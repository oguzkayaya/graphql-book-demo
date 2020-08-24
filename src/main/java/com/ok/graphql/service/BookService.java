package com.ok.graphql.service;

import com.ok.graphql.model.Author;
import com.ok.graphql.model.Book;
import com.ok.graphql.repository.BookRepository;
import graphql.schema.DataFetcher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookService {
    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private AuthorService authorService;

    public DataFetcher<Book> getBook() {
        return environment -> {
            int bookId = environment.getArgument("id");
            return bookRepository.findById(bookId).get();
        };
    }

    public DataFetcher<List<Book>> getBooks() {
        return environment -> bookRepository.findAll();
    }

    public DataFetcher<Book> createBook() {
        return environment -> {
            Book book = new Book();
            int authorId = authorService.createAuthor(
                    environment.getArgument("authorName"),
                    environment.getArgument("age"));
            book.setName(environment.getArgument("bookName"));
            book.setPages(environment.getArgument("pages"));
            book.setAuthorId(authorId);
            return bookRepository.save(book);
        };
    }

    public DataFetcher<Boolean> deleteBook() {
        return environment -> {
            try {
                int bookId = environment.getArgument("id");
                bookRepository.deleteById(bookId);
                return true;
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        };
    }

    public DataFetcher<Book> updateBook() {
        return environment -> {
            int bookId = environment.getArgument("id");
            Book book = bookRepository.findById(bookId).get();
            book.setAuthorId(environment.getArgument("authorId"));
            book.setPages(environment.getArgument("pages"));
            book.setName(environment.getArgument("bookName"));
            return bookRepository.save(book);
        };
    }
}
