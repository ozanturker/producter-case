package com.producter.basketballcase.services;

import br.com.six2six.fixturefactory.Fixture;
import br.com.six2six.fixturefactory.Rule;
import com.producter.basketballcase.BasketballCaseApplication;
import com.producter.basketballcase.entites.BasketballPlayer;
import com.producter.basketballcase.entites.Position;
import com.producter.basketballcase.exceptions.TeamIsFullException;
import com.producter.basketballcase.repositories.BasketballPlayerRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;

import javax.transaction.Transactional;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        classes = BasketballCaseApplication.class,
        properties = "spring.main.lazy-initialization=true")
public class BasketballServiceTest {

    @Autowired
    private BasketballPlayerService playerService;

    @Autowired
    private BasketballPlayerRepository playerRepository;

    @Test
    @Transactional
    public void createBasketballPlayer() {
        BasketballPlayer player = BasketballPlayer.builder()
                .position(Position.C).surname("test_surname").name("test_name").build();
        BasketballPlayer result = playerService.createBasketballPlayer(player);

        assertEquals(player.getName(), result.getName());
        assertEquals(player.getPosition(), result.getPosition());
        assertEquals(player.getSurname(), player.getSurname());
        assertNotNull(result.getId());
    }

    @Test
    @Transactional
    public void createBasketballPlayerTeamIsFull() {
        Fixture.of(BasketballPlayer.class).addTemplate("valid", new Rule() {{
            add("name", random("Anderson Parra", "Arthur Hirata"));
            add("surname", random("nerd", "geek"));
            add("position", random(Position.values()));
        }});
        List<BasketballPlayer> basketballPlayers = Fixture.from(BasketballPlayer.class).gimme(15, "valid");
        playerRepository.saveAll(basketballPlayers);


        BasketballPlayer player = BasketballPlayer.builder()
                .position(Position.C).surname("test_surname").name("test_name").build();
        assertThrows(TeamIsFullException.class,() -> {
            playerService.createBasketballPlayer(player);
        });
    }

    @Test
    @Transactional
    public void deleteBasketballPlayer() {
        BasketballPlayer player = BasketballPlayer.builder()
                .position(Position.C).surname("test_surname").name("test_name").build();

        BasketballPlayer savedPlayer = playerRepository.save(player);

        boolean result = playerService.deleteBasketballPlayer(savedPlayer.getId());
        assertTrue(result);
        assertTrue(playerRepository.findById(savedPlayer.getId()).isEmpty());
    }

    @Test
    @Transactional
    public void deleteBasketballPlayerNotExist() {
        assertThrows(Exception.class, new Executable() {
            @Override
            public void execute() throws Throwable {
                playerService.deleteBasketballPlayer(100L);
            }
        });
    }


    @Test
    @Transactional
    public void getBasketballPlayers() {
        BasketballPlayer player1 = playerRepository.save(BasketballPlayer.builder()
                .position(Position.C).surname("test_surname_1").name("test_name_1").build());
        BasketballPlayer player2 = playerRepository.save(BasketballPlayer.builder()
                .position(Position.PF).surname("test_surname_2").name("test_name_2").build());
        BasketballPlayer player3 = playerRepository.save(BasketballPlayer.builder()
                .position(Position.SF).surname("test_surname_3").name("test_name_3").build());

        List<BasketballPlayer> allPlayers = playerService.getAllPlayers(PageRequest.of(0, 10));

        assertEquals(3, allPlayers.size());
        assertEquals(player1.getName(), allPlayers.get(0).getName());
        assertEquals(player1.getPosition(), allPlayers.get(0).getPosition());
        assertEquals(player1.getSurname(), allPlayers.get(0).getSurname());
        assertNotNull(allPlayers.get(0).getId());
    }

}
