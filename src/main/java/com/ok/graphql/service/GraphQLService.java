package com.ok.graphql.service;

import com.ok.graphql.model.Author;
import com.ok.graphql.model.Book;
import com.ok.graphql.repository.AuthorRepository;
import com.ok.graphql.repository.BookRepository;
import graphql.GraphQL;
import graphql.schema.GraphQLSchema;
import graphql.schema.idl.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.stream.Stream;

@Component
public class GraphQLService {
    @Autowired
    private BookService bookService;
    @Autowired
    private BookRepository bookRepository;
    @Autowired
    private AuthorRepository authorRepository;
    @Autowired
    private AuthorService authorService;

    @Bean
    public GraphQL graphQL() throws IOException {
        loadDataToDatabase();
        SchemaParser schemaParser = new SchemaParser();
        ClassPathResource schema = new ClassPathResource("schema.graphql");
        TypeDefinitionRegistry typeDefinitionRegistry = schemaParser.parse(schema.getInputStream());
        RuntimeWiring runtimeWiring = RuntimeWiring.newRuntimeWiring()
                .type(TypeRuntimeWiring
                        .newTypeWiring("Query")
                        .dataFetcher("getBook", bookService.getBook()))
                .type(TypeRuntimeWiring.
                        newTypeWiring("Query")
                        .dataFetcher("getBooks", bookService.getBooks()))
                .type(TypeRuntimeWiring.
                        newTypeWiring("Mutation")
                        .dataFetcher("createBook", bookService.createBook()))
                .type(TypeRuntimeWiring.
                        newTypeWiring("Book")
                        .dataFetcher("author", authorService.getAuthor()))
                .type(TypeRuntimeWiring.
                        newTypeWiring("Mutation")
                        .dataFetcher("deleteBook", bookService.deleteBook()))
                .type(TypeRuntimeWiring.
                        newTypeWiring("Mutation")
                        .dataFetcher("updateBook", bookService.updateBook()))
                .build();
        SchemaGenerator generator = new SchemaGenerator();
        GraphQLSchema graphQLSchema = generator.makeExecutableSchema(typeDefinitionRegistry, runtimeWiring);
        return GraphQL.newGraphQL(graphQLSchema).build();
    }

    public void loadDataToDatabase() {
        Stream.of(
                new Author(1, "author 1", 20),
                new Author(2, "author 2", 30),
                new Author(3, "author 3", 40)
        ).forEach(author -> authorRepository.save(author));

        Stream.of(
                new Book(1, "First Book", 130, 1),
                new Book(2, "Second Book", 300, 2),
                new Book(3, "Book 3", 60, 3)
        ).forEach(book -> bookRepository.save(book));
    }
}
