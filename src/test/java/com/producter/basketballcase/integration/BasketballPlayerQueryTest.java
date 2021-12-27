package com.producter.basketballcase.integration;

import br.com.six2six.fixturefactory.Fixture;
import br.com.six2six.fixturefactory.Rule;
import com.graphql.spring.boot.test.GraphQLResponse;
import com.graphql.spring.boot.test.GraphQLTestTemplate;
import com.producter.basketballcase.BasketballCaseApplication;
import com.producter.basketballcase.entites.BasketballPlayer;
import com.producter.basketballcase.entites.Position;
import com.producter.basketballcase.repositories.BasketballPlayerRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;

import javax.transaction.Transactional;
import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        classes = BasketballCaseApplication.class,
        properties = "spring.main.lazy-initialization=true")
public class BasketballPlayerQueryTest {

    @Autowired
    private GraphQLTestTemplate graphQLTestTemplate;

    @Autowired
    private BasketballPlayerRepository playerRepository;

    private static final String QUERY_REQUEST = "graphql/resolver/queries/requests/%s.graphqls";

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
    public void getBasketballPlayers() throws IOException {
        List<BasketballPlayer> basketballPlayers = Fixture.from(BasketballPlayer.class).gimme(5, "valid");
        playerRepository.saveAll(basketballPlayers);
        String testName = "get_basketball_players";
        GraphQLResponse graphQLResponse = graphQLTestTemplate.postForResource(String.format(QUERY_REQUEST, testName));
        assertEquals(graphQLResponse.getStatusCode(), HttpStatus.OK);
        assertNotNull(graphQLResponse.get("$.data.getBasketballPlayers[0].id"));
        assertEquals(basketballPlayers.get(0).getName(),
                graphQLResponse.get("$.data.getBasketballPlayers[0].name"));
        assertEquals(basketballPlayers.get(0).getSurname(),
                graphQLResponse.get("$.data.getBasketballPlayers[0].surname"));
        assertEquals(basketballPlayers.get(0).getPosition().name(),
                graphQLResponse.get("$.data.getBasketballPlayers[0].position"));
    }

}
