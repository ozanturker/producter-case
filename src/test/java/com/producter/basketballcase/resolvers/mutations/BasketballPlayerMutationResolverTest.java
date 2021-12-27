package com.producter.basketballcase.resolvers.mutations;

import br.com.six2six.fixturefactory.Fixture;
import br.com.six2six.fixturefactory.Rule;
import com.producter.basketballcase.BasketballCaseApplication;
import com.producter.basketballcase.dtos.inputs.CreateBasketballPlayerInput;
import com.producter.basketballcase.entites.BasketballPlayer;
import com.producter.basketballcase.entites.Position;
import com.producter.basketballcase.exceptions.PlayerNotFoundException;
import com.producter.basketballcase.exceptions.TeamIsFullException;
import com.producter.basketballcase.repositories.BasketballPlayerRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        classes = BasketballCaseApplication.class,
        properties = "spring.main.lazy-initialization=true")
@Transactional
public class BasketballPlayerMutationResolverTest {

    @Autowired
    private BasketballPlayerMutationResolver basketballPlayerMutationResolver;

    @Autowired
    private BasketballPlayerRepository playerRepository;

    static CreateBasketballPlayerInput basketballPlayer = new CreateBasketballPlayerInput();

    @BeforeAll
    static void setUp() {
        basketballPlayer.setName("test_name");
        basketballPlayer.setSurname("test_surname");
        basketballPlayer.setPosition(Position.PF);
        Fixture.of(BasketballPlayer.class).addTemplate("valid", new Rule() {{
            add("name", random("Anderson Parra", "Arthur Hirata"));
            add("surname", random("nerd", "geek"));
            add("position", random(Position.values()));
        }});
    }

    @Test
    public void createBasketballPlayer() {
        BasketballPlayer player = basketballPlayerMutationResolver
                .createBasketballPlayer(BasketballPlayerMutationResolverTest.basketballPlayer);

        assertNotNull(player.getId());
        assertEquals(basketballPlayer.getName(), player.getName());
        assertEquals(basketballPlayer.getPosition(), player.getPosition());
        assertEquals(basketballPlayer.getSurname(), player.getSurname());
    }

    @Test
    public void createBasketballPlayerFullTeam() {
        List<BasketballPlayer> basketballPlayers = Fixture.from(BasketballPlayer.class).gimme(15, "valid");
        playerRepository.saveAll(basketballPlayers);
        assertThrows(TeamIsFullException.class, () -> {
            basketballPlayerMutationResolver
                    .createBasketballPlayer(BasketballPlayerMutationResolverTest.basketballPlayer);
        });
    }

    @Test
    public void deleteBasketballPlayer() {
        BasketballPlayer player = playerRepository.save(BasketballPlayer.builder()
                .name("test_name")
                .surname("test_surname")
                .position(Position.C)
                .build());
        boolean result = basketballPlayerMutationResolver
                .deleteBasketballPlayer(player.getId());

        assertTrue(result);
        Optional<BasketballPlayer> removedPlayer = playerRepository.findById(player.getId());
        assertTrue(removedPlayer.isEmpty());
    }

    @Test
    public void deleteBasketballPlayerNotExist() {
        assertThrows(PlayerNotFoundException.class, () -> {
            basketballPlayerMutationResolver
                    .deleteBasketballPlayer(10L);
        });
    }


}
