package com.producter.basketballcase.resolvers.queries;

import br.com.six2six.fixturefactory.Fixture;
import br.com.six2six.fixturefactory.Rule;
import com.producter.basketballcase.BasketballCaseApplication;
import com.producter.basketballcase.dtos.Pagination;
import com.producter.basketballcase.entites.BasketballPlayer;
import com.producter.basketballcase.entites.Position;
import com.producter.basketballcase.repositories.BasketballPlayerRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.transaction.Transactional;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        classes = BasketballCaseApplication.class,
        properties = "spring.main.lazy-initialization=true")
public class BasketballPlayerQueryResolverTest {

    @Autowired
    private BasketballPlayerQueryResolver basketballPlayerQueryResolver;

    @Autowired
    private BasketballPlayerRepository playerRepository;


    @BeforeAll
    static void beforeAll() {
        Fixture.of(BasketballPlayer.class).addTemplate("valid", new Rule() {{
            add("name", random("Anderson Parra", "Arthur Hirata"));
            add("surname", random("nerd", "geek"));
            add("position", random(Position.values()));
        }});
    }

    @Test
    @Transactional
    public void getBasketballPlayers() {
        List<BasketballPlayer> players = Fixture.from(BasketballPlayer.class).gimme(5, "valid");
        playerRepository.saveAll(players);
        Pagination pagination = new Pagination();
        pagination.setPage(0);
        pagination.setSize(5);
        List<BasketballPlayer> result = basketballPlayerQueryResolver.getBasketballPlayers(pagination);
        assertArrayEquals(players.toArray(), result.toArray());

    }
}
