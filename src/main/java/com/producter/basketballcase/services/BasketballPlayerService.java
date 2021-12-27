package com.producter.basketballcase.services;

import com.producter.basketballcase.entites.BasketballPlayer;
import org.springframework.data.domain.PageRequest;

import java.util.List;

public interface BasketballPlayerService {

    public List<BasketballPlayer> getAllPlayers(PageRequest page);

    public boolean deleteBasketballPlayer(Long id);

    public BasketballPlayer createBasketballPlayer(BasketballPlayer basketballPlayer);

}
