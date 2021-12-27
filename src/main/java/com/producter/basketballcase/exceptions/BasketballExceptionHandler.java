package com.producter.basketballcase.exceptions;

import com.producter.basketballcase.entites.Position;
import graphql.schema.CoercingParseValueException;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Arrays;

@Component
public class BasketballExceptionHandler {

    @ExceptionHandler({CoercingParseValueException.class})
    public InvalidPositionException handle(CoercingParseValueException e) {
        return new InvalidPositionException(
                String.format("Invalid value for position. Available values are %s", Arrays.asList(Position.values())));
    }



}
