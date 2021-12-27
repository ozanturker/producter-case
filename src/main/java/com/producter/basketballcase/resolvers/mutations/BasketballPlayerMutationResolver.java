package com.producter.basketballcase.resolvers.mutations;

import com.producter.basketballcase.dtos.inputs.CreateBasketballPlayerInput;
import com.producter.basketballcase.entites.BasketballPlayer;
import com.producter.basketballcase.exceptions.PlayerNotFoundException;
import com.producter.basketballcase.exceptions.TeamIsFullException;
import com.producter.basketballcase.repositories.BasketballPlayerRepository;
import com.producter.basketballcase.services.BasketballPlayerService;
import graphql.kickstart.tools.GraphQLMutationResolver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import javax.transaction.Transactional;
import javax.validation.Valid;

@Component
@Validated
public class BasketballPlayerMutationResolver implements GraphQLMutationResolver {

    @Autowired
    private BasketballPlayerService playerService;

    @Transactional
    public BasketballPlayer createBasketballPlayer(@Valid CreateBasketballPlayerInput input) {
        BasketballPlayer player = BasketballPlayer.builder()
                .name(input.getName())
                .surname(input.getSurname())
                .position(input.getPosition())
                .build();

        return this.playerService.createBasketballPlayer(player);
    }

    @Transactional
    public boolean deleteBasketballPlayer(Long id) {
        return this.playerService.deleteBasketballPlayer(id);
    }
}
