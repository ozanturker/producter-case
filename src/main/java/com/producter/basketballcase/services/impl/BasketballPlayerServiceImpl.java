package com.producter.basketballcase.services.impl;

import com.producter.basketballcase.entites.BasketballPlayer;
import com.producter.basketballcase.exceptions.PlayerNotFoundException;
import com.producter.basketballcase.exceptions.TeamIsFullException;
import com.producter.basketballcase.repositories.BasketballPlayerRepository;
import com.producter.basketballcase.services.BasketballPlayerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BasketballPlayerServiceImpl implements BasketballPlayerService {

    @Autowired
    private BasketballPlayerRepository playerRepository;

    @Override
    public List<BasketballPlayer> getAllPlayers(PageRequest page) {
        return playerRepository.findAll(page).toList();
    }

    @Override
    public boolean deleteBasketballPlayer(Long id) {
        try {
            playerRepository.deleteById(id);
            return true;
        } catch (Exception ex) {
            throw new PlayerNotFoundException(String.format("Player not found with given id: %d ", id));
        }
    }

    @Override
    public BasketballPlayer createBasketballPlayer(BasketballPlayer basketballPlayer) {
        if (playerRepository.count() == 15) {
            throw new TeamIsFullException("Team is already full. Player count is 15");
        }
        return playerRepository.save(basketballPlayer);
    }
}
