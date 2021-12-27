package com.producter.basketballcase.resolvers.queries;

import com.producter.basketballcase.dtos.Pagination;
import com.producter.basketballcase.entites.BasketballPlayer;
import com.producter.basketballcase.services.BasketballPlayerService;
import graphql.kickstart.tools.GraphQLQueryResolver;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@Component
@Validated
public class BasketballPlayerQueryResolver implements GraphQLQueryResolver {

    @Autowired
    private BasketballPlayerService playerService;

    public List<BasketballPlayer> getBasketballPlayers(@Valid Pagination pagination) {
        return playerService.getAllPlayers(PageRequest.of(pagination.getPage(), pagination.getSize()));
    }
}
