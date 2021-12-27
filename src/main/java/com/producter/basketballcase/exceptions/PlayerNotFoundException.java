package com.producter.basketballcase.exceptions;

import graphql.ErrorClassification;
import graphql.GraphQLError;
import graphql.language.SourceLocation;

import java.util.List;

public class PlayerNotFoundException extends GenericApplicationException implements GraphQLError {
    public PlayerNotFoundException(String message) {
        super(message);
    }


    @Override
    public List<SourceLocation> getLocations() {
        return null;
    }

    @Override
    public ErrorClassification getErrorType() {
        return null;
    }
}
