package com.producter.basketballcase.dtos.inputs;

import com.producter.basketballcase.entites.Position;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;

@Data
public class CreateBasketballPlayerInput {
    @Length(max = 255, message = "The name must be less than 255 characters")
    @Length(min = 1, message = "The name must be at least 1 characters")
    private String name;
    @Length(max = 255, message = "The surname must be less than 255 characters")
    @Length(min = 1, message = "The surname must be at least 1 characters")
    private String surname;
    @NotNull
    private Position position;
}
