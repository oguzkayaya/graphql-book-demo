package com.ok.graphql.controller;

import com.ok.graphql.model.GraphQLRequestBody;
import graphql.ExecutionInput;
import graphql.ExecutionResult;
import graphql.GraphQL;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GraphQLController {

    @Autowired
    private GraphQL graphQL;

    @PostMapping(value = "/graphql")
    public ResponseEntity<?> executeQuery(@RequestBody GraphQLRequestBody body) {
        ExecutionResult execute = graphQL.execute(ExecutionInput.newExecutionInput()
                .query(body.getQuery())
                .operationName(body.getOperationName())
                .variables(body.getVariables())
                .build());
        return new ResponseEntity<>(execute, HttpStatus.OK);
    }
}
