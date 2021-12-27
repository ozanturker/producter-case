package com.producter.basketballcase.dtos;

import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

@Data
public class Pagination {
    @Min(value = 1, message = "The size must not be lower than 1")
    @Max(value = 20, message = "The size must not be higher than 20")
    private int size;
    @Min(value = 0, message = "The size must not be lower than 0")
    private int page;
}
