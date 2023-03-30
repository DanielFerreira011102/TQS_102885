package pt.ua.games;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import pt.ua.games.domain.Game;
import pt.ua.games.repositories.GameRepository;
import java.util.Arrays;

@Testcontainers
@SpringBootTest
class GamesApplicationTests {

	@Container
	public static PostgreSQLContainer container = new PostgreSQLContainer("postgres:latest")
			.withUsername("duke")
			.withPassword("password")
			.withDatabaseName("test");

	@Autowired
	private GameRepository gameRepository;

	@DynamicPropertySource
	static void properties(DynamicPropertyRegistry registry) {
		registry.add("spring.datasource.url", container::getJdbcUrl);
		registry.add("spring.datasource.password", container::getPassword);
		registry.add("spring.datasource.username", container::getUsername);
	}

	@Test
	void contextLoads() {
		String[] names = {"CS:GO", "Overwatch", "Uncharted 3"};
		int[] years = {2012, 2016, 2011};
		int[] ratings = {10, 6, 10};


		for (int i = 0; i < 3; i++) {
			Game game = new Game();
			game.setTitle(names[i]);
			game.setReleaseYear(years[i]);
			game.setRating(ratings[i]);
			gameRepository.save(game);
		}
	}

}
