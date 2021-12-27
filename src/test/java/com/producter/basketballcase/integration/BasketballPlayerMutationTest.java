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
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        classes = BasketballCaseApplication.class,
        properties = "spring.main.lazy-initialization=true")

public class BasketballPlayerMutationTest {
    @Autowired
    private GraphQLTestTemplate graphQLTestTemplate;

    @Autowired
    private BasketballPlayerRepository playerRepository;

    private static final String QUERY_REQUEST = "graphql/resolver/mutations/requests/%s.graphqls";

    @BeforeAll
    static void beforeAll() {
        Fixture.of(BasketballPlayer.class).addTemplate("valid", new Rule() {{
            add("name", random("Anderson Parra", "Arthur Hirata"));
            add("surname", random("nerd", "geek"));
            add("position", random(Position.values()));
        }});

    }

    @Test
    public void createBasketballPlayer() throws IOException {
        String testName = "create_basketball_player";
        GraphQLResponse graphQLResponse = graphQLTestTemplate.postForResource(String.format(QUERY_REQUEST, testName));
        assertEquals(graphQLResponse.getStatusCode(), HttpStatus.OK);
        assertNotNull(graphQLResponse.get("$.data.createBasketballPlayer.id"));
        assertEquals("test_name", graphQLResponse.get("$.data.createBasketballPlayer.name"));
        assertEquals("test_surname", graphQLResponse.get("$.data.createBasketballPlayer.surname"));
        assertEquals(Position.C.name(), graphQLResponse.get("$.data.createBasketballPlayer.position"));
    }

    @Test
    public void createBasketballPlayerForFullTeam() throws IOException {
        List<BasketballPlayer> players = Fixture.from(BasketballPlayer.class).gimme(15, "valid");
        playerRepository.saveAllAndFlush(players);
        String testName = "create_basketball_player";
        GraphQLResponse graphQLResponse = graphQLTestTemplate.postForResource(String.format(QUERY_REQUEST, testName));
        assertEquals(graphQLResponse.getStatusCode(), HttpStatus.OK);
        assertEquals("Team is already full. Player count is 15", graphQLResponse.get("$.errors[0].message"));
    }

    @Test
    public void deleteBasketballPlayer() throws IOException {
        BasketballPlayer player = Fixture.from(BasketballPlayer.class).gimme("valid");
        player = playerRepository.saveAndFlush(player);
        String testName = "delete_basketball_player";
        GraphQLResponse graphQLResponse = graphQLTestTemplate.postForResource(String.format(QUERY_REQUEST, testName));
        assertEquals(graphQLResponse.getStatusCode(), HttpStatus.OK);
        assertTrue(Boolean.parseBoolean(graphQLResponse.get("$.data.deleteBasketballPlayer")));
        Optional<BasketballPlayer> deletedPlayer = playerRepository.findById(player.getId());
        assertTrue(deletedPlayer.isEmpty());
    }

    @Test
    public void deleteBasketballPlayerNotExist() throws IOException {
        String testName = "delete_basketball_player";
        GraphQLResponse graphQLResponse = graphQLTestTemplate.postForResource(String.format(QUERY_REQUEST, testName));
        assertEquals(graphQLResponse.getStatusCode(), HttpStatus.OK);
        assertEquals("Player not found with given id: 1 ", graphQLResponse.get("$.errors[0].message"));
    }
}
