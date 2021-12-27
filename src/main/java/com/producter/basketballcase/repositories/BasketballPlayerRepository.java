package com.producter.basketballcase.repositories;

import com.producter.basketballcase.entites.BasketballPlayer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

@Repository
public interface BasketballPlayerRepository extends JpaRepository<BasketballPlayer, Long> {

}
