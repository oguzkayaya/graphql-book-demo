package com.ok.graphql.service;

import com.ok.graphql.model.Author;
import com.ok.graphql.model.Book;
import com.ok.graphql.repository.AuthorRepository;
import graphql.schema.DataFetcher;
import graphql.schema.DataFetchingEnvironment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuthorService {

    @Autowired
    private AuthorRepository authorRepository;

    public int createAuthor(String name, int age) {
        Author author = new Author();
        author.setName(name);
        author.setAge(age);
        Author save = authorRepository.save(author);
        return save.getId();
    }


    public DataFetcher<Author> getAuthor() {
        return environment -> {
            Book book = environment.getSource();
            int authorId = book.getAuthorId();
            Author author = authorRepository.findById(authorId).get();
            return author;
        };
    }
}
